package ie.ucc.cs1.fyp.Model;

/**
 * Created by kpmmmurphy on 10/01/15.
 */
public class AlertManager {
    protected boolean camera_on;
    protected boolean video_mode;
    protected boolean buzzer_on;

    public boolean isCamera_on() {
        return camera_on;
    }

    public void setCamera_on(boolean camera_on) {
        this.camera_on = camera_on;
    }

    public boolean isVideo_mode() {
        return video_mode;
    }

    public void setVideo_mode(boolean video_mode) {
        this.video_mode = video_mode;
    }

    public boolean isBuzzer_on() {
        return buzzer_on;
    }

    public void setBuzzer_on(boolean buzzer_on) {
        this.buzzer_on = buzzer_on;
    }
}
