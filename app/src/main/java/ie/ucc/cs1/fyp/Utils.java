package ie.ucc.cs1.fyp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import ie.ucc.cs1.fyp.Model.SensorOutput;
import ie.ucc.cs1.fyp.PushNotifcation.GcmIntentService;

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

        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ7, Constants.SENSOR_MEASUREMENT_PPM, randomInRange(0, 1000), 0, 0));
        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MQ2, Constants.SENSOR_MEASUREMENT_PPM, randomInRange(0, 1000), 0, 0));
        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_MOTION, "", randomInRange(0, 1), 0, 0));
        sensorOutputs.add(new SensorOutput(Constants.SENSOR_NAME_THERMISTOR, Constants.SENSOR_MEASUREMENT_CELCIUS, randomInRange(0, 27), 0, 0));

        return sensorOutputs;
    }

    public static String toJson(Object obj){
        return new GsonBuilder().create().toJson(obj);
    }

    public static int randomInRange(int min, int max) {
        return min + (int) (Math.random() * max);
    }

    public static void createDialog(Context context, String sensorName, String sensorValue){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sensor_output);

        sensorName = sensorName.replace("_", " ");
        int sensorVal   = Integer.valueOf(sensorValue);

        ((TextView)dialog.findViewById(R.id.dialog_tv_title)).setText(sensorName);
        ((TextView)dialog.findViewById(R.id.dialog_tv_sensor_output_level)).setText(sensorValue);
        ((TextView)dialog.findViewById(R.id.dialog_tv_sensor_output_suggestion)).setText(SensorValueManager.getUserFeedback(sensorName, sensorVal, context ));

        int sensorDetailsID = 0;
        if(sensorName.equalsIgnoreCase(Constants.SENSOR_NAME_MQ7.replace("_", " "))){
            sensorDetailsID = R.string.sensor_description_mq7;
            ((TextView)dialog.findViewById(R.id.dialog_tv_sensor_measurement)).setText(Constants.SENSOR_MEASUREMENT_PPM);
        }else if(sensorName.equalsIgnoreCase(Constants.SENSOR_NAME_MQ2.replace("_", " "))){
            sensorDetailsID = R.string.sensor_description_mq2;
            ((TextView)dialog.findViewById(R.id.dialog_tv_sensor_measurement)).setText(Constants.SENSOR_MEASUREMENT_PPM);
        }else if(sensorName.equalsIgnoreCase(Constants.SENSOR_NAME_MOTION)){
            sensorDetailsID = R.string.sensor_description_motion;
        }else if (sensorName.equalsIgnoreCase(Constants.SENSOR_NAME_THERMISTOR)){
            sensorDetailsID = R.string.sensor_description_thermistor;
            ((TextView)dialog.findViewById(R.id.dialog_tv_sensor_measurement)).setText(Constants.SENSOR_MEASUREMENT_CELCIUS);
        }else if(sensorName.equalsIgnoreCase(Constants.SENSOR_NAME_LIGHT)){
            sensorDetailsID = R.string.sensor_description_light;
            ((TextView)dialog.findViewById(R.id.dialog_tv_sensor_measurement)).setText(Constants.SENSOR_MEASUREMENT_LUX);
        }

        if(sensorDetailsID != 0){
            ((TextView)dialog.findViewById(R.id.dialog_tv_sensor_details)).setText(context.getString(sensorDetailsID));
        }

        dialog.show();
    }

    public static String constructMsg(Context context, Bundle extras, String sensor, String sensorValue){
        if(sensor.isEmpty()){
            sensor = extras.getString(Constants.SENSOR_NAME);
        }
        if(sensorValue.isEmpty()){
            sensorValue = extras.getString(Constants.SENSOR_VALUE);
        }

        String msgText = "Alert Threshold Reached";
        if(sensor.equalsIgnoreCase(Constants.SENSOR_CAMERA)){
            msgText = "Image Captured!";
        }else if(sensor.equalsIgnoreCase(Constants.SENSOR_CAMERA)){
            msgText = "Activity Detected!";
        }

        //Store current alert details in SharedPrefs
        SharedPreferences prefs = context.getSharedPreferences("fyp", Context.MODE_PRIVATE);
        prefs.edit().putString(Constants.SENSOR_NAME, sensor).apply();
        prefs.edit().putString(Constants.SENSOR_VALUE, sensorValue).apply();

        return String.format("%s : %s", sensor.toUpperCase(), msgText);
    }

    private View.OnLongClickListener onSensorTileLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            TextView tvTitle = (TextView)(view.findViewById(R.id.tv_sensor_name));
            TextView tvValue = (TextView)(view.findViewById(R.id.tv_sensor_output_level));

            String sensorName  = tvTitle.getText().toString();

            //Utils.createDialog(mContext, sensorName, String.valueOf(100));
            return false;
        }
    };

    public static void sendDummyPN(Context context, String sensor, String sensorVal){
        GcmIntentService gcmIntentService = new GcmIntentService();
        gcmIntentService.sendNotification(constructMsg(context, null, sensor, sensorVal), context);
    }

}