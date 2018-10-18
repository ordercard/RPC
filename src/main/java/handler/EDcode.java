package handler;

import code.Serlaizes;
import entry.Packet;
import entry.Request;
import entry.Response;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;


public class EDcode {

    public static  final int MAC_NUMBER = 0x12345678;
    public static final EDcode INSTANCE = new EDcode();
    public  static final Serlaizes  serialize =new Serlaizes();


    private static final Map<Byte,Class<? extends Packet>> map ;
    static {

        map= new HashMap<>();
        map.put((byte)1, Request.class);
        map.put((byte)2, Response.class);

    }

    public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {

        ByteBuf byteBuf = byteBufAllocator.ioBuffer();

        byte[] bytes = Serlaizes.<Packet>serialize(packet);

        byteBuf.writeInt(MAC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(1);
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        return  byteBuf;
    }

    public void encode(ByteBuf byteBuf, Packet packet) {
        // 1. 序列化 java 对象
        byte[] bytes = Serlaizes.<Packet>serialize(packet);

        // 2. 实际编码过程
        byteBuf.writeInt(MAC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(1);
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    public Packet decode(ByteBuf byteBuf) {
        //    System.out.println("可读字节数"+byteBuf.readableBytes());

        byteBuf.skipBytes(4);
        byteBuf.skipBytes(1);

        byte  seralthm =byteBuf.readByte();
        byte  com =byteBuf.readByte();
        int length =byteBuf.readInt();
        byte[] bytes =  new byte[length];
        byteBuf.readBytes(bytes);
        Class<? extends Packet> rtype = getCommandType(com);
        if (rtype != null ){
            return Serlaizes.deserialize(bytes,rtype);
        }
        return    Serlaizes.deserialize(bytes,Packet.class);
    }


    private Class<? extends Packet> getCommandType(byte com) {
        return map.get(com);
    }
}

