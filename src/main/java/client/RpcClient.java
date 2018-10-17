package client;

import entry.Request;
import entry.Response;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Data;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Data
public class RpcClient extends SimpleChannelInboundHandler<Response> {

    private static final int MAX_RETRY =5;
    private volatile Response response;
    private Object obj = new Object();
    private String address;
    private int port;
    private Channel channelHandler;

    public RpcClient(String address, int port) {
        this.address=address;
        this.port=port;
    }

    public Response send(Request request) throws InterruptedException {
       channelHandler.writeAndFlush(request);
       wait();
       return  response;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
      channelHandler =ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Response o) throws Exception {
        response=o;
        notify();
    }
    /*
     ClientBootstrap bootstrap = new ClientBootstrap();
        ExecutorService boss = Executors.newCachedThreadPool();
        ExecutorService work = Executors.newCachedThreadPool();
        bootstrap.setFactory(new NioClientSocketChannelFactory(boss,work));
         bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
            public ChannelPipeline getPipeline() throws Exception {
                ChannelPipeline pipeline = Channels.pipeline();
                pipeline.addLast("decoder",new ResponseDecoder());
                pipeline.addLast("encoder",new RequestEncoder());
                pipeline.addLast("handler",RpcClient.this);
                return pipeline;
            }
        });
         ChannelFuture connect = bootstrap.connect(new InetSocketAddress(address, port)).sync();
        connect.getChannel().write(request).sync();
         synchronized (obj){
            obj.wait();
        }
        connect.getChannel().close().sync();
        return this.response;
     */

   void  init(){
    NioEventLoopGroup workGroup  =  new NioEventLoopGroup();
    Bootstrap bootstrap =new Bootstrap();
    bootstrap.group(workGroup).channel(NioSocketChannel.class).
            handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) {
                }
            });
}
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
                Channel channel =( (ChannelFuture)future).channel();
                // 连接成功之后，启动控制台线程
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 本次重连的间隔
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");
                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit
                        .SECONDS);
            }
        });
    }

}
