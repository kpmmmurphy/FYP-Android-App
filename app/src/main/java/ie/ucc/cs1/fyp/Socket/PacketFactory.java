package ie.ucc.cs1.fyp.Socket;

import android.util.Log;

import ie.ucc.cs1.fyp.BuildConfig;
import ie.ucc.cs1.fyp.Constants;
import ie.ucc.cs1.fyp.Model.ConnectPacket;
import ie.ucc.cs1.fyp.Utils;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class PacketFactory {

    public static byte[] newConnectPacket(Session session){
        ConnectPacket packet = new ConnectPacket(session);
        String json = Utils.toJson(packet);
        if(BuildConfig.DEBUG){
            Log.d(Constants.LOGTAG, json);
        }
        return json.getBytes();
    }
}
