package ie.ucc.cs1.fyp;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

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

    public static void createDialog(Context context,String sensorName, String sensorValue){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sensor_output);

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
        }

        ((TextView)dialog.findViewById(R.id.dialog_tv_sensor_details)).setText(context.getString(sensorDetailsID));

        dialog.show();
    }

}