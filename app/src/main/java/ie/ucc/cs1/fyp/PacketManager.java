package ie.ucc.cs1.fyp;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ie.ucc.cs1.fyp.Model.CurrentSensorValues;
import ie.ucc.cs1.fyp.Model.Packet;
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

    public void deliverPacket(Packet packet){
        if(BuildConfig.DEBUG){
            Log.d(LOGTAG, "Received Packet -> " + packet);
        }
        if(packet != null){
            if (packet.getService().equals(Constants.SERVICE_SENSOR_DATA)){
                CurrentSensorValues currentSensorOutputs = packet.getPayload().getCurrentSensorOutputs();
                currentSensorOutputs.setData_and_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                SensorValueManager.getInstance().setCurrentSensorValues(currentSensorOutputs);
                if(BuildConfig.DEBUG){
                    Log.d(LOGTAG,"Sensor Data");
                }
            }else if(packet.getService().equals(Constants.SERVICE_CONFIG)){
                if(BuildConfig.DEBUG){
                    Log.d(LOGTAG,"Config");
                }
                session.setConfig(packet.getPayload().getConfig());
            }else if(packet.getService().equals(Constants.SERVICE_RESPONSE)){
                if(BuildConfig.DEBUG){
                    Log.d(LOGTAG,"Response");
                }
                //PiResponse piResponse = gson.fromJson(currentPacket.getPayload(), PiResponse.class);
                //Toast.makeText(mContext, piResponse.getMsg_response(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
