package ie.ucc.cs1.fyp.Model;

/**
 * Created by kpmmmurphy on 15/01/15.
 */
public class CurrentSensorValuesFromServer {

    protected String id;
    protected String date_and_time;
    protected String temperature;
    protected String avg_temperature;
    protected String min_temperature;
    protected String max_temperature;
    protected String carbon_monoxide;
    protected String avg_carbon_monoxide;
    protected String min_carbon_monoxide;
    protected String max_carbon_monoxide;
    protected String flammable_gas;
    protected String avg_flammable_gas;
    protected String min_flammable_gas;
    protected String max_flammable_gas;
    protected String motion;
    protected String percentage_motion;

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

    public String getAvg_temperature() {
        return avg_temperature;
    }

    public void setAvg_temperature(String avg_temperature) {
        this.avg_temperature = avg_temperature;
    }

    public String getMin_temperature() {
        return min_temperature;
    }

    public void setMin_temperature(String min_temperature) {
        this.min_temperature = min_temperature;
    }

    public String getMax_temperature() {
        return max_temperature;
    }

    public void setMax_temperature(String max_temperature) {
        this.max_temperature = max_temperature;
    }

    public String getAvg_carbon_monoxide() {
        return avg_carbon_monoxide;
    }

    public void setAvg_carbon_monoxide(String avg_carbon_monoxide) {
        this.avg_carbon_monoxide = avg_carbon_monoxide;
    }

    public String getMin_carbon_monoxide() {
        return min_carbon_monoxide;
    }

    public void setMin_carbon_monoxide(String min_carbon_monoxide) {
        this.min_carbon_monoxide = min_carbon_monoxide;
    }

    public String getMax_carbon_monoxide() {
        return max_carbon_monoxide;
    }

    public void setMax_carbon_monoxide(String max_carbon_monoxide) {
        this.max_carbon_monoxide = max_carbon_monoxide;
    }

    public String getAvg_flammable_gas() {
        return avg_flammable_gas;
    }

    public void setAvg_flammable_gas(String avg_flammable_gas) {
        this.avg_flammable_gas = avg_flammable_gas;
    }

    public String getMin_flammable_gas() {
        return min_flammable_gas;
    }

    public void setMin_flammable_gas(String min_flammable_gas) {
        this.min_flammable_gas = min_flammable_gas;
    }

    public String getMax_flammable_gas() {
        return max_flammable_gas;
    }

    public void setMax_flammable_gas(String max_flammable_gas) {
        this.max_flammable_gas = max_flammable_gas;
    }

    public String getPercentage_motion() {
        return percentage_motion;
    }

    public void setPercentage_motion(String percentage_motion) {
        this.percentage_motion = percentage_motion;
    }
}
