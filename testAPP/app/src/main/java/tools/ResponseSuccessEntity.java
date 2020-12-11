package tools;


// for json to object mapping test
public class ResponseSuccessEntity {

    String success = "false";
    public ResponseSuccessEntity(String success) {
        this.success = success;
    }

    public ResponseSuccessEntity() {
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
