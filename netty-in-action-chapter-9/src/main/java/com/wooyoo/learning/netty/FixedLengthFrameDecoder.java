package com.wooyoo.learning.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * AB CD EFG HI =====> ABC DEF GHI
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {
    private final int frameLength;

    public FixedLengthFrameDecoder(int frameLength) {
        assert frameLength > 0;
        this.frameLength = frameLength;
    }

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() >= frameLength) { // 检查是否有足够的字节可以被读取，以生成下一个帧
            ByteBuf buf = in.readBytes(frameLength); // 从ByteBuf中读取一个新帧
            out.add(buf); // 将该帧添加到已被解码的消息列表中
        }
    }
}
