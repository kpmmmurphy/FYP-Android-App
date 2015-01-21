package ie.ucc.cs1.fyp.Model;

import java.util.ArrayList;

/**
 * Created by kpmmmurphy on 21/01/15.
 */
public class SensorValuesHolder {
    ArrayList<CurrentSensorValuesFromServer> sensor_values_list;

    public ArrayList<CurrentSensorValuesFromServer> getSensor_values_list() {
        return sensor_values_list;
    }

    public void setSensor_values_list(ArrayList<CurrentSensorValuesFromServer> sensor_values_list) {
        this.sensor_values_list = sensor_values_list;
    }
}
