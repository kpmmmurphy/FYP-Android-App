package ie.ucc.cs1.fyp;

import java.util.List;

import ie.ucc.cs1.fyp.Model.SensorOutput;
import retrofit.RestAdapter;
import retrofit.http.GET;

/**
 * Retrofit API interface, for talking to the Pi
 *
 * Created by kpmmmurphy on 24/10/14.
 */
public class SystemAPIClient {

    private static final String API_URL = "https://127.0.0.1/fyp";

    public interface SensorOutputs {
        @GET("/sensors/sensor_output")
        List<SensorOutput> sensorOutputs();
    }

    static class LatestSensorOutputs implements SensorOutputs {
        @Override
        public List<SensorOutput> sensorOutputs() {
            return null;
        }

        //Default Constuctor
        public LatestSensorOutputs(){}
    }

    public static void main(String... args){

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();

        // Wrap our REST adapter to allow mock implementations and fake network delay.
        //MockRestAdapter mockRestAdapter = MockRestAdapter.from(restAdapter);
    }
}
