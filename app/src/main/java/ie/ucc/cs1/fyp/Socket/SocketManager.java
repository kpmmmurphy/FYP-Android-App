package ie.ucc.cs1.fyp.Socket;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import ie.ucc.cs1.fyp.Constants;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class SocketManager {

    private static String LOGTAG = "__SocketManager";
    private Context mContext;

    private MulticastSocket clientSocket;
    private int PACKET_MAX_SIZE = 1024;

    public SocketManager(Context context){
        this.mContext = context;
        new Thread(new Runnable() {
            public void run() {
                try {
                    //clientSocket= new DatagramSocket(Constants.SOCKET_MULTICAST_PORT);
                    clientSocket = createMulticastSocket(mContext);
                    byte[] receivedata = new byte[PACKET_MAX_SIZE];
                    while(true) {
                        DatagramPacket recv_packet = new DatagramPacket(receivedata, receivedata.length);
                        Log.d("UDP", "S: Receiving...");
                        clientSocket.receive(recv_packet);
                        String receivedstring = new String(recv_packet.getData());
                        Log.d("UDP", " Received String: " + receivedstring);
                        InetAddress ipaddress = recv_packet.getAddress();
                        int port = recv_packet.getPort();
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

    public static MulticastSocket createMulticastSocket(Context context)
            throws UnknownHostException, SocketException, IOException {
        MulticastSocket multicastSocket = new MulticastSocket(Constants.SOCKET_MULTICAST_PORT);
        Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
        NetworkInterface eth0 = null;
        while (enumeration.hasMoreElements()) {
            eth0 = enumeration.nextElement();
            Log.e(LOGTAG, eth0.getName());
        }

        /*if (connectedToEthernet(context)) {

            if (netIf != null)
                multicastSocket.setNetworkInterface(netIf);
        }*/
        //else if (isWirelessDirect(context)) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if(wifiManager != null){
            WifiManager.MulticastLock lock = wifiManager.createMulticastLock("Log_Tag");
            lock.acquire();
            Log.e(LOGTAG, "Lock Aquired");
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
        Log.e(LOGTAG, netIf.toString());

            //multicastSocket.setNetworkInterface(netIf);
            //multicastSocket.joinGroup(InetAddress.getByName(Constants.SOCKET_MULTICAST_GROUP_IP));
            multicastSocket.joinGroup(new InetSocketAddress(InetAddress.getByName(Constants.SOCKET_MULTICAST_GROUP_IP), Constants.SOCKET_MULTICAST_PORT), netIf);
        Log.e(LOGTAG, "Creating Multicast Socket");
        //}
        multicastSocket.setTimeToLive(33);
        return multicastSocket;
    }

    /*public static boolean isWirelessDirect(Context context) {
        ConnectivityManager connManager =



    }*/
}