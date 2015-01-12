package ie.ucc.cs1.fyp.Model;

import ie.ucc.cs1.fyp.Socket.Session;

/**
 * Created by kpmmmurphy on 12/01/15.
 */
public class Payload {

    protected Integer status_code;
    protected String msg_response;
    protected CurrentSensorValues current_sensor_values;
    protected Config config;
    protected Session session;

    public CurrentSensorValues getCurrentSensorOutputs() {
        return current_sensor_values;
    }

    public void setCurrentSensorOutputs(CurrentSensorValues currentSensorValues) {
        this.current_sensor_values = currentSensorValues;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getMsg_response() {
        return msg_response;
    }

    public void setMsg_response(String msg_response) {
        this.msg_response = msg_response;
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
}
