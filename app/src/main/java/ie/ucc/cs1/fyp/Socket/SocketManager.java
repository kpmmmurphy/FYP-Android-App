package ie.ucc.cs1.fyp.Socket;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import ie.ucc.cs1.fyp.BuildConfig;
import ie.ucc.cs1.fyp.Constants;
import ie.ucc.cs1.fyp.Model.Packet;
import ie.ucc.cs1.fyp.Model.Session;
import ie.ucc.cs1.fyp.Utils;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class SocketManager {

    private Boolean DEBUG = true;
    private static String LOGTAG = "__SocketManager";
    private static SocketManager __instance;
    private Context mContext;
    private Gson gson;
    private WifiManager wifiManager;
    private String ourIP;

    private InetAddress multicastGroup;
    private MulticastSocket multicastSocket;
    private ServerSocket serverSocket;
    private Thread sensorValueThread;
    private Socket piDirectSocket;
    private DatagramPacket multicastPacket;
    private String recievedString;

    private int ACK_SOCKET_TIMEOUT = 40000;
    private int MULTICAST_TIMEOUT = 10000;
    private int PACKET_MAX_SIZE = 1024;

    private SocketManager(Context context) {
        this.mContext = context;
        gson = new Gson();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ourIP = Session.getInstance(mContext).getIp_address();
        new SetupNetworkingTask().execute();

    }

    public static synchronized SocketManager getInstance(Context context) {
        if (__instance == null) {
            __instance = new SocketManager(context);
        }
        return __instance;
    }

    private class ConnectToPi extends AsyncTask<Session, Void, Boolean> {

        private String LOGTAG = "__ConnectToPi";

        @Override
        protected Boolean doInBackground(Session... sessions) {
            Utils.methodDebug(LOGTAG);
            Session session = sessions[0];

            try {
                byte[] connectPacket = PacketFactory.newConnectPacket(session);
                multicastSocket = createMulticastSocket(mContext);
                multicastGroup = InetAddress.getByName(Constants.SOCKET_MULTICAST_GROUP_IP);
                multicastPacket = new DatagramPacket(connectPacket, connectPacket.length, new InetSocketAddress(multicastGroup, Constants.SOCKET_MULTICAST_PORT));
                multicastSocket.setTimeToLive(30);
                multicastSocket.send(multicastPacket);
                multicastSocket.close();

                if (BuildConfig.DEBUG) {
                    Log.d(LOGTAG, "Sending Multicast Packet");
                }

                if (serverSocket == null) {
                    serverSocket = createServerSocket(ourIP);
                }

                try {
                    //Now wait for ACK
                    Socket ackSocket = serverSocket.accept();
                    ackSocket.setSoTimeout(ACK_SOCKET_TIMEOUT);
                    if (BuildConfig.DEBUG) {
                        Log.d(LOGTAG, "Created ACK Socket :: " + ackSocket.toString());
                    }

                    String receivedString = readPacket(ackSocket.getInputStream());

                    if (BuildConfig.DEBUG) {
                        Log.d(LOGTAG, "Received Packet :: " + receivedString);
                    }

                    Packet packet = gson.fromJson(receivedString, Packet.class);
                    if (BuildConfig.DEBUG) {
                        if (packet.getPayload() != null && packet.getPayload().getPaired() != null)
                            Log.e(LOGTAG, String.valueOf(packet.getPayload().getPaired().getStatus_code()));
                    }
                    if (packet.getPayload() != null && packet.getService().equals(Constants.SERVICE_PAIRED) && packet.getPayload().getPaired().getStatus_code() == Constants.CONNECT_SUCCESS) {
                        //Set session to connected
                        if (BuildConfig.DEBUG) {
                            Log.d(LOGTAG, "Connected to Pi");
                        }
                        session.setConnectedToPi(true);
                        session.setPiIPAddress(ackSocket.getInetAddress());
                    }
                    ackSocket.close();

                } catch (SocketException e) {
                    e.printStackTrace();
                }

            } catch (SocketException e) {
                Log.e("UDP", "Socket Error", e);
            } catch (IOException e) {
                Log.e("UDP", "IO Error", e);
            }
            return session.isConnectedToPi();
        }

        @Override
        protected void onPostExecute(Boolean connected) {
            Utils.methodDebug(LOGTAG);
            //Send broadcast to Main Activity
            if (BuildConfig.DEBUG) {
                Log.d(LOGTAG, "Sending Broadcast to MainActivity");
            }
            sendConnectedBroadcast();
        }
    }

    public void startConnectionToPi(Session session) {
        new ConnectToPi().execute(session);
    }

    public synchronized void startPiDirectThread() {
        sensorValueThread = new Thread() {
            @Override
            public void run() {
                try {
                    //Request Graph Data Right Away!!
                    SocketManager.getInstance(mContext).sendPacketToPi(Utils.toJson(new Packet(Constants.JSON_VALUE_WIFI_DIRECT_GET_GRAPH_DATA, null)));
                    while (!this.isInterrupted()) {
                        piDirectSocket = serverSocket.accept();
                        String currentPacket = readPacket(piDirectSocket.getInputStream());
                        if (BuildConfig.DEBUG) {
                            Log.d(LOGTAG, "Received :: " + currentPacket);
                        }
                        PacketManager.getInstance(mContext).deliverPacket(gson.fromJson(currentPacket, Packet.class));
                    }
                    piDirectSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        sensorValueThread.start();
    }

    public synchronized void sendPacketToPi(String packet) {
        new SendPacketToPi().execute(packet);
    }

    public class SendPacketToPi extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (BuildConfig.DEBUG) {
                Log.d(LOGTAG, "Packet Sent to Pi");
            }
        }

        @Override
        protected Void doInBackground(String... strings) {
            String packet = strings[0];
            if (BuildConfig.DEBUG) {
                Log.d(LOGTAG, "Sending Packet to Pi -> " + packet);
            }

            try {
                Socket socket = new Socket(Session.getInstance(mContext).getPiIPAddress(), Constants.SOCKET_CLIENT_PORT);
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.println(packet);
                out.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private MulticastSocket createMulticastSocket(Context context)
            throws UnknownHostException, SocketException, IOException {
        Utils.methodDebug(LOGTAG);

        MulticastSocket multicastSocket = new MulticastSocket(Constants.SOCKET_MULTICAST_PORT);
        if (wifiManager != null) {
            WifiManager.MulticastLock lock = wifiManager.createMulticastLock(LOGTAG);
            lock.acquire();
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int intaddr = wifiInfo.getIpAddress();
        byte[] byteaddr = new byte[]{
                (byte) (intaddr & 0xff),
                (byte) (intaddr >> 8 & 0xff),
                (byte) (intaddr >> 16 & 0xff),
                (byte) (intaddr >> 24 & 0xff)};
        InetAddress addr = InetAddress.getByAddress(byteaddr);
        NetworkInterface netIf = NetworkInterface.getByInetAddress(addr);
        multicastSocket.joinGroup(new InetSocketAddress(InetAddress.getByName(Constants.SOCKET_MULTICAST_GROUP_IP), Constants.SOCKET_MULTICAST_PORT), netIf);
        multicastSocket.setTimeToLive(30);
        return multicastSocket;
    }

    private ServerSocket createServerSocket(String connectToIp) {
        ServerSocket ss = null;
        try {
            //ss = new ServerSocket(Constants.SOCKET_SERVER_PORT, 0, InetAddress.getByName(connectToIp));
            ss = new ServerSocket(Constants.SOCKET_SERVER_PORT);
            if (BuildConfig.DEBUG) {
                Log.d(LOGTAG, ss.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ss;
    }

    private class SetupNetworkingTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            serverSocket = createServerSocket(ourIP);
            return null;
        }
    }

    private String readPacket(InputStream inputStream) {
        String s = "";
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            s = bufferedReader.readLine();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return s;
    }

    private byte[] trimPacket(byte[] packet) {
        int count = 0;
        for (byte charr : packet) {
            if (charr == 0) {
                break;
            }
            ++count;
        }
        return Arrays.copyOf(packet, count);
    }

    private void sendConnectedBroadcast() {
        if (BuildConfig.DEBUG) {
            Log.d(LOGTAG, "Sending Connected to Pi Intent");
        }
        Intent connectedIntent = new Intent(Constants.INTENT_CONNECTED_TO_PI);
        connectedIntent.putExtra(Constants.SERVICE_PAIRED, Session.getInstance(mContext).isConnectedToPi());
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(connectedIntent);
    }
}