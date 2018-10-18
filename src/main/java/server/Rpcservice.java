package server;

import annotaion.RPCSer;
import handler.EDHandler;
import handler.Spliter;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.InetSocketAddress;
import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther :huiqiang
 * @Description :
 * @Date: Create in 11:03 2018/10/18 2018
 * @Modify:
 */
public class Rpcservice  implements ApplicationContextAware {

    private int port;
    public Rpcservice(int port) {
        this.port = port;
    }
    private Map<String,Object> serviceMap = new HashMap<String,Object>();

    public void startServer(){
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
                .group(boosGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(EDHandler.INSTANCE);
                        ch.pipeline().addLast("handler",new ServerHandler(serviceMap));

                    }
                });
        bind(serverBootstrap, port);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        Map<String,Object>  map  =applicationContext.getBeansWithAnnotation(RPCSer.class);
        for (Map.Entry<String, Object> entry: map.entrySet()){
            String interfaceName = entry.getValue().getClass().getAnnotation(RPCSer.class).value().getName();
            serviceMap.put(interfaceName,entry.getValue());

        }


    }
        private static ChannelFuture bind(final ServerBootstrap serverBootstrap, final int port) {
            return
                    serverBootstrap.bind(port).addListener(x->{
                        if (x.isSuccess()){
                            System.out.println("端口绑定成功");
                        }else {
                            System.out.println("端口绑定失败");
                            bind(serverBootstrap,port+1);
                        }
                    });

        }
}
