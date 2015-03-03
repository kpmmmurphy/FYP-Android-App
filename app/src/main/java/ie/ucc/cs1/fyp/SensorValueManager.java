package ie.ucc.cs1.fyp;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.ArrayList;

import ie.ucc.cs1.fyp.Model.CurrentSensorValues;
import ie.ucc.cs1.fyp.Model.CurrentSensorValuesFromServer;
import ie.ucc.cs1.fyp.Model.PeripheralSensorValues;
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

        String data_and_time = "";
        if(!TextUtils.isEmpty(currentSensorValuesFromServer.getDate_and_time())){
            data_and_time = currentSensorValuesFromServer.getDate_and_time();
        }

        int temp = 0;
        if(!TextUtils.isEmpty(currentSensorValuesFromServer.getTemperature())){
            temp = Integer.valueOf(currentSensorValuesFromServer.getTemperature());
        }

        int max_temp = 0;
        if(!TextUtils.isEmpty(currentSensorValuesFromServer.getMax_temperature())){
            max_temp  = Integer.valueOf(currentSensorValuesFromServer.getMax_temperature());
        }

        int min_temp = 0;
        if(!TextUtils.isEmpty(currentSensorValuesFromServer.getMin_temperature())) {
            min_temp = Integer.valueOf(currentSensorValuesFromServer.getMin_temperature());
        }

        int motion = 0;
        if(!TextUtils.isEmpty(currentSensorValuesFromServer.getMotion())){
            motion = Integer.valueOf(currentSensorValuesFromServer.getMotion());
        }

        int motion_percentage = 0;
        if(!TextUtils.isEmpty(currentSensorValuesFromServer.getPercentage_motion())){
            motion_percentage = Float.valueOf(currentSensorValuesFromServer.getPercentage_motion()).intValue();
        }

        int co = 0;
        if(!TextUtils.isEmpty(currentSensorValuesFromServer.getCarbon_monoxide())){
            co = Integer.valueOf(currentSensorValuesFromServer.getCarbon_monoxide());
        }

        int max_co = 0;
        if(!TextUtils.isEmpty(currentSensorValuesFromServer.getMax_carbon_monoxide())){
            max_co  = Integer.valueOf(currentSensorValuesFromServer.getMax_carbon_monoxide());
        }
        int min_co = 0;
        if(!TextUtils.isEmpty(currentSensorValuesFromServer.getMin_carbon_monoxide())) {
            min_co = Integer.valueOf(currentSensorValuesFromServer.getMin_carbon_monoxide());
        }

        int flamm_gas = 0;
        if (!TextUtils.isEmpty(currentSensorValuesFromServer.getFlammable_gas())){
            flamm_gas = Integer.valueOf(currentSensorValuesFromServer.getFlammable_gas());
        }

        int max_flam = 0;
        if(!TextUtils.isEmpty(currentSensorValuesFromServer.getMax_flammable_gas())) {
            max_flam = Integer.valueOf(currentSensorValuesFromServer.getMax_flammable_gas());
        }
        int min_flam = 0;
        if(!TextUtils.isEmpty(currentSensorValuesFromServer.getMin_flammable_gas())) {
            min_flam = Integer.valueOf(currentSensorValuesFromServer.getMin_flammable_gas());
        }

        //Just create an empty array list
        ArrayList<PeripheralSensorValues> peripheralSensorValues = new ArrayList<PeripheralSensorValues>();

        CurrentSensorValues values = new CurrentSensorValues(data_and_time, temp, co, motion, flamm_gas, min_temp, max_temp, min_co, max_co, min_flam, max_flam, motion_percentage, peripheralSensorValues);
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

    public ArrayList<SensorOutput> getCurrentPeripheralSensorOutputsList(long peripheralDeviceID){
        ArrayList<SensorOutput> sensorOutputs = new ArrayList<SensorOutput>();
        for (PeripheralSensorValues peripheralSensorValues : currentSensorValues.getPeripheral_sensor_values()){
            if(peripheralSensorValues.getDevice_id() == peripheralDeviceID){
                sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_THERMISTOR, Constants.SENSOR_MEASUREMENT_CELCIUS, peripheralSensorValues.getTemperature(), null, null));
                sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_LIGHT, null, peripheralSensorValues.getLight(), null, null));
            }else{
                sensorOutputs = Utils.randomSensorOutput();
            }
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

            if(value >= 10)
                feedbackID = R.string.sensor_output_mq2_10;

            if(value >= 20)
                feedbackID = R.string.sensor_output_mq2_20;

            if(value >= 50)
                feedbackID = R.string.sensor_output_mq2_50;

        }else if(sensor.equalsIgnoreCase(Constants.SENSOR_NAME_MOTION.replace("_", " "))){

            if(value == 0)
                feedbackID = R.string.sensor_output_motion_0;

            if(value == 1)
                feedbackID = R.string.sensor_output_motion_1;

        }else if(sensor.equalsIgnoreCase(Constants.SENSOR_NAME_THERMISTOR.replace("_", " "))){

            if(value < 0)
                feedbackID = R.string.sensor_output_thermistor_sub_zero;

            if(value == 0)
                feedbackID = R.string.sensor_output_thermistor_0;

            if(value >= 10)
                feedbackID = R.string.sensor_output_thermistor_10;

            if(value >= 20)
                feedbackID = R.string.sensor_output_thermistor_20;

            if(value >= 30)
                feedbackID = R.string.sensor_output_thermistor_30;

            if(value >= 40)
                feedbackID = R.string.sensor_output_thermistor_40;

            if(value >= 50)
                feedbackID = R.string.sensor_output_thermistor_50;

        }

        return context.getString(feedbackID);
    }

}
