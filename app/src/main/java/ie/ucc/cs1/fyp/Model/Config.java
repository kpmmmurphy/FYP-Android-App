package ie.ucc.cs1.fyp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kpmmmurphy on 10/01/15.
 */
public class Config {
    @SerializedName("sensor_manager")
    ie.ucc.cs1.fyp.SensorManager sensorManager;

    @SerializedName("system_details_manager")
    SystemDetailsManager systemDetailsManager;

    @SerializedName("wifi_direct_manager")
    WifiDirectManager wifiDirectManager;

    @SerializedName("alert_manager")
    AlertManager alertManager;

    @SerializedName("sensors")
    ArrayList<Sensor> sensors;

    @SerializedName("api_manager")
    APIManager apiManager;
}
