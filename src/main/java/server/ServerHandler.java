package server;

import entry.Request;
import entry.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

/**
 * @Auther :huiqiang
 * @Description :
 * @Date: Create in 11:15 2018/10/18 2018
 * @Modify:
 */
public class ServerHandler extends SimpleChannelInboundHandler<Request> {
    private Map<String, Object> serviceMap;

    public ServerHandler(Map<String, Object> serviceMap) {
        this.serviceMap = serviceMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Request msg) throws Exception {
        Response response = new Response();

        try {
            Object o = serviceMap.get(msg.getClassName());
            Class<?> cla = Class.forName(msg.getClassName());
            Method method = cla.getMethod(msg.getMethodName(), msg.getParameterTypes());
            Object res = method.invoke(o, msg.getParameters());
            response.setRequestId(UUID.randomUUID().toString());
            response.setResult(res);
        } catch (Exception e) {
            response.setError(e);
            response.setRequestId(UUID.randomUUID().toString());
        }

        System.out.println(msg
                +""+response);
        //返回执行结果
        ctx.channel().writeAndFlush(response);
    }
}
