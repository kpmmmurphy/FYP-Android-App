package ie.ucc.cs1.fyp.Model;

/**
 * Created by kpmmmurphy on 16/01/15.
 */
public class APIResponse {
    //MetaData Params
    public int status_code;
    public String pi_public_ip;

    //Requested Objects
    public CurrentSensorValuesFromServer sensor_values;

}
