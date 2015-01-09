package ie.ucc.cs1.fyp.Socket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import ie.ucc.cs1.fyp.Model.PiResponse;
import ie.ucc.cs1.fyp.Utils;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class SocketManager {

    private Boolean DEBUG = true;
    private static String LOGTAG = "__SocketManager";
    private Context mContext;

    private InetAddress multicastGroup;
    private MulticastSocket multicastSocket;
    private ServerSocket serverSocket;
    private DatagramPacket  multicastPacket;
    private DatagramPacket  sendacket;
    private DatagramPacket  recvacket;
    private String recievedString;

    private BufferedReader bufferedReader;
    private InputStreamReader inputStreamReader;
    private int PACKET_MAX_SIZE = 1024;

    public SocketManager(Context context){
        this.mContext = context;
        try {
            multicastGroup  = InetAddress.getByName(Constants.SOCKET_MULTICAST_GROUP_IP);
            multicastSocket = createMulticastSocket(mContext);

        }catch (UnknownHostException e){
            e.printStackTrace();
        }catch (SocketException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToPi(final Session session){
        Utils.methodDebug(LOGTAG);
        serverSocket    = createServerSocket(session);
        new Thread(new Runnable() {
            public void run() {
                try {
                    byte[] connectPacket = PacketFactory.newConnectPacket(session);

                    //Socket client = new Socket(InetAddress.getByName("192.168.42.1"), 5007);
                    multicastPacket = new DatagramPacket(connectPacket, connectPacket.length, new InetSocketAddress(multicastGroup, Constants.SOCKET_MULTICAST_PORT));
                    //OutputStream outputStream =  client.getOutputStream();
                    //DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    //outputStream.write("Did it work".getBytes());
                    multicastSocket.setTimeToLive(30);

                    multicastSocket.send(multicastPacket);
                    if (BuildConfig.DEBUG){
                        Log.e(LOGTAG, "Sending Multicast");
                    }

                    //Now wait for ACK
                    Socket ackSocket = serverSocket.accept();
                    if (BuildConfig.DEBUG){
                        Log.d(LOGTAG, ackSocket.toString());
                    }

                    String receivedString = readPacket(ackSocket.getInputStream());
                    if (BuildConfig.DEBUG) {
                        Log.d(LOGTAG, "Received Packet :: " + receivedString);
                    }

                    PiResponse piResponse = (PiResponse)new Gson().fromJson(receivedString, PiResponse.class);
                    if(piResponse.getService().equals(Constants.SERVICE_PAIRED) && piResponse.getStatus_code() == Constants.CONNECT_SUCCESS){
                        //Set session to connected
                        Log.e(LOGTAG, "It Worked!!!");
                    }
                    recievedString = new String(trimPacket(multicastPacket.getData()));
                    if(BuildConfig.DEBUG){
                        Log.d("UDP", " Received String: " + recievedString);
                        InetAddress ipaddress = multicastPacket.getAddress();
                        int port = multicastPacket.getPort();
                        Log.d("UDP", "IPAddress : " + ipaddress.toString());
                        Log.d("UDP", "Port : " + Integer.toString(port));
                    }

                } catch (SocketException e) {
                    Log.e("UDP", "Socket Error", e);
                } catch (IOException e) {
                    Log.e("UDP", "IO Error", e);
                }
            }
        }).start();
    }

    private class ConnectToPi extends AsyncTask<Session, Void, Void>{

        @Override
        protected Void doInBackground(Session... sessions) {
            Utils.methodDebug("vdkvbgdrvndrvgn");
            connectToPi(sessions[0]);
            return null;
        }
    }

    public void startConnectionToPi(Session session){
        new ConnectToPi().execute(session);
    }

    public void createMulticastServerThread(){
        Utils.methodDebug(LOGTAG);
        new Thread(new Runnable() {
            public void run() {
                try {
                    MulticastSocket clientSocket = createMulticastSocket(mContext);
                    byte[] receivedata = new byte[PACKET_MAX_SIZE];
                    while(true) {
                        multicastPacket = new DatagramPacket(receivedata, receivedata.length);
                        clientSocket.receive(multicastPacket);
                        recievedString = new String(trimPacket(multicastPacket.getData()));
                        if(DEBUG){
                            Log.d("UDP", " Received String: " + recievedString);
                            InetAddress ipaddress = multicastPacket.getAddress();
                            int port = multicastPacket.getPort();
                            Log.d("UDP", "IPAddress : " + ipaddress.toString());
                            Log.d("UDP", "Port : " + Integer.toString(port));
                        }
                    }
                } catch (SocketException e) {
                    Log.e("UDP", "Socket Error", e);
                } catch (IOException e) {
                    Log.e("UDP", "IO Error", e);
                }
            }
        }).start();
    }

    private MulticastSocket createMulticastSocket(Context context)
            throws UnknownHostException, SocketException, IOException {
        Utils.methodDebug(LOGTAG);

        MulticastSocket multicastSocket = new MulticastSocket(Constants.SOCKET_MULTICAST_PORT);
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

        if(wifiManager != null){
            WifiManager.MulticastLock lock = wifiManager.createMulticastLock(LOGTAG);
            lock.acquire();
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int intaddr = wifiInfo.getIpAddress();
        byte[] byteaddr = new byte[] {
                (byte) (intaddr & 0xff),
                (byte) (intaddr >> 8 & 0xff),
                (byte) (intaddr >> 16 & 0xff),
                (byte) (intaddr >> 24 & 0xff)};
        InetAddress addr = InetAddress.getByAddress(byteaddr);
        NetworkInterface netIf = NetworkInterface.getByInetAddress(addr);
        multicastSocket.joinGroup(new InetSocketAddress(InetAddress.getByName(Constants.SOCKET_MULTICAST_GROUP_IP), Constants.SOCKET_MULTICAST_PORT), netIf);
        multicastSocket.setTimeToLive(33);
        return multicastSocket;
    }

    private ServerSocket createServerSocket(Session session){
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(Constants.SOCKET_SERVER_PORT,0,InetAddress.getByName(session.getIp_address()));
            //serverSocket.bind(new InetSocketAddress(, ));
            if(BuildConfig.DEBUG){
                Log.d(LOGTAG, serverSocket.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serverSocket;
    }

    private String readPacket(InputStream inputStream){
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

    private byte[] trimPacket(byte[] packet){
        int count = 0;
        for(byte charr : packet){
            if(charr == 0){
                break;
            }
            ++count;
        }
        return Arrays.copyOf(packet, count);
    }
}