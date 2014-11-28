package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * Server which sends packets to the Multicast group on 235.1.1.1:10000
 *
 * Created by Kevin Murphy on 07/11/14.
 */
public class MulticastServer {

    private static final boolean DEBUG = true;

    private static final int    CHANNEL_PORT      = 12000;
    private static final String MULTICAST_ADDRESS = "234.1.1.1";
    private static final int    BUFFER_SIZE       = 1024;

    private static byte[] buffer = new byte[BUFFER_SIZE];

    private static MulticastSocket socket;
    private static DatagramPacket packet;

    public static void main(String... args){
        try {
            socket = new MulticastSocket(CHANNEL_PORT);
        }catch (IOException ex){
            ex.printStackTrace();
        }

        if (socket != null) {
            try {
                packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(MULTICAST_ADDRESS), CHANNEL_PORT);
            }catch (UnknownHostException ex){
                ex.printStackTrace();
            }
            if(packet != null){
                System.out.println("Sending " + buffer.length + " Bytes to " + socket.getInetAddress());
                while (true){
                    try{
                        socket.send(packet);
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }
                    try{
                        Thread.sleep(1);
                    }catch (InterruptedException ex){
                        ex.printStackTrace();
                    }
                }
            }
        }
    }
}
