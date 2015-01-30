package ie.ucc.cs1.fyp.Model;

import ie.ucc.cs1.fyp.Socket.Session;

/**
 * Created by kpmmmurphy on 12/01/15.
 */
public class Payload {

    protected CurrentSensorValues current_sensor_values;
    protected Config config;
    protected Session session;
    protected Status status;
    protected Paired paired;

    public CurrentSensorValues getCurrentSensorOutputs() {
        return current_sensor_values;
    }

    public void setCurrentSensorOutputs(CurrentSensorValues currentSensorValues) {
        this.current_sensor_values = currentSensorValues;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Paired getPaired() {
        return paired;
    }

    public void setPaired(Paired paired) {
        this.paired = paired;
    }
}
