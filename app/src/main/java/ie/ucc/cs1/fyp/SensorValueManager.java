package ie.ucc.cs1.fyp;

import android.content.Context;

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
        int max_temp = 0;
        if(currentSensorValuesFromServer.getMax_temperature() != null){
            max_temp  = Integer.valueOf(currentSensorValuesFromServer.getMax_temperature());
        }
        int min_temp = 0;
        if(currentSensorValuesFromServer.getMin_temperature() != null) {
            min_temp = Integer.valueOf(currentSensorValuesFromServer.getMin_temperature());
        }
        int motion            = Integer.valueOf(currentSensorValuesFromServer.getMotion());
        int motion_percentage = 0;
        if(currentSensorValuesFromServer.getPercentage_motion() != null){
            motion_percentage = Float.valueOf(currentSensorValuesFromServer.getPercentage_motion()).intValue();
        }

        int co        = Integer.valueOf(currentSensorValuesFromServer.getCarbon_monoxide());
        int max_co = 0;
        if(currentSensorValuesFromServer.getMax_carbon_monoxide() != null){
            max_co  = Integer.valueOf(currentSensorValuesFromServer.getMax_carbon_monoxide());
        }
        int min_co = 0;
        if(currentSensorValuesFromServer.getMin_carbon_monoxide() != null) {
            min_co = Integer.valueOf(currentSensorValuesFromServer.getMin_carbon_monoxide());
        }

        int flamm_gas = Integer.valueOf(currentSensorValuesFromServer.getFlammable_gas());
        int max_flam = 0;
        if(currentSensorValuesFromServer.getMax_flammable_gas() != null) {
            max_flam = Integer.valueOf(currentSensorValuesFromServer.getMax_flammable_gas());
        }
        int min_flam = 0;
        if(currentSensorValuesFromServer.getMin_flammable_gas() != null) {
            min_flam = Integer.valueOf(currentSensorValuesFromServer.getMin_flammable_gas());
        }

        CurrentSensorValues values = new CurrentSensorValues(data_and_time, temp, co, motion, flamm_gas, min_temp, max_temp, min_co, max_co, min_flam, max_flam, motion_percentage);
        this.setCurrentSensorValues(values);
    }

    public ArrayList<SensorOutput> getCurrentSensorOutputsList() {
        ArrayList<SensorOutput> sensorOutputs = new ArrayList<SensorOutput>();
        if(currentSensorValues != null){
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ7, Constants.SENSOR_MEASUREMENT_PPM, currentSensorValues.getCarbon_monoxide(), currentSensorValues.getMax_carbon_monoxide(), currentSensorValues.getMin_carbon_monoxide()));
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ2, Constants.SENSOR_MEASUREMENT_PPM, currentSensorValues.getFlammable_gas(), currentSensorValues.getMax_flammable_gas(), currentSensorValues.getMin_flammable_gas()));
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MOTION, "", currentSensorValues.getMotion(), currentSensorValues.getPrecentage_motion(), null));
            sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_THERMISTOR, Constants.SENSOR_MEASUREMENT_CELCIUS, currentSensorValues.getTemperature(), currentSensorValues.getMax_temperature(), currentSensorValues.getMin_temperature()));
        }else{
            sensorOutputs = Utils.randomSensorOutput();
        }

        return sensorOutputs;
    }

    public static String getUserFeedback(String sensor, int value, Context context){
        int feedbackID = R.string.sensor_output_normal;
        if(sensor.equalsIgnoreCase(Constants.SENSOR_NAME_MQ7.replace("_", " "))){

            if(value >= 25)
                feedbackID = R.string.sensor_output_mq7_25;

            if(value >= 35)
                feedbackID = R.string.sensor_output_mq7_35;

            if(value >= 100)
                feedbackID = R.string.sensor_output_mq7_100;

            if(value >= 200)
                feedbackID = R.string.sensor_output_mq7_200;

            if(value >= 400)
                feedbackID = R.string.sensor_output_mq7_400;

            if(value >= 800)
                feedbackID = R.string.sensor_output_mq7_800;

            if(value >= 1600)
                feedbackID = R.string.sensor_output_mq7_1600;

            if(value >= 3200)
                feedbackID = R.string.sensor_output_mq7_3200;

            if(value >= 6400)
                feedbackID = R.string.sensor_output_mq7_6400;

            if(value >= 12800)
                feedbackID = R.string.sensor_output_mq7_12800;


        }else if(sensor.equalsIgnoreCase(Constants.SENSOR_NAME_MQ2.replace("_", " "))){

        }else if(sensor.equalsIgnoreCase(Constants.SENSOR_NAME_MOTION.replace("_", " "))){

        }else if(sensor.equalsIgnoreCase(Constants.SENSOR_NAME_THERMISTOR.replace("_", " "))){

        }

        return context.getString(feedbackID);
    }

}
