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

    public CurrentSensorValues(String data_and_time, int temperature, int carbon_monoxide, int motion, int flammable_gas) {
        this.data_and_time = data_and_time;
        this.temperature = temperature;
        this.carbon_monoxide = carbon_monoxide;
        this.motion = motion;
        this.flammable_gas = flammable_gas;
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
}
