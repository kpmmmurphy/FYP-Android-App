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



}
