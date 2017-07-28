package com.wooyoo.learning.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {
    private final InetSocketAddress remoteAddress;

    public LogEventEncoder(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogEvent logEvent, List<Object> out) throws Exception {
        byte[] file = logEvent.getLogfile()
                              .getBytes(CharsetUtil.UTF_8);
        byte[] msg = logEvent.getMsg()
                             .getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = ctx.alloc()
                         .buffer(file.length + msg.length + 1);
        buf.writeBytes(file); // 将文件名写入到ByteBuf中
        buf.writeByte(LogEvent.SEPARATOR); // 添加一个SEPARATOR
        buf.writeBytes(msg); // 将日志消息写入ByteBuf中
        out.add(new DatagramPacket(buf, remoteAddress));
    }
}
