package com.wooyoo.learning.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

/**
 * 使用JVM的堆（heap）缓冲区
 * 底层是内部维护了一个字节数组
 */
public class BackingArrayTest {
    public static void main(String[] args) {
        ByteBuf heapBuf = Unpooled.buffer();
        heapBuf.writeCharSequence("Hello", Charset.defaultCharset());
        if (heapBuf.hasArray()) { // hasArray如果为true，则表示用的是堆缓冲区
            byte[] arr = heapBuf.array();
            for (byte b : arr) {
                if (b != 0) {
                    System.out.println((char) b);
                }
            }
        }
    }
}
