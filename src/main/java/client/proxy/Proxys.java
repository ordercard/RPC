package client.proxy;

import client.RpcClient;
import entry.Request;
import entry.Response;

import java.lang.reflect.Method;

public class Proxys {

    private String address;
    private int port;

    public Proxys(String address, int port) {
        this.address = address;
        this.port = port;
    }


    public <T> T proxy(Class<?> claz){


        return (T)java.lang.reflect.Proxy.newProxyInstance(claz.getClassLoader(),new Class[]{claz},(Object proxy, Method method, Object[] args)->{
            Request request =new Request();
            request.setClassName(method.getDeclaringClass().getName());
            request.setMethodName(method.getName());
            request.setParameters(args);
            request.setParameterTypes(method.getParameterTypes());
            RpcClient client =new RpcClient(address,port);
            Response response = client.send(request);
            if (response.getError()!=null){
                throw response.getError();
            }
            else{
                return response;
            }
        });




    }
}
