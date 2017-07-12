package com.wooyoo.learning.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class EchoServer {
    private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            System.err.println("Usage: " + EchoServer.class.getSimpleName() + " <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }

    private void start() throws InterruptedException {
        final EchoServerHandler serverHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup(); // 使用NIO传输，所以创建NioEventLoopGroup

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                     .channel(NioServerSocketChannel.class) // 指定所使用的NIO传输Channel
                     .localAddress(new InetSocketAddress(port)) // 使用指定的端口设置套接字地址
                     .childHandler(new ChannelInitializer<SocketChannel>() { // 添加一个EchoServerHandler到子Channel的ChannelPipeline
                         // 每当一个新的连接被接受时，一个子Channel将会被创建，而ChannelInitializer会把EchoServerHandler实例添加到该Channel的ChannelPipeline中
                         protected void initChannel(SocketChannel ch) throws Exception {
                             ch.pipeline()
                               .addLast(serverHandler); // 由于EchoServerHandler被标记Shareable，所以所有的客户端连接都会使用同一个handler
                         }
                     });
            ChannelFuture future = bootstrap.bind()
                                            .sync(); // 异步地绑定服务器，调用sync方法阻塞等待直到绑定完成
            future.channel()
                  .closeFuture()
                  .sync(); // 获取Channel的CloseFuture，并且阻塞当前线程直到它完成
        }
        finally {
            group.shutdownGracefully()
                 .sync();
        }
    }
}
