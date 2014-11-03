package ie.ucc.cs1.fyp.Model;

/**
 * Model corresponding to the Current_Day_Sensor_Output Database Table
 *
 * Created by Kevin Murphy on 22/10/14.
 */
public class SensorOutput {
    protected String  name;
    protected Integer value;
    protected String  measurement;

    public SensorOutput(String name,String measurement, Integer value) {
        this.value = value;
        this.name = name;
        this.measurement = measurement;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
}
