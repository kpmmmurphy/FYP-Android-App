package ie.ucc.cs1.fyp;

import android.util.Log;

import java.util.ArrayList;

import ie.ucc.cs1.fyp.Model.SensorOutput;

/**
 * Created by kpmmmurphy on 30/10/14.
 */
public class Utils {

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

    public static int randomInRange(int min, int max) {
        return min + (int) (Math.random() * max);
    }

}