package com.wooyoo.learning.netty.access;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class CopyTest {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Hello World", Charset.defaultCharset());
        ByteBuf sliced = byteBuf.slice(0, 2); // 采用slice的方式获取从0开始，长度为2的一个切片
        ByteBuf copy = byteBuf.copy(0, 2); // 采用copy的方式获取从0开始，长度为2的一个切片

        // slice test
        System.out.println(sliced.toString(Charset.defaultCharset()));
        sliced.setByte(0, (byte) 'h');
        System.out.println((char) byteBuf.getByte(0)); // slice生成的byteBuf会共享内存，所以slice的改变会影响原有的byteBuf

        // copy test
        System.out.println(copy.toString(Charset.defaultCharset()));
        System.out.println((char) copy.getByte(0)); // 可以看到通过copy产生的byteBuf是不会受到slice的影响的，即是独立的一份内存空间

    }
}
