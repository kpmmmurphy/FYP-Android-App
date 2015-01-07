package ie.ucc.cs1.fyp;

/**
 * Created by kpmmmurphy on 30/10/14.
 */
public interface Constants {

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
    public static final int SOCKET_MULTICAST_PORT        = 5007;


}
