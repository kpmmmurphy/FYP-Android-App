package ie.ucc.cs1.fyp;

/**
 * Created by kpmmmurphy on 30/10/14.
 */
public interface Constants {

    //General
    public static final String LOGTAG = "FYP";

    //Sensor Names
    public static final String SENSOR_NAME_MQ7         = "Carbon_Monoxide";
    public static final String SENSOR_NAME_MQ2         = "Flammable_Gas";
    public static final String SENSOR_NAME_MOTION      = "Motion";
    public static final String SENSOR_NAME_THERMISTOR  = "Temperature";
    public static final String SENSOR_CAMERA           = "Camera";
    public static final String SENSOR_NAME             = "sensor";
    public static final String SENSOR_VALUE            = "value";
    public static final String SENSOR_MEASUREMENT      = "measurement";

    //Sensor Measurement
    public static final String SENSOR_MEASUREMENT_PPM     = "ppm";
    public static final String SENSOR_MEASUREMENT_CELCIUS = "celcius";

    //SocketManager
    public static final String SOCKET_MULTICAST_GROUP_IP = "224.1.1.1";
    public static final int SOCKET_CLIENT_PORT           = 5005;
    public static final int SOCKET_SERVER_PORT           = 5006;
    public static final int SOCKET_MULTICAST_PORT        = 5007;
    public static final int VIDEO_STREAM_PORT            = 12345;

    //Socket Json Keys
    public static final String JSON_KEY_SERVICE = "service";

    //Socket Json Values
    public static final String SERVICE_CONNECT      = "connect";
    public static final String SERVICE_CONFIG       = "config";
    public static final String SERVICE_RESPONSE     = "response";
    public static final String SERVICE_SENSOR_DATA  = "current_sensor_values";
    public static final String SERVICE_PAIRED       = "paired";
    public static final String SERVICE_REQUEST_STREAM  = "request_stream";
    public static final String SERVICE_REQUEST_IMAGE   = "request_image";
    public static final String JSON_VALUE_WIFI_DIRECT_GET_GRAPH_DATA = "get_graph_data";
    public static final String GRAPH_DATA_CUR_HOUR         = "sensor_list_current_hour";
    public static final String GRAPH_DATA_CUR_DAY_AGG_HOUR = "sensor_list_current_day_agg_hour";
    public static final String GRAPH_DATA_AGG_DAY          = "sensor_list_agg_day";

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
    public static final String CONFIG_DEFAULT = "{\"sensor_manager\": {\"collection_priority\": 1, \"collection_rate\": 15}, \"system_details_manager\": {\"gps_lat\": \"Not set\", \"gps_lng\": \"Not set\", \"name\": \"Security Centre\", \"location\": \"Where am I located?\"}, \"api_manager\": {\"sensor_value_upload_rate\": 10, \"camera_image_upload_rate\": 60, \"sys_config_request_rate\": 60}, \"wifi_direct_manager\": {\"sensor_value_send_rate\": 20}, \"database_manager\": null, \"sensors\": [{\"priority\": 1, \"is_active\": true, \"name\": \"motion\", \"alert_threshold\": 1, \"probe_rate\": 10}, {\"priority\": 1, \"is_active\": true, \"name\": \"flammable_gas\", \"alert_threshold\": 50, \"probe_rate\": 10}, {\"priority\": 1, \"is_active\": true, \"name\": \"carbon_monoxide\", \"alert_threshold\": 50, \"probe_rate\": 10}, {\"priority\": 1, \"is_active\": true, \"name\": \"temperature\", \"alert_threshold\": 50, \"probe_rate\": 10}], \"alert_manager\": {\"camera_on\": true, \"video_mode\": false, \"buzzer_on\": true}}";

    //API
    public final static String API_URL = "http://cs1.ucc.ie/~kpm2/fyp/api/";
    public final static String API_CAMERA = "camera/";
    public final static String API_REQUEST_HEADER_SERVICE            = "service";
    public final static String API_REQUEST_SERVICE_GET_SENSOR_VALUES = "get_sensor_values";
    public final static String API_REQUEST_SERVICE_GET_CONFIG        = "get_config";
    public final static String API_REQUEST_SERVICE_UPDATE_CONFIG     = "update_config";
    public final static String API_REQUEST_SERVICE_LIST_IMAGES       = "list_images";
    public final static String API_REQUEST_SERVICE_CURRENT_HOUR_SENSOR_VALUES = "get_current_hour_sensor_values";
    public final static String API_REQUEST_SERVICE_AGG_SENSOR_VALUES_PER_HOUR = "get_agg_sensor_values_per_hour";
    public final static String API_REQUEST_SERVICE_AGG_SENSOR_VALUES_PER_DAY  = "get_agg_sensor_values_per_day";
    public final static String API_REQUEST_SERVICE_REG_PN_ID          = "insert_reg_ids";
    public static final String API_REQUEST_SERVICE_REQUEST_STREAM     = "request_video_stream";
    public static final String API_REQUEST_SERVICE_REQUEST_NEW_IMAGE  = "requesting_new_image";

    //GRAPHING
    public static final int GRAPH_DATE_TIME_TYPE_HOUR_WITH_SECOND = 1;
    public static final int GRAPH_DATE_TIME_TYPE_HOUR = 2;
    public static final int GRAPH_DATE_TIME_TYPE_DAY = 3;

    //PUSH NOTIFICATIONS
    public static final String GCM_API_KEY = "42480706366";
    public static final String PN_TITLE = "Sensor System";
    public static final String PN_FROM_PENDING_INTENT = "PN_FROM_PENDING_INTENT";
}
