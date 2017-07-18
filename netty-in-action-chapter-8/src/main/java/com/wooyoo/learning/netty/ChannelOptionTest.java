package com.wooyoo.learning.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.net.InetSocketAddress;

/**
 * 使用ChannelOption以及AttributeKey
 */
public class ChannelOptionTest {
    public static void main(String[] args) {
        final AttributeKey<Integer> id = AttributeKey.newInstance("ID"); // 创建一个AttributeKey以标识该属性
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                 .channel(NioSocketChannel.class)
                 .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                     @Override
                     public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                         Integer idValue = ctx.channel()
                                              .attr(id)
                                              .get();
                         System.out.println(idValue);
                     }

                     protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                         System.out.println("Received Data");
                     }
                 });
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true)
                 .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        bootstrap.attr(id, 123456);
        ChannelFuture future = bootstrap.connect(new InetSocketAddress("www.manning.com", 80));
        future.syncUninterruptibly();
    }
}
