package ie.ucc.cs1.fyp.Model;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class Packet {
    protected String service;
    protected String payload;

    public Packet(String service, String payload) {
        this.service = service;
        this.payload = payload;
    }

    public String getService() {
        return service;
    }

    public String getPayload() {
        return payload;
    }
}
