package ie.ucc.cs1.fyp.Network;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import ie.ucc.cs1.fyp.Constants;
import ie.ucc.cs1.fyp.Model.Config;
import ie.ucc.cs1.fyp.Model.CurrentSensorValuesFromServer;
import ie.ucc.cs1.fyp.Utils;

/**
 * Retrofit API interface, for talking to the Pi
 *
 * Created by kpmmmurphy on 24/10/14.
 */
public class API {
    private static final String LOGTAG = API.class.getSimpleName();
    private static final String URL = "http://cs1.ucc.ie/~kpm2/fyp/api/api_manager.php";

    private static API __instance;
    private RequestQueue queue;
    private ImageLoader imageLoader;

    private API(Context context){
        Utils.methodDebug(LOGTAG);
        queue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(1000);
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public static synchronized API getInstance(Context context){
        Utils.methodDebug(LOGTAG);
        if (__instance == null){
            __instance = new API(context);
        }
        return __instance;
    }

    //--REQUESTS
     public void requestSensorValues(Response.Listener<CurrentSensorValuesFromServer> listener, Response.ErrorListener errorListener){
        Utils.methodDebug(LOGTAG);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Constants.API_REQUEST_HEADER_SERVICE, Constants.API_REQUEST_SERVICE_GET_SENSOR_VALUES);

        GsonRequest<CurrentSensorValuesFromServer> sensorValueRequest = new GsonRequest<CurrentSensorValuesFromServer>(URL, CurrentSensorValuesFromServer.class, headers, listener, errorListener);
        addToQueue(sensorValueRequest);
    }

    public void requestSystemConfig(Response.Listener<Config> listener, Response.ErrorListener errorListener){
        Utils.methodDebug(LOGTAG);

        Map<String, String> headers = new HashMap<String, String>();
        headers.put(Constants.API_REQUEST_HEADER_SERVICE, Constants.API_REQUEST_SERVICE_GET_CONFIG);

        GsonRequest<Config> configGsonRequest = new GsonRequest<Config>(URL, Config.class, headers, listener, errorListener);
        addToQueue(configGsonRequest);
    }

    //Must updated API manager to give back formatted responses.
    //public void uploadSystemConfig(Response.Listener<>)

    public void addToQueue(Request req){
        req.setTag(Constants.LOGTAG);
        getQueue().add(req);
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public void setQueue(RequestQueue queue) {
        this.queue = queue;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }
}
