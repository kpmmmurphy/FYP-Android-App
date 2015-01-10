package ie.ucc.cs1.fyp.Model;

/**
 * Created by kpmmmurphy on 10/01/15.
 */
public class APIManager {
    protected int sensor_value_upload_rate;
    protected int camera_image_upload_rate;
    protected int sys_config_request_rate;

    public int getSensor_value_upload_rate() {
        return sensor_value_upload_rate;
    }

    public void setSensor_value_upload_rate(int sensor_value_upload_rate) {
        this.sensor_value_upload_rate = sensor_value_upload_rate;
    }

    public int getCamera_image_upload_rate() {
        return camera_image_upload_rate;
    }

    public void setCamera_image_upload_rate(int camera_image_upload_rate) {
        this.camera_image_upload_rate = camera_image_upload_rate;
    }

    public int getSys_config_request_rate() {
        return sys_config_request_rate;
    }

    public void setSys_config_request_rate(int sys_config_request_rate) {
        this.sys_config_request_rate = sys_config_request_rate;
    }
}
