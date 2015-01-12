package ie.ucc.cs1.fyp.Model;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class Packet {
    protected String service;
    protected Payload payload;

    public Packet(String service, Payload payload) {
        this.service = service;
        this.payload = payload;
    }

    public String getService() {
        return service;
    }

    public Payload getPayload() {
        return payload;
    }
}
