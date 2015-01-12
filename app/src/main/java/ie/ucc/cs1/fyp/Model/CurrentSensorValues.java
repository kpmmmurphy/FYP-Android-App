package ie.ucc.cs1.fyp.Model;

/**
 * Created by kpmmmurphy on 09/01/15.
 */
public class CurrentSensorValues {

    protected int temperature;
    protected int carbon_monoxide;
    protected int motion;
    protected int flammable_gas;

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
}
