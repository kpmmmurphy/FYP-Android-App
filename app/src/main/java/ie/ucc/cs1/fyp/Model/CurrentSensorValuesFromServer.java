package ie.ucc.cs1.fyp.Model;

/**
 * Created by kpmmmurphy on 15/01/15.
 */
public class CurrentSensorValuesFromServer {

    protected String id;
    protected String date_and_time;
    protected String temperature;
    protected String carbon_monoxide;
    protected String motion;
    protected String flammable_gas;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(String date_and_time) {
        this.date_and_time = date_and_time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getCarbon_monoxide() {
        return carbon_monoxide;
    }

    public void setCarbon_monoxide(String carbon_monoxide) {
        this.carbon_monoxide = carbon_monoxide;
    }

    public String getMotion() {
        return motion;
    }

    public void setMotion(String motion) {
        this.motion = motion;
    }

    public String getFlammable_gas() {
        return flammable_gas;
    }

    public void setFlammable_gas(String flammable_gas) {
        this.flammable_gas = flammable_gas;
    }
}
