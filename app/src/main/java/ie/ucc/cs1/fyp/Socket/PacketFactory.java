package ie.ucc.cs1.fyp.Socket;

import android.util.Log;

import ie.ucc.cs1.fyp.BuildConfig;
import ie.ucc.cs1.fyp.Constants;
import ie.ucc.cs1.fyp.Model.Packet;
import ie.ucc.cs1.fyp.Model.Payload;
import ie.ucc.cs1.fyp.Utils;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class PacketFactory {
    private static final String LOGTAG = PacketFactory.class.getSimpleName();

    public static byte[] newConnectPacket(Session session){
        Payload payload = new Payload();
        payload.setSession(session);
        Packet packet = new Packet(Constants.SERVICE_CONNECT, payload);
        String json = Utils.toJson(packet);
        if(BuildConfig.DEBUG){
            Log.d(LOGTAG, json);
        }
        return json.getBytes();
    }
}
