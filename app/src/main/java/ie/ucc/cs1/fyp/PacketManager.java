package ie.ucc.cs1.fyp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ie.ucc.cs1.fyp.Model.CurrentSensorValues;
import ie.ucc.cs1.fyp.Model.Packet;
import ie.ucc.cs1.fyp.Model.Session;

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

    public void deliverPacket(final Packet packet){
        if(packet != null){
            if (packet.getService().equals(Constants.SERVICE_SENSOR_DATA)){
                if(BuildConfig.DEBUG){
                    Log.d(LOGTAG,"Sensor Data");
                }
                CurrentSensorValues currentSensorValues = packet.getPayload().getCurrent_sensor_values();
                Log.e(LOGTAG, Utils.toJson(currentSensorValues));
                currentSensorValues.setData_and_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
                SensorValueManager.getInstance().setCurrentSensorValues(currentSensorValues);
            }else if(packet.getService().equals(Constants.SERVICE_CONFIG)){
                if(BuildConfig.DEBUG){
                    Log.d(LOGTAG,"Config");
                }
                session.setConfig(packet.getPayload().getConfig());
            }else if(packet.getService().equals(Constants.GRAPH_DATA_CUR_HOUR)) {
                if (BuildConfig.DEBUG) {
                    Log.d(LOGTAG, "GRAPH_DATA_CUR_HOUR");
                }
                session.getGraphData().put(Constants.GRAPH_DATA_CUR_HOUR, packet.getPayload().getSensor_list_current_hour());
            }else if(packet.getService().equals(Constants.GRAPH_DATA_CUR_DAY_AGG_HOUR)) {
                if (BuildConfig.DEBUG) {
                    Log.d(LOGTAG, "GRAPH_DATA_CUR_DAY_AGG_HOUR");
                }
                session.getGraphData().put(Constants.GRAPH_DATA_CUR_DAY_AGG_HOUR, packet.getPayload().getSensor_list_current_day_agg_hour());
            }else if(packet.getService().equals(Constants.GRAPH_DATA_AGG_DAY)) {
                if (BuildConfig.DEBUG) {
                    Log.d(LOGTAG, "GRAPH_DATA_AGG_DAY");
                }
                session.getGraphData().put(Constants.GRAPH_DATA_AGG_DAY, packet.getPayload().getSensor_list_agg_day());
            }else if(packet.getService().equals(Constants.SERVICE_REQUEST_STREAM)) {
                if (BuildConfig.DEBUG) {
                    Log.d(LOGTAG, "SERVICE_REQUEST_STREAM");
                }
                session.setStreamAddress(packet.getPayload().getStream_address());
            }else if(packet.getService().equals(Constants.JSON_VALUE_WIFI_DIRECT_SYSTEM_CONFIG_UPDATED)){
                if(BuildConfig.DEBUG){
                    Log.d(LOGTAG,"Response");
                }
                LocalBroadcastManager.getInstance(mContext).sendBroadcast( new Intent(Constants.INTENT_PI_CONFIG_UPDATED));

            }else if(packet.getService().equals(Constants.SERVICE_RESPONSE)){
                if(BuildConfig.DEBUG){
                    Log.d(LOGTAG,"Response");
                }
            }
        }
    }
}
