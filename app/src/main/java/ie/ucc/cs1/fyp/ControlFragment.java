package ie.ucc.cs1.fyp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ie.ucc.cs1.fyp.Model.APIResponse;
import ie.ucc.cs1.fyp.Model.Config;
import ie.ucc.cs1.fyp.Model.Packet;
import ie.ucc.cs1.fyp.Model.Payload;
import ie.ucc.cs1.fyp.Model.Sensor;
import ie.ucc.cs1.fyp.Network.API;
import ie.ucc.cs1.fyp.Socket.Session;
import ie.ucc.cs1.fyp.Socket.SocketManager;

/**
 * Created by kpmmmurphy on 04/11/14.
 */
public class ControlFragment extends Fragment{

    private static String LOGTAG = "__CAMERA_FRAGMENT";

    @InjectView(R.id.fragment_control_layout)
    ScrollView controlLayout;

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
    Button submitBtn;

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

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(BuildConfig.DEBUG){
                    Log.d(LOGTAG, "Submitting Config");
                    if(Session.getInstance(getActivity().getApplicationContext()).isConnectedToPi()){
                        Payload payload =  new Payload();
                        payload.setConfig(gatherInput());
                        Packet configPacket = new Packet(Constants.SERVICE_CONFIG , payload);
                        SocketManager.getInstance(getActivity()).sendPacketToPi(Utils.toJson(configPacket));
                    }else{
                        //API...
                        YoYo.with(Techniques.FadeOut).duration(700).playOn(controlLayout);
                        API.getInstance(getActivity()).uploadSystemConfig(gatherInput(), updateConfigSuccessListener,updateConfigErrorListener);
                    }
                }
            }
        });
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
            fillFields(null);
        }else{
            //API
            API.getInstance(getActivity()).requestSystemConfig(getConfigSuccessListener, getConfigErrorListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.methodDebug(LOGTAG);
    }

    private void fillFields(Config config){

        if(config == null)
            config = Session.getInstance(getActivity()).getConfig();

        if (config != null){
            /*System Details*/
            systemName.setText(config.getSystemDetailsManager().getName());
            systemLocation.setText(config.getSystemDetailsManager().getLocation());
            systemLat.setText(config.getSystemDetailsManager().getGps_lat());
            systemLng.setText(config.getSystemDetailsManager().getGps_lng());

        /*API Manager*/
            cameraUploadRate.setText(String.valueOf(config.getApiManager().getCamera_image_upload_rate()));
            sensorValueUploadRate.setText(String.valueOf(config.getApiManager().getSensor_value_upload_rate()));
            systemConfigRate.setText(String.valueOf(config.getApiManager().getSys_config_request_rate()));

        /*Alert Manager*/
            buzzerOn.setChecked(config.getAlertManager().isBuzzer_on());
            camereOn.setChecked(config.getAlertManager().isCamera_on());
            videoMode.setChecked(config.getAlertManager().isVideo_mode());

        /*Sensor Manager*/
            collectionRate.setText(String.valueOf(config.getSensorManager().getCollection_rate()));
            collectionPriorty.setText(String.valueOf(config.getSensorManager().getCollection_priority()));

        /*Access Point Manager*/
            sensorValueSendRate.setText(String.valueOf(config.getWifiDirectManager().getSensor_value_send_rate()));

        /*----SENSORS----*/
            for(Sensor sensor : config.getSensors()){
                if(sensor.getName().equalsIgnoreCase(Constants.SENSOR_NAME_MQ7)){
                    mq7AlertThreshold.setText(String.valueOf(sensor.getAlert_threshold()));
                    mq7IsActive.setChecked(sensor.isIs_active());
                    mq7Priority.setText(String.valueOf(sensor.getPriority()));
                    mq7ProbeRate.setText(String.valueOf(sensor.getProbe_rate()));
                }else if(sensor.getName().equalsIgnoreCase(Constants.SENSOR_NAME_MQ2)){
                    mq2AlertThreshold.setText(String.valueOf(sensor.getAlert_threshold()));
                    mq2IsActive.setChecked(sensor.isIs_active());
                    mq2Priority.setText(String.valueOf(sensor.getPriority()));
                    mq2ProbeRate.setText(String.valueOf(sensor.getProbe_rate()));
                }else if(sensor.getName().equalsIgnoreCase(Constants.SENSOR_NAME_THERMISTOR)){
                    thermistorAlertThreshold.setText(String.valueOf(sensor.getAlert_threshold()));
                    thermistorIsActive.setChecked(sensor.isIs_active());
                    thermistorPriority.setText(String.valueOf(sensor.getPriority()));
                    thermistorProbeRate.setText(String.valueOf(sensor.getProbe_rate()));
                }else if(sensor.getName().equalsIgnoreCase(Constants.SENSOR_NAME_MOTION)){
                    motionAlertThreshold.setText(String.valueOf(sensor.getAlert_threshold()));
                    motionIsActive.setChecked(sensor.isIs_active());
                    motionPriority.setText(String.valueOf(sensor.getPriority()));
                    motionProbeRate.setText(String.valueOf(sensor.getProbe_rate()));
                }
            }
        }
    }

    protected Config gatherInput(){
        Config config = new Config();

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
        ArrayList<Sensor> sensors = new ArrayList<Sensor>();
        Sensor sensor = new Sensor();

        sensor.setName(Constants.SENSOR_NAME_MQ7.toLowerCase());
        sensor.setAlert_threshold(Integer.valueOf(mq7AlertThreshold.getText().toString()));
        sensor.setIs_active(mq7IsActive.isChecked());
        sensor.setProbe_rate(Integer.valueOf(mq7ProbeRate.getText().toString()));
        sensor.setPriority(Integer.valueOf(mq7Priority.getText().toString()));
        sensors.add(sensor);

        sensor = new Sensor();
        sensor.setName(Constants.SENSOR_NAME_MQ2.toLowerCase());
        sensor.setAlert_threshold(Integer.valueOf(mq2AlertThreshold.getText().toString()));
        sensor.setIs_active(mq2IsActive.isChecked());
        sensor.setProbe_rate(Integer.valueOf(mq2ProbeRate.getText().toString()));
        sensor.setPriority(Integer.valueOf(mq2Priority.getText().toString()));
        sensors.add(sensor);

        sensor = new Sensor();
        sensor.setName(Constants.SENSOR_NAME_THERMISTOR.toLowerCase());
        sensor.setAlert_threshold(Integer.valueOf(thermistorAlertThreshold.getText().toString()));
        sensor.setIs_active(thermistorIsActive.isChecked());
        sensor.setProbe_rate(Integer.valueOf(thermistorProbeRate.getText().toString()));
        sensor.setPriority(Integer.valueOf(thermistorPriority.getText().toString()));
        sensors.add(sensor);

        sensor = new Sensor();
        sensor.setName(Constants.SENSOR_NAME_MOTION.toLowerCase());
        sensor.setAlert_threshold(Integer.valueOf(motionAlertThreshold.getText().toString()));
        sensor.setIs_active(motionIsActive.isChecked());
        sensor.setProbe_rate(Integer.valueOf(motionProbeRate.getText().toString()));
        sensor.setPriority(Integer.valueOf(motionPriority.getText().toString()));
        sensors.add(sensor);

        config.setSensors(sensors);

        String configString = Utils.toJson(config);
        if (BuildConfig.DEBUG){
            Log.d(LOGTAG, configString);
        }
        Session.getInstance(getActivity()).setConfig(config);
        return config;
    }

    private Response.Listener<Config> getConfigSuccessListener = new Response.Listener<Config>() {
        @Override
        public void onResponse(Config response) {
            Utils.methodDebug(LOGTAG);
            fillFields(response);
        }
    };

    private Response.ErrorListener getConfigErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.methodDebug(LOGTAG);
        }
    };

    private Response.Listener<APIResponse> updateConfigSuccessListener = new Response.Listener<APIResponse>() {
        @Override
        public void onResponse(APIResponse response) {
            Utils.methodDebug(LOGTAG);
            if(response.getStatus_code() == Constants.CONNECT_SUCCESS){

            }
            Toast.makeText(getActivity(), getString(R.string.config_upload_success),Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.FadeIn).duration(700).playOn(controlLayout);

        }
    };

    private Response.ErrorListener updateConfigErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Utils.methodDebug(LOGTAG);
            Toast.makeText(getActivity(), getString(R.string.config_upload_failed),Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.FadeOut).duration(700).playOn(controlLayout);
        }
    };

}
