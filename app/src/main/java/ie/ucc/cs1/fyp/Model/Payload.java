package ie.ucc.cs1.fyp.Model;

import java.util.ArrayList;

/**
 * Created by kpmmmurphy on 12/01/15.
 */
public class Payload {

    protected CurrentSensorValues current_sensor_values;
    protected ArrayList<CurrentSensorValuesFromServer> sensor_list_current_hour;
    protected ArrayList<CurrentSensorValuesFromServer> sensor_list_current_day_agg_hour;
    protected ArrayList<CurrentSensorValuesFromServer> sensor_list_agg_day;
    protected Config config;
    protected Session session;
    protected Paired paired;
    protected String stream_address;
    protected String image_data;

    public CurrentSensorValues getCurrent_sensor_values() {
        return current_sensor_values;
    }

    public void setCurrent_sensor_values(CurrentSensorValues current_sensor_values) {
        this.current_sensor_values = current_sensor_values;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Paired getPaired() {
        return paired;
    }

    public void setPaired(Paired paired) {
        this.paired = paired;
    }

    public ArrayList<CurrentSensorValuesFromServer> getSensor_list_current_hour() {
        return sensor_list_current_hour;
    }

    public void setSensor_list_current_hour(ArrayList<CurrentSensorValuesFromServer> sensor_list_current_hour) {
        this.sensor_list_current_hour = sensor_list_current_hour;
    }

    public ArrayList<CurrentSensorValuesFromServer> getSensor_list_current_day_agg_hour() {
        return sensor_list_current_day_agg_hour;
    }

    public void setSensor_list_current_day_agg_hour(ArrayList<CurrentSensorValuesFromServer> sensor_list_current_day_agg_hour) {
        this.sensor_list_current_day_agg_hour = sensor_list_current_day_agg_hour;
    }

    public ArrayList<CurrentSensorValuesFromServer> getSensor_list_agg_day() {
        return sensor_list_agg_day;
    }

    public void setSensor_list_agg_day(ArrayList<CurrentSensorValuesFromServer> sensor_list_agg_day) {
        this.sensor_list_agg_day = sensor_list_agg_day;
    }

    public String getStream_address() {
        return stream_address;
    }

    public String getImage_data() {
        return image_data;
    }
}
