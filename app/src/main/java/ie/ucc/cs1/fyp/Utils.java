package ie.ucc.cs1.fyp;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import ie.ucc.cs1.fyp.Model.SensorOutput;

/**
 * Created by kpmmmurphy on 30/10/14.
 */
public class Utils {

    public static String getMethodName(){
        return Thread.currentThread().getStackTrace()[4].getMethodName();
    }

    public static void methodDebug(String LOGTAG){
        if(BuildConfig.DEBUG){
            Log.d(LOGTAG, getMethodName());
        }
    }

    public static ArrayList<SensorOutput> randomSensorOutput(){
        ArrayList<SensorOutput> sensorOutputs = new ArrayList<SensorOutput>();
        Random random = new Random();

        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ7,   Constants.SENSOR_MEASUREMENT_PPM, random.nextInt()));
        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ2,   Constants.SENSOR_MEASUREMENT_PPM, random.nextInt()));
        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_SMOKE, Constants.SENSOR_MEASUREMENT_PPM, random.nextInt()));
        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_THERMISTOR, Constants.SENSOR_MEASUREMENT_CELCIUS, random.nextInt()));

        return sensorOutputs;
    }
}
