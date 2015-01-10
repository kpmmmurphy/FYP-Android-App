package ie.ucc.cs1.fyp;

/**
 * Created by kpmmmurphy on 30/10/14.
 */
public interface Constants {

    //General
    public static final String LOGTAG = "FYP";

    //Sensor Names
    public static final String SENSOR_NAME_MQ7         = "Carbon Dioxide";
    public static final String SENSOR_NAME_MQ2         = "Flammable Gas";
    public static final String SENSOR_NAME_SMOKE       = "Smoke";
    public static final String SENSOR_NAME_THERMISTOR  = "Temperature";

    //Sensor Measurement
    public static final String SENSOR_MEASUREMENT_PPM     = "ppm";
    public static final String SENSOR_MEASUREMENT_CELCIUS = "celcius";

    //SocketManager
    public static final String SOCKET_MULTICAST_GROUP_IP = "224.1.1.1";
    public static final int SOCKET_SERVER_PORT           = 5006;
    public static final int SOCKET_MULTICAST_PORT        = 5007;

    //Socket Json Keys
    public static final String JSON_KEY_SERVICE = "service";

    //Socket Json Values
    public static final String SERVICE_CONNECT      = "connect";
    public static final String SERVICE_CONFIG       = "config";
    public static final String SERVICE_SENSOR_DATA  = "sensor_data";
    public static final String SERVICE_PAIRED       = "paired";

    //Pi Response Values
    public static final int CONNECT_SUCCESS  = 200;

    //Custom Intents
    public static final String INTENT_CONNECTED_TO_PI = "INTENT_CONNECTED_TO_PI";

    //Config
    public static final String CONFIG_SENSOR_MANAGER = "{'sensor_manager': {'collection_priority': 1, 'collection_rate': 15}";
    public static final String CONFIG_SYSTEM_DETAILS_MANAGER = "{system_details_manager ': {'gps_lat': 'Not set', 'gps_lng': 'Not set', 'name': 'Security Centre', 'location': 'Where am I located?'}";
    public static final String CONFIG_API_MANAGER = "{'api_manager': {'sensor_value_upload_rate': 10, 'camera_image_upload_rate': 60, 'sys_config_request_rate': 60}";
    public static final String CONFIG_WIFI_DIRECT_MANAGER = "{'wifi_direct_manager': {'sensor_value_send_rate': 20}";
    public static final String CONFIG_ALERT_MANAGER = "{'alert_manager': {'camera_on': true, 'video_mode': false, 'buzzer_on': true}";

    public static final String CONFIG_MOTION = "{'priority': 1, 'is_active': true, 'name': 'motion', 'alert_threshold': 1, 'probe_rate': 10}";
    public static final String CONFIG_MQ2    = "{'priority': 1, 'is_active': true, 'name': 'flammable_gas', 'alert_threshold': 50, 'probe_rate': 10}";
    public static final String CONFIG_MQ7 = "{'priority': 1, 'is_active': true, 'name': 'carbon_monoxide', 'alert_threshold': 50, 'probe_rate': 10}";
    public static final String CONFIG_THERMISTOR = "{'priority': 1, 'is_active': true, 'name': 'temperature', 'alert_threshold': 50, 'probe_rate': 10}";
    public static final String CONFIG_DEFAULT = "{\"sensor_manager\": {\"collection_priority\": 1, \"collection_rate\": 15}, \"system_details_manager \": {\"gps_lat\": \"Not set\", \"gps_lng\": \"Not set\", \"name\": \"Security Centre\", \"location\": \"Where am I located?\"}, \"api_manager\": {\"sensor_value_upload_rate\": 10, \"camera_image_upload_rate\": 60, \"sys_config_request_rate\": 60}, \"wifi_direct_manager\": {\"sensor_value_send_rate\": 20}, \"database_manager\": null, \"sensors\": [{\"priority\": 1, \"is_active\": true, \"name\": \"motion\", \"alert_threshold\": 1, \"probe_rate\": 10}, {\"priority\": 1, \"is_active\": true, \"name\": \"flammable_gas\", \"alert_threshold\": 50, \"probe_rate\": 10}, {\"priority\": 1, \"is_active\": true, \"name\": \"carbon_monoxide\", \"alert_threshold\": 50, \"probe_rate\": 10}, {\"priority\": 1, \"is_active\": true, \"name\": \"temperature\", \"alert_threshold\": 50, \"probe_rate\": 10}], \"alert_manager\": {\"camera_on\": true, \"video_mode\": false, \"buzzer_on\": true}}";


}
