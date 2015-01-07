package ie.ucc.cs1.fyp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import ie.ucc.cs1.fyp.Model.SensorOutput;

/**
 * Created by kpmmmurphy on 30/10/14.
 */
public class Utils {

    private boolean DEBUG = true;

    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[4].getMethodName();
    }

    public static void methodDebug(String LOGTAG) {
        if (BuildConfig.DEBUG) {
            Log.d(LOGTAG, getMethodName());
        }
    }

    public static ArrayList<SensorOutput> randomSensorOutput() {
        ArrayList<SensorOutput> sensorOutputs = new ArrayList<SensorOutput>();

        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ7, Constants.SENSOR_MEASUREMENT_PPM, randomInRange(0, 1000)));
        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ2, Constants.SENSOR_MEASUREMENT_PPM, randomInRange(0, 1000)));
        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_SMOKE, Constants.SENSOR_MEASUREMENT_PPM, randomInRange(0, 1000)));
        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_THERMISTOR, Constants.SENSOR_MEASUREMENT_CELCIUS, randomInRange(0, 27)));

        return sensorOutputs;
    }

    public static String toJson(Object obj){
        return new GsonBuilder().create().toJson(obj);
    }

    public static Object fromJson(String json, Object clazz){
        return new Gson().fromJson(json, clazz.getClass());
    }



    public static int randomInRange(int min, int max) {
        return min + (int) (Math.random() * max);
    }

}