package com.wooyoo.learning.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 使用直接缓冲区（Direct Buffer），不占用堆空间，性能更高
 * 与堆缓冲不一样，这里底层用的是jdk nio包下的DirectByteBuffer类
 */
public class DirectBufTest {
    public static void main(String[] args) {
        ByteBuf directBuf = Unpooled.directBuffer();
        directBuf.writeCharSequence("Hello", Charset.defaultCharset());

        int length = directBuf.readableBytes(); // 获取可读字节数
        byte[] arr = new byte[length];
        directBuf.getBytes(directBuf.readerIndex(), arr); // 将字节复制到该数组

        System.out.println(new String(arr)); // 查看是否是Hello
    }
}
