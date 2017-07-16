package com.wooyoo.learning.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 复合缓冲区，常用于优化性能，即实现用户态的零拷贝操作，详见 http://www.cnblogs.com/xys1228/p/6088805.html
 */
public class CompositeBufTest {
    public static void main(String[] args) {
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        ByteBuf heapBuf = Unpooled.buffer();
        ByteBuf directBuf = Unpooled.directBuffer();
        heapBuf.writeCharSequence("Hello", Charset.defaultCharset());
        directBuf.writeCharSequence("World", Charset.defaultCharset());

        compositeByteBuf.addComponents(true, heapBuf, directBuf); // 这里一定要加true，否则compositeByteBuf是读取不到数据的

        int length = compositeByteBuf.readableBytes();
        byte[] arr = new byte[length];
        compositeByteBuf.getBytes(compositeByteBuf.readerIndex(), arr);
        System.out.println(new String(arr)); // 读取到了所有components的数据，即"HelloWorld"
    }
}
