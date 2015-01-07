package ie.ucc.cs1.fyp.Socket;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.provider.Settings.Secure;
import android.text.format.Formatter;
import android.util.Log;

import com.google.gson.GsonBuilder;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class Session {

    private volatile Boolean DEBUG = true;
    private static String LOGTAG = "__Session";
    private static Session __instance = null;

    //Fields to be serialised
    protected String ip_address;
    protected String device_id;
    protected volatile boolean connected = false;

    private Session(Context context){
        WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        ip_address = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        device_id = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    public static Session getInstance(Context context){
        if (__instance  == null){
            __instance = new Session(context);
            Log.d(LOGTAG, new GsonBuilder().create().toJson(__instance));
        }
        return __instance;
    }


}
