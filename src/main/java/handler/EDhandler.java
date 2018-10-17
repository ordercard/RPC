package handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

@ChannelHandler.Sharable
public class EDHandler extends MessageToMessageCodec<ByteBuf, Packet> {
    public static final EDHandler INSTANCE = new EDHandler();

    private EDHandler() {

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) {
        out.add(EDcode.INSTANCE.decode(byteBuf));
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) {
        ByteBuf byteBuf = ctx.channel().alloc().ioBuffer();
        EDcode.INSTANCE.encode(byteBuf, packet);
        out.add(byteBuf);
    }
}
