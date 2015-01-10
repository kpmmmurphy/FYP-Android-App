package ie.ucc.cs1.fyp.Socket;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.text.format.Formatter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import ie.ucc.cs1.fyp.Constants;
import ie.ucc.cs1.fyp.Model.APIManager;
import ie.ucc.cs1.fyp.Model.AlertManager;
import ie.ucc.cs1.fyp.Model.Config;
import ie.ucc.cs1.fyp.Model.Sensor;
import ie.ucc.cs1.fyp.Model.SystemDetailsManager;
import ie.ucc.cs1.fyp.Model.WifiDirectManager;
import ie.ucc.cs1.fyp.SensorManager;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class Session {

    private transient Boolean DEBUG = true;
    private transient static String LOGTAG = "__Session";
    private static Session __instance = null;

    private transient Config config;

    //Fields to be serialised
    protected String time_stamp;
    protected String ip_address;
    protected String device_id;
    protected transient boolean connectedToPi = false;

    private Session(Context context){
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ip_address = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        device_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        time_stamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

        new LoadConfigTask().execute();
    }

    public static Session getInstance(Context context){
        if (__instance  == null){
            __instance = new Session(context);
            Log.d(LOGTAG, new GsonBuilder().create().toJson(__instance));
        }
        return __instance;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getIp_address() {
        return ip_address;
    }

    public void setIp_address(String ip_address) {
        this.ip_address = ip_address;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public boolean isConnectedToPi() {
        return connectedToPi;
    }

    public void setConnectedToPi(boolean connected) {
        this.connectedToPi = connected;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    private class LoadConfigTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            config = new Gson().fromJson(Constants.CONFIG_DEFAULT, Config.class);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.e(LOGTAG, "sdrfvjn");
            for(Sensor sensor : config.getSensors()){
                Log.e(LOGTAG,sensor.getName());
            }
        }
    }

    private Config createDefaultConfig(){
        Gson gson = new Gson();
        ArrayList<Sensor> sensors = new ArrayList<Sensor>();
        sensors.add(gson.fromJson(Constants.CONFIG_MOTION, Sensor.class));
        sensors.add(gson.fromJson(Constants.CONFIG_THERMISTOR, Sensor.class));
        sensors.add(gson.fromJson(Constants.CONFIG_MQ7, Sensor.class));
        sensors.add(gson.fromJson(Constants.CONFIG_MQ2, Sensor.class));

        SystemDetailsManager systemDetailsManager = gson.fromJson(Constants.CONFIG_SYSTEM_DETAILS_MANAGER, SystemDetailsManager.class);
        APIManager apiManager = gson.fromJson(Constants.CONFIG_API_MANAGER, APIManager.class);
        WifiDirectManager wifiDirectManager = gson.fromJson(Constants.CONFIG_WIFI_DIRECT_MANAGER, WifiDirectManager.class);
        AlertManager alertManager = gson.fromJson(Constants.CONFIG_ALERT_MANAGER, AlertManager.class);
        SensorManager sensorManager = gson.fromJson(Constants.CONFIG_SENSOR_MANAGER, SensorManager.class);

        return null;
    }
}
