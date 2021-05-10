package tools;


// for json to object mapping test
public class AndroidResponse {

    String success = "false";
    public AndroidResponse(String success) {
        this.success = success;
    }

    public AndroidResponse() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
