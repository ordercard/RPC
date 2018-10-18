package entry;

import lombok.Data;

import java.util.Arrays;
@Data
public class Request  extends  Packet{

    private String requestId;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;



    @Override
    public String toString() {
        return "Request{" +
                "requestId='" + requestId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }

    @Override
    public Byte getCommand() {
        return  (byte)1;
    }
}
