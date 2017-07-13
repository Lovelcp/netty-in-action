package com.wooyoo.learning.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * 使用Netty的异步网络处理
 */
public class NettyNioServer {
    public void server(int port) throws InterruptedException {
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(group)
                           .channel(NioServerSocketChannel.class) // 使用Nio
                           .localAddress(new InetSocketAddress(port))
                           .childHandler(new ChannelInitializer<SocketChannel>() {
                               @Override
                               protected void initChannel(SocketChannel ch) throws Exception {
                                   ch.pipeline()
                                     .addLast(new ChannelInboundHandlerAdapter() {
                                         @Override
                                         public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                             ctx.writeAndFlush(buf.duplicate())
                                                .addListener(ChannelFutureListener.CLOSE);
                                         }
                                     });
                               }
                           });
            ChannelFuture future = serverBootstrap.bind()
                                                  .sync();
            future.channel()
                  .closeFuture()
                  .sync();
        }
        finally {
            group.shutdownGracefully()
                 .sync();
        }
    }

}
