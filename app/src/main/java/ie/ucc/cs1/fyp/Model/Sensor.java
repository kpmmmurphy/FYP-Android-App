package ie.ucc.cs1.fyp.Model;

/**
 * Created by kpmmmurphy on 10/01/15.
 */
public class Sensor {
    protected boolean is_active;
    protected int priority;
    protected String name;
    protected int alert_threshold;
    protected int probe_rate;

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAlert_threshold() {
        return alert_threshold;
    }

    public void setAlert_threshold(int alert_threshold) {
        this.alert_threshold = alert_threshold;
    }

    public int getProbe_rate() {
        return probe_rate;
    }

    public void setProbe_rate(int probe_rate) {
        this.probe_rate = probe_rate;
    }
}
