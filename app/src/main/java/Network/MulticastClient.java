package Network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Client who joins the Multicast group on 235.1.1.1:10000
 *
 * Created by Kevin Murphy on 06/11/14.
 */
public class MulticastClient {

    private static final boolean DEBUG = true;

    private static final int CHANNEL_PORT = 12000;
    private static final String MULTICAST_ADDRESS = "234.1.1.1";

    private static final int BUFFER_SIZE = 1024;
    private static byte[] buffer = new byte[1024];

    private static DatagramPacket  packet;
    private static MulticastSocket socket;

    public static void main(String... args){
        packet = new DatagramPacket(buffer, buffer.length);

        try {
            socket = new MulticastSocket(CHANNEL_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (socket != null) {
            try {
                socket.joinGroup(InetAddress.getByName(MULTICAST_ADDRESS));
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true){
                try {
                    socket.receive(packet);
                    if(DEBUG){
                        System.out.println("Received Packet of length:: " + packet.getLength() + " From " + packet.getAddress());
                        System.out.println("Recieved Data:: " + packet.toString().trim());
                    }
                    //Reset length to stop truncation
                    packet.setLength(BUFFER_SIZE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
