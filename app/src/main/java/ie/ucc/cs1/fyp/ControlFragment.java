package ie.ucc.cs1.fyp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Model.Config;
import ie.ucc.cs1.fyp.Model.Sensor;
import ie.ucc.cs1.fyp.Socket.Session;

/**
 * Created by kpmmmurphy on 04/11/14.
 */
public class ControlFragment extends Fragment{

    private static String LOGTAG = "__CAMERA_FRAGMENT";

    /*System Details*/
    @InjectView(R.id.et_system_config_name)
    EditText systemName;
    @InjectView(R.id.et_system_config_location)
    EditText systemLocation;
    @InjectView(R.id.et_system_config_gps_lat)
    EditText systemLat;
    @InjectView(R.id.et_system_config_gps_lng)
    EditText systemLng;

    /*Sensor Manager*/
    @InjectView(R.id.et_sensor_manager_collection_priority)
    EditText collectionPriorty;
    @InjectView(R.id.et_sensor_manager_collection_rate)
    EditText collectionRate;

    /*Alert Manager*/
    @InjectView(R.id.switch_alert_manager_buzzer_on)
    Switch buzzerOn;
    @InjectView(R.id.switch_alert_manager_camera_on)
    Switch camereOn;
    @InjectView(R.id.switch_alert_manager_video_mode)
    Switch videoMode;

    /*API Manager*/
    @InjectView(R.id.et_api_manager_camera_image_upload_rate)
    EditText cameraUploadRate;
    @InjectView(R.id.et_api_manager_sensor_value_upload_rate)
    EditText sensorValueUploadRate;
    @InjectView(R.id.et_api_manager_system_config_request_rate)
    EditText systemConfigRate;

    /*Access Point Manager*/
    @InjectView(R.id.et_access_point_manager_sensor_value_send_rate)
    EditText sensorValueSendRate;

    /*----SENSORS----*/
    /*MQ7*/
    @InjectView(R.id.switch_mq7_is_active)
    Switch mq7IsActive;
    @InjectView(R.id.et_mq7_alert_threshold)
    EditText mq7AlertThreshold;
    @InjectView(R.id.et_mq7_probe_rate)
    EditText mq7ProbeRate;
    @InjectView(R.id.et_mq7_priority)
    EditText mq7Priority;

    /*MQ2*/
    @InjectView(R.id.switch_mq2_is_active)
    Switch mq2IsActive;
    @InjectView(R.id.et_mq2_alert_threshold)
    EditText mq2AlertThreshold;
    @InjectView(R.id.et_mq2_probe_rate)
    EditText mq2ProbeRate;
    @InjectView(R.id.et_mq2_priority)
    EditText mq2Priority;

    /*Thermistor*/
    @InjectView(R.id.switch_thermistor_is_active)
    Switch thermistorIsActive;
    @InjectView(R.id.et_thermistor_alert_threshold)
    EditText thermistorAlertThreshold;
    @InjectView(R.id.et_thermistor_probe_rate)
    EditText thermistorProbeRate;
    @InjectView(R.id.et_thermistor_priority)
    EditText thermistorPriority;

    /*Thermistor*/
    @InjectView(R.id.switch_motion_is_active)
    Switch motionIsActive;
    @InjectView(R.id.et_motion_alert_threshold)
    EditText motionAlertThreshold;
    @InjectView(R.id.et_motion_probe_rate)
    EditText motionProbeRate;
    @InjectView(R.id.et_motion_priority)
    EditText motionPriority;

    @InjectView(R.id.btn_submit)
    Button submit;

    public ControlFragment() {
        Utils.methodDebug(LOGTAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.methodDebug(LOGTAG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utils.methodDebug(LOGTAG);
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Utils.methodDebug(LOGTAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.methodDebug(LOGTAG);
        if(Session.getInstance(getActivity()).isConnectedToPi()){
            //Session.getInstance(getActivity()).getConfig();
        }else{
            //API
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.methodDebug(LOGTAG);
    }

    private void gatherInput(){
        Config config = Session.getInstance(getActivity()).getConfig();

        /*System Details*/
        config.getSystemDetailsManager().setName(systemName.getText().toString());
        config.getSystemDetailsManager().setLocation(systemLocation.getText().toString());
        config.getSystemDetailsManager().setGps_lat(systemLat.getText().toString());
        config.getSystemDetailsManager().setGps_lng(systemLng.getText().toString());

        /*API Manager*/
        config.getApiManager().setCamera_image_upload_rate(Integer.valueOf(cameraUploadRate.getText().toString()));
        config.getApiManager().setSensor_value_upload_rate(Integer.valueOf(sensorValueUploadRate.getText().toString()));
        config.getApiManager().setSys_config_request_rate(Integer.valueOf(systemConfigRate.getText().toString()));

        /*SensorManager*/
        config.getSensorManager().setCollection_priority(Integer.valueOf(collectionPriorty.getText().toString()));
        config.getSensorManager().setCollection_rate(Integer.valueOf(collectionRate.getText().toString()));

        /*Access Point Manager*/
        config.getWifiDirectManager().setSensor_value_send_rate(Integer.valueOf(sensorValueSendRate.getText().toString()));

        /*Alert Manager*/
        config.getAlertManager().setBuzzer_on(buzzerOn.isChecked());
        config.getAlertManager().setCamera_on(camereOn.isChecked());
        config.getAlertManager().setVideo_mode(videoMode.isChecked());

        /*----SENSORS----*/
        for(Sensor sensor : config.getSensors()){
            if(sensor.getName().equals(Constants.SENSOR_NAME_MQ7)){
                sensor.setAlert_threshold(Integer.valueOf(mq7AlertThreshold.getText().toString()));
                sensor.setIs_active(mq7IsActive.isChecked());
                sensor.setProbe_rate(Integer.valueOf(mq7ProbeRate.getText().toString()));
                sensor.setPriority(Integer.valueOf(mq7Priority.getText().toString()));
            }else if(sensor.getName().equals(Constants.SENSOR_NAME_MQ2)){
                sensor.setAlert_threshold(Integer.valueOf(mq2AlertThreshold.getText().toString()));
                sensor.setIs_active(mq2IsActive.isChecked());
                sensor.setProbe_rate(Integer.valueOf(mq2ProbeRate.getText().toString()));
                sensor.setPriority(Integer.valueOf(mq2Priority.getText().toString()));
            }else if(sensor.getName().equals(Constants.SENSOR_NAME_THERMISTOR)){
                sensor.setAlert_threshold(Integer.valueOf(thermistorAlertThreshold.getText().toString()));
                sensor.setIs_active(thermistorIsActive.isChecked());
                sensor.setProbe_rate(Integer.valueOf(thermistorProbeRate.getText().toString()));
                sensor.setPriority(Integer.valueOf(thermistorPriority.getText().toString()));
            }else if(sensor.getName().equals(Constants.SENSOR_NAME_SMOKE)){

            }
        }
    }
}
