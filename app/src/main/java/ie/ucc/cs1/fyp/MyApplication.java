package ie.ucc.cs1.fyp;

import android.app.Application;
import android.content.Context;

import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by kpmmmurphy on 28/11/14.
 */
public class MyApplication extends Application {

    private static String LOGTAG = "__Application";
    private static Application mInstance;
    //One Scheduler to rule them all
    private static ScheduledExecutorService scheduler;
    private static HashMap<String, ScheduledFuture> currentTasks;

    @Override
    public void onCreate(){
        super.onCreate();
        Utils.methodDebug(LOGTAG);
        mInstance = this;
        scheduler = Executors.newSingleThreadScheduledExecutor();
        currentTasks = new HashMap<String, ScheduledFuture>();
    }

    public static Context getContext(){
        return  mInstance.getApplicationContext();
    }

    public static void scheduleTask(Runnable task, long initDelay, long interval, String tag){
        currentTasks.put(tag, scheduler.scheduleAtFixedRate(task, initDelay, interval, TimeUnit.SECONDS));
    }

    public static void unscheduleTask(String tag){
        currentTasks.get(tag).cancel(true);
    }
}
