package com.wooyoo.learning.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 从Channel中引导客户端
 */
public class BootstrapFromChannel {
    public static void main(String[] args) {
        ServerBootstrap bootstrap = new ServerBootstrap(); // 创建ServerBootstrap以创建ServerSocketChannel，并绑定它
        bootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup())
                 .channel(NioServerSocketChannel.class)
                 .childHandler(new SimpleChannelInboundHandler<ByteBuf>() {
                     ChannelFuture connectFuture;

                     @Override
                     public void channelActive(ChannelHandlerContext ctx) throws Exception {
                         Bootstrap clientBootstrap = new Bootstrap(); // 创建一个Bootstrap类的实例以连接到远程主机
                         clientBootstrap.channel(NioSocketChannel.class)
                                        .handler(new SimpleChannelInboundHandler<ByteBuf>() {
                                            protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                                System.out.println("Received data");
                                            }
                                        });
                         clientBootstrap.group(ctx.channel()
                                                  .eventLoop()); // 使用与分配给已被接受的子Channel相同的EventLoop
                         connectFuture = clientBootstrap.connect(new InetSocketAddress("www.manning.com", 80)); // 连接到远程节点
                     }

                     @Override
                     protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                         if (connectFuture.isDone()) {
                             // do something with the data
                         }
                     }
                 });

        ChannelFuture future = bootstrap.bind(new InetSocketAddress(8080));
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Server bound");
                }
                else {
                    System.out.println("Bind attempt failed");
                    channelFuture.cause()
                                 .printStackTrace();
                }
            }
        });
    }
}
