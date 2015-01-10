package ie.ucc.cs1.fyp;

import android.app.Application;
import android.content.Context;

/**
 * Created by kpmmmurphy on 28/11/14.
 */
public class MyApplication extends Application {

    private static String LOGTAG = "__Application";
    private static Application mInstance;
    @Override
    public void onCreate(){
        super.onCreate();
        Utils.methodDebug(LOGTAG);
        mInstance = this;
    }

    public static Context getContext(){
        return  mInstance.getApplicationContext();
    }
}
