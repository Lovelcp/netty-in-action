package com.wooyoo.learning.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedStream;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;

public class ChunkedWritehandlerInitializer extends ChannelInitializer<Channel> {
    private final File file;
    private final SSLContext sslContext;

    public ChunkedWritehandlerInitializer(File file, SSLContext sslContext) {
        this.file = file;
        this.sslContext = sslContext;
    }

    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new SslHandler(sslContext.createSSLEngine())); // 将sslHandler添加到pipeline中
        pipeline.addLast(new ChunkedWriteHandler()); // 添加ChunkedWriteHandler以处理作为ChunkedInput传入的数据
        pipeline.addLast(new WriteStreamHandler()); // 一旦连接建立，WriteStreamHandler就开始写文件数据
    }

    public final class WriteStreamHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            super.channelActive(ctx);
            ctx.writeAndFlush(new ChunkedStream(new FileInputStream(file)));
        }
    }
}
