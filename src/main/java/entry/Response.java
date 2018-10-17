package entry;

import lombok.Data;

@Data
public class Response {

    private String requestId;
    private Throwable error;
    private Object result;



    @Override
    public String toString() {
        return "Response{" +
                "requestId='" + requestId + '\'' +
                ", error=" + error +
                ", result=" + result +
                '}';
    }
}
