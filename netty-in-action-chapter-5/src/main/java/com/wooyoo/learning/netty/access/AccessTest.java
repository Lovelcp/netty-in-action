package com.wooyoo.learning.netty.access;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class AccessTest {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.buffer(10); // 分配10个空间的堆缓冲区
        int readIdx1 = byteBuf.readerIndex();
        int writeIdx1 = byteBuf.writerIndex();
        System.out.println("一开始，read索引：" + readIdx1 + " write索引：" + writeIdx1);

        byteBuf.writeByte('H');
        byteBuf.writeByte('e');
        byteBuf.writeByte('l');
        byteBuf.writeByte('l');
        byteBuf.writeByte('o');
        int readIdx2 = byteBuf.readerIndex();
        int writeIdx2 = byteBuf.writerIndex();
        System.out.println("写了Hello后，read索引：" + readIdx2 + " write索引：" + writeIdx2);

        byteBuf.readByte();
        byteBuf.readByte();
        byte b1 = byteBuf.readByte();
        int readIdx3 = byteBuf.readerIndex();
        int writeIdx3 = byteBuf.writerIndex();
        System.out.println("3次读取后，最后一次读取的是" + (char) b1 + "，read索引：" + readIdx3 + " write索引：" + writeIdx3);

        byte b2 = byteBuf.getByte(0);
        byteBuf.discardReadBytes(); // 丢弃已读字节
        byte b3 = byteBuf.getByte(0);
        int readIdx4 = byteBuf.readerIndex();
        int writeIdx4 = byteBuf.writerIndex();
        System.out.println("我们发现，如果不丢弃已读字节，第一个字节是" + (char) b2 + "，丢弃了之后，第一个字节变成了" + (char) b3 + "，此时read索引：" + readIdx4 + " write索引：" + writeIdx4);
    }
}
