package com.wooyoo.learning.netty.access;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ByteProcessor;

import java.nio.charset.Charset;

public class SearchTest {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeCharSequence("Hello", Charset.defaultCharset());

        // 使用indexOf方法来查找
        int idx1 = byteBuf.indexOf(0, byteBuf.readableBytes(), (byte) 'o');
        System.out.println(idx1);

        // 使用processor来查找
        int idx2 = byteBuf.forEachByte(new ByteProcessor() {
            public boolean process(byte value) throws Exception {
                return value != (byte) 'o'; // 返回true表示继续迭代，返回false表示停止迭代
            }
        });
        System.out.println(idx2);
    }
}
