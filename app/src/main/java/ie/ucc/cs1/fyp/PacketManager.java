package ie.ucc.cs1.fyp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import ie.ucc.cs1.fyp.Model.Config;
import ie.ucc.cs1.fyp.Model.Packet;
import ie.ucc.cs1.fyp.Model.PiResponse;
import ie.ucc.cs1.fyp.Socket.Session;

/**
 * Created by kpmmmurphy on 11/01/15.
 */
public class PacketManager {

    private static String LOGTAG = "__PacketManager";
    private Context mContext;
    private static PacketManager __instance;
    private Session session;
    private static Gson gson;

    private PacketManager(Context context){
        gson = new Gson();
        this.mContext = context;
        session = Session.getInstance(mContext);
    }

    public static PacketManager getInstance(Context context){
        if(__instance == null){
            __instance = new PacketManager(context);
        }
        return __instance;
    }

    public void deliverPacket(String packet){
        if(BuildConfig.DEBUG){
            Log.d(LOGTAG, "Received Packet -> " + packet);
        }
        Packet currentPacket = gson.fromJson(packet, Packet.class);

        if (currentPacket.getService().equals(Constants.SERVICE_SENSOR_DATA)){
            SensorManager.getInstance().updateSensorOutputs(currentPacket.getPayload());
            if(BuildConfig.DEBUG){
                Log.d(LOGTAG,"Sensor Data");
            }
        }else if(currentPacket.getService().equals(Constants.SERVICE_CONFIG)){
            if(BuildConfig.DEBUG){
                Log.d(LOGTAG,"Config");
            }
            session.setConfig(gson.fromJson(currentPacket.getPayload(), Config.class));
        }else if(currentPacket.getService().equals(Constants.SERVICE_RESPONSE)){
            PiResponse piResponse = gson.fromJson(currentPacket.getPayload(), PiResponse.class);
            Toast.makeText(mContext, piResponse.getMsg_response(), Toast.LENGTH_SHORT);
        }
    }
}
