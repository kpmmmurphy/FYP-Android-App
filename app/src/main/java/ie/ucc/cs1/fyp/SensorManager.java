package ie.ucc.cs1.fyp;

import com.google.gson.Gson;

import java.util.ArrayList;

import ie.ucc.cs1.fyp.Model.CurrentSensorOutputs;
import ie.ucc.cs1.fyp.Model.SensorOutput;

/**
 * Created by kpmmmurphy on 09/01/15.
 */
public class SensorManager {

    private static SensorManager __instance = null;
    private ArrayList<SensorOutput> sensorOutputs;
    private CurrentSensorOutputs currentSensorOutputs;

    private Gson gson;

    private SensorManager(){
        sensorOutputs = new ArrayList<SensorOutput>();
        gson = new Gson();
    }

    public static SensorManager getInstance(){
        if(__instance == null){
            __instance = new SensorManager();
        }
        return __instance;
    }

    public void updateSensorOutputs(String sensorOutputs){
         currentSensorOutputs = gson.fromJson(sensorOutputs, CurrentSensorOutputs.class);
    }

    public CurrentSensorOutputs getCurrentSensorOutputs() {
        return currentSensorOutputs;
    }

    public ArrayList<SensorOutput> getCurrentSensorOutputsList() {
        ArrayList<SensorOutput> sensorOutputs = new ArrayList<SensorOutput>();
        if(currentSensorOutputs != null){
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ7, Constants.SENSOR_MEASUREMENT_PPM, currentSensorOutputs.getCarbon_monoxide()));
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ2, Constants.SENSOR_MEASUREMENT_PPM, currentSensorOutputs.getFlammable_gas()));
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_SMOKE, Constants.SENSOR_MEASUREMENT_PPM, currentSensorOutputs.getMotion()));
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_THERMISTOR, Constants.SENSOR_MEASUREMENT_CELCIUS, currentSensorOutputs.getTemperature()));
        }else{
            sensorOutputs = Utils.randomSensorOutput();
        }

        return sensorOutputs;
    }
}
