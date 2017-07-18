package com.wooyoo.learning.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * -1 -2 -3 =======> 1 2 3
 */
public class AbsIntegerEncoder extends MessageToMessageEncoder<ByteBuf> {
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        while (msg.readableBytes() >= 4) { // 检查是否有足够的字节用来编码，一个整数占用4个字节
            int value = Math.abs(msg.readInt()); // 从输入的ByteBuf中读取下一个整数，并且计算绝对值
            out.add(value); // 将该整数写入到编码消息的List中
        }
    }
}
