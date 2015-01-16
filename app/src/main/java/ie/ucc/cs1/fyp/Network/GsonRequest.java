package ie.ucc.cs1.fyp.Network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import ie.ucc.cs1.fyp.BuildConfig;
import ie.ucc.cs1.fyp.Model.CurrentSensorValuesFromServer;
import ie.ucc.cs1.fyp.Utils;

/**
 * Created by kpmmmurphy on 12/01/15.
 */
public class GsonRequest<T> extends JsonRequest<T> {
    private static final String LOGTAG = GsonRequest.class.getSimpleName();

    private Gson gson = new Gson();
    private final Class<T> clazz;
    private final String body;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;

    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers, Response.Listener<T> listener, Response.ErrorListener errorListener ) {
        this(url, clazz ,headers, null, listener, errorListener);
    }

    public GsonRequest(String url, Class<T> clazz, Map<String, String> headers, String jsonRequest, Response.Listener<T> listener, Response.ErrorListener errorListener ) {
        super(Method.POST, url, (jsonRequest == null) ? null : jsonRequest , listener ,errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.body = jsonRequest;
    }

    @Override
    public byte[] getBody() {
        return body != null ? body.getBytes() : super.getBody();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        Utils.methodDebug(LOGTAG);
        try{
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            //For some reason, my API PHP manager is producing responses encapsulated with '[]'
            //Just remove the first and last chars should do the trick
            if (clazz.equals(CurrentSensorValuesFromServer.class)){
                json = json.substring(1 , json.length() - 1);
            }

            if(BuildConfig.DEBUG){
                Log.d(LOGTAG, "Response :: " + json);
            }
            //Do not set the cache...
            return Response.success(gson.fromJson(json, clazz), null);
        }catch (UnsupportedEncodingException ex){
            return Response.error(new ParseError(ex));
        }catch (JsonSyntaxException ex){
            return Response.error(new ParseError(ex));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }
}
