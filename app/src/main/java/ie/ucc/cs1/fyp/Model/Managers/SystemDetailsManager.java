package ie.ucc.cs1.fyp.Model.Managers;

/**
 * Created by kpmmmurphy on 10/01/15.
 */
public class SystemDetailsManager {
    protected String name;
    protected String location;
    protected String gps_lat;
    protected String gps_lng;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGps_lat() {
        return gps_lat;
    }

    public void setGps_lat(String gps_lat) {
        this.gps_lat = gps_lat;
    }

    public String getGps_lng() {
        return gps_lng;
    }

    public void setGps_lng(String gps_lng) {
        this.gps_lng = gps_lng;
    }
}
