package ie.ucc.cs1.fyp.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kpmmmurphy on 10/01/15.
 */
public class Config {
    @SerializedName("sensor_manager")
    SensorManager sensorManager;

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

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    public SystemDetailsManager getSystemDetailsManager() {
        return systemDetailsManager;
    }

    public void setSystemDetailsManager(SystemDetailsManager systemDetailsManager) {
        this.systemDetailsManager = systemDetailsManager;
    }

    public WifiDirectManager getWifiDirectManager() {
        return wifiDirectManager;
    }

    public void setWifiDirectManager(WifiDirectManager wifiDirectManager) {
        this.wifiDirectManager = wifiDirectManager;
    }

    public AlertManager getAlertManager() {
        return alertManager;
    }

    public void setAlertManager(AlertManager alertManager) {
        this.alertManager = alertManager;
    }

    public ArrayList<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(ArrayList<Sensor> sensors) {
        this.sensors = sensors;
    }

    public APIManager getApiManager() {
        return apiManager;
    }

    public void setApiManager(APIManager apiManager) {
        this.apiManager = apiManager;
    }
}
