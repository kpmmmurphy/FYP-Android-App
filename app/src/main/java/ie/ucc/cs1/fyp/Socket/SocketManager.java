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
import ie.ucc.cs1.fyp.SensorManager;
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
    private Thread sensorValueThread;
    private Socket       sensorValuesSocket;
    private DatagramPacket  multicastPacket;
    private String recievedString;

    private BufferedReader bufferedReader;
    private InputStreamReader inputStreamReader;
    private int PACKET_MAX_SIZE = 1024;

    public SocketManager(Context context){
        this.mContext = context;
    }


    private class ConnectToPi extends AsyncTask<Session, Void, Boolean>{

        private String LOGTAG = "__ConnectToPi";

        @Override
        protected Boolean doInBackground(Session... sessions) {
            Utils.methodDebug(LOGTAG);
            Session session = sessions[0];
            serverSocket    = createServerSocket(session);
            try {
                byte[] connectPacket = PacketFactory.newConnectPacket(session);

                multicastSocket = createMulticastSocket(mContext);
                multicastGroup  = InetAddress.getByName(Constants.SOCKET_MULTICAST_GROUP_IP);
                multicastPacket = new DatagramPacket(connectPacket, connectPacket.length, new InetSocketAddress(multicastGroup, Constants.SOCKET_MULTICAST_PORT));
                multicastSocket.setTimeToLive(30);
                multicastSocket.send(multicastPacket);

                if (BuildConfig.DEBUG){
                    Log.d(LOGTAG, "Sending Multicast Packet");
                }

                //Now wait for ACK
                Socket ackSocket = serverSocket.accept();
                if (BuildConfig.DEBUG){
                    Log.d(LOGTAG, "Created ACK Socket :: " + ackSocket.toString());
                }

                String receivedString = readPacket(ackSocket.getInputStream());
                if (BuildConfig.DEBUG) {
                    Log.d(LOGTAG, "Received Packet :: " + receivedString);
                }

                PiResponse piResponse = new Gson().fromJson(receivedString, PiResponse.class);
                if(piResponse.getService().equals(Constants.SERVICE_PAIRED) && piResponse.getStatus_code() == Constants.CONNECT_SUCCESS){
                    //Set session to connected
                    if(BuildConfig.DEBUG){
                        Log.d(LOGTAG, "Connected to Pi");
                    }
                    session.setConnectedToPi(true);

                }

            } catch (SocketException e) {
                Log.e("UDP", "Socket Error", e);
            } catch (IOException e) {
                Log.e("UDP", "IO Error", e);
            }finally {
                multicastSocket.close();

            }
            return session.isConnectedToPi();
        }

        @Override
        protected void onPostExecute(Boolean connected) {
            Utils.methodDebug(LOGTAG);
            //Send broadcast to Main Activity
            if(connected){
                if(BuildConfig.DEBUG){
                    Log.d(LOGTAG, "Sending Broadcast to MainActivity");
                }
                sendConnectedBroadcast();
            }else{
                //Send not connected
            }
        }
    }

    public void startConnectionToPi(Session session){
        new ConnectToPi().execute(session);
    }

    public void startSensorValueThread(){
        sensorValueThread = new Thread(){
            @Override
            public void run() {
                Log.e(LOGTAG, "Threading!!!");
                try {
                    while (!this.isInterrupted()){
                        sensorValuesSocket = serverSocket.accept();
                        String currentSensorValues = readPacket(sensorValuesSocket.getInputStream());
                        if(BuildConfig.DEBUG){
                            Log.d(LOGTAG, currentSensorValues);
                        }
                        SensorManager.getInstance().updateSensorOutputs(currentSensorValues);
                    }
                    sensorValuesSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        sensorValueThread.start();
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

    private void sendConnectedBroadcast(){
        if(BuildConfig.DEBUG){
            Log.d(LOGTAG, "Sending Connected to Pi Intent");
        }
        Intent connectedIntent = new Intent(Constants.INTENT_CONNECTED_TO_PI);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(connectedIntent);
    }
}