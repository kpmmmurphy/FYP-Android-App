package ie.ucc.cs1.fyp.Model;

import com.google.gson.annotations.SerializedName;

import ie.ucc.cs1.fyp.Constants;
import ie.ucc.cs1.fyp.Socket.Session;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class ConnectPacket extends Packet {
    @SerializedName("payload")
    protected Session session;

    public ConnectPacket(Session session){
        this.service = Constants.SERVICE_CONNECT;
        this.session = session;
    }
}
