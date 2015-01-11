package ie.ucc.cs1.fyp.Model;

/**
 * Created by kpmmmurphy on 07/01/15.
 */
public class PiResponse{

    protected int status_code;
    protected String msg_response;

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
}
