package ie.ucc.cs1.fyp.Model;

/**
 * Created by kpmmmurphy on 09/01/15.
 */
public class CurrentSensorValues {

    protected String id;
    protected String data_and_time;
    protected int temperature;
    protected int carbon_monoxide;
    protected int motion;
    protected int flammable_gas;
    protected int min_temperature;
    protected int max_temperature;
    protected int min_carbon_monoxide;
    protected int max_carbon_monoxide;
    protected int min_flammable_gas;
    protected int max_flammable_gas;
    protected int precentage_motion;

    public CurrentSensorValues(String data_and_time, int temperature, int carbon_monoxide, int motion, int flammable_gas, int min_temperature, int max_temperature, int min_carbon_monoxide, int max_carbon_monoxide, int min_flammable_gas, int max_flammable_gas, int precentage_motion) {
        this.data_and_time = data_and_time;
        this.temperature = temperature;
        this.carbon_monoxide = carbon_monoxide;
        this.motion = motion;
        this.flammable_gas = flammable_gas;
        this.min_temperature = min_temperature;
        this.max_temperature = max_temperature;
        this.min_carbon_monoxide = min_carbon_monoxide;
        this.max_carbon_monoxide = max_carbon_monoxide;
        this.min_flammable_gas = min_flammable_gas;
        this.max_flammable_gas = max_flammable_gas;
        this.precentage_motion = precentage_motion;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getCarbon_monoxide() {
        return carbon_monoxide;
    }

    public void setCarbon_monoxide(int carbon_monoxide) {
        this.carbon_monoxide = carbon_monoxide;
    }

    public int getMotion() {
        return motion;
    }

    public void setMotion(int motion) {
        this.motion = motion;
    }

    public int getFlammable_gas() {
        return flammable_gas;
    }

    public void setFlammable_gas(int flammable_gas) {
        this.flammable_gas = flammable_gas;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData_and_time() {
        return data_and_time;
    }

    public void setData_and_time(String data_and_time) {
        this.data_and_time = data_and_time;
    }

    public int getMin_temperature() {
        return min_temperature;
    }

    public void setMin_temperature(int min_temperature) {
        this.min_temperature = min_temperature;
    }

    public int getMax_temperature() {
        return max_temperature;
    }

    public void setMax_temperature(int max_temperature) {
        this.max_temperature = max_temperature;
    }

    public int getMin_carbon_monoxide() {
        return min_carbon_monoxide;
    }

    public void setMin_carbon_monoxide(int min_carbon_monoxide) {
        this.min_carbon_monoxide = min_carbon_monoxide;
    }

    public int getMax_carbon_monoxide() {
        return max_carbon_monoxide;
    }

    public void setMax_carbon_monoxide(int max_carbon_monoxide) {
        this.max_carbon_monoxide = max_carbon_monoxide;
    }

    public int getMin_flammable_gas() {
        return min_flammable_gas;
    }

    public void setMin_flammable_gas(int min_flammable_gas) {
        this.min_flammable_gas = min_flammable_gas;
    }

    public int getMax_flammable_gas() {
        return max_flammable_gas;
    }

    public void setMax_flammable_gas(int max_flammable_gas) {
        this.max_flammable_gas = max_flammable_gas;
    }

    public int getPrecentage_motion() {
        return precentage_motion;
    }

    public void setPrecentage_motion(int precentage_motion) {
        this.precentage_motion = precentage_motion;
    }
}
