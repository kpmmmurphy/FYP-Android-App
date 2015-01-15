package ie.ucc.cs1.fyp;

import com.google.gson.Gson;

import java.util.ArrayList;

import ie.ucc.cs1.fyp.Model.CurrentSensorValues;
import ie.ucc.cs1.fyp.Model.CurrentSensorValuesFromServer;
import ie.ucc.cs1.fyp.Model.SensorOutput;
import ie.ucc.cs1.fyp.Network.API;

/**
 * Created by kpmmmurphy on 09/01/15.
 */
public class SensorValueManager {
    private static final String LOGTAG = API.class.getSimpleName();

    private static SensorValueManager __instance = null;
    private ArrayList<SensorOutput> sensorOutputs;
    private CurrentSensorValues currentSensorValues;
    private CurrentSensorValuesFromServer currentSensorValuesFromServer;

    private Gson gson;

    private SensorValueManager(){
        sensorOutputs = new ArrayList<SensorOutput>();
        gson = new Gson();
    }

    public static SensorValueManager getInstance(){
        if(__instance == null){
            __instance = new SensorValueManager();
        }
        return __instance;
    }

    public void setCurrentSensorValues(CurrentSensorValues currentSensorValues) {
        this.currentSensorValues = currentSensorValues;
    }

    public CurrentSensorValues getCurrentSensorValues() {
        return currentSensorValues;
    }

    public CurrentSensorValuesFromServer getCurrentSensorValuesFromServer() {
        return currentSensorValuesFromServer;
    }

    public void setCurrentSensorValuesFromServer(CurrentSensorValuesFromServer currentSensorValuesFromServer) {
        String data_and_time = currentSensorValuesFromServer.getDate_and_time();
        int temp      = Integer.valueOf(currentSensorValuesFromServer.getTemperature());
        int motion    = Integer.valueOf(currentSensorValuesFromServer.getMotion());
        int co        = Integer.valueOf(currentSensorValuesFromServer.getCarbon_monoxide());
        int flamm_gas = Integer.valueOf(currentSensorValuesFromServer.getFlammable_gas());
        CurrentSensorValues values = new CurrentSensorValues(data_and_time, temp, co, motion, flamm_gas);
        this.setCurrentSensorValues(values);
    }

    public ArrayList<SensorOutput> getCurrentSensorOutputsList() {
        ArrayList<SensorOutput> sensorOutputs = new ArrayList<SensorOutput>();
        if(currentSensorValues != null){
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ7, Constants.SENSOR_MEASUREMENT_PPM, currentSensorValues.getCarbon_monoxide()));
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ2, Constants.SENSOR_MEASUREMENT_PPM, currentSensorValues.getFlammable_gas()));
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MOTION, "", currentSensorValues.getMotion()));
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_THERMISTOR, Constants.SENSOR_MEASUREMENT_CELCIUS, currentSensorValues.getTemperature()));
        }else{
            sensorOutputs = Utils.randomSensorOutput();
        }

        return sensorOutputs;
    }

}
