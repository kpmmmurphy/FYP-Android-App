package ie.ucc.cs1.fyp.Socket;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.Settings.Secure;
import android.text.format.Formatter;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ie.ucc.cs1.fyp.Constants;
import ie.ucc.cs1.fyp.Model.Config;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class Session {

    private transient Boolean DEBUG = true;
    private transient static String LOGTAG = "__Session";
    private static Session __instance = null;

    private transient Config config;
    private transient InetAddress piIPAddress;

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

        //new LoadConfigTask().execute();
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

    public InetAddress getPiIPAddress() {
        return piIPAddress;
    }

    public void setPiIPAddress(InetAddress piIPAddress) {
        this.piIPAddress = piIPAddress;
    }

    private class LoadConfigTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            config = new Gson().fromJson(Constants.CONFIG_DEFAULT, Config.class);
            return null;
        }
    }
}
