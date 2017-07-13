package com.wooyoo.learning.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

/**
 * 未使用Netty的异步网络编程
 */
public class PlainNioServer {
    public void serve(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        ServerSocket serverSocket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocket.bind(address); // 将服务器绑定到选定的端口

        Selector selector = Selector.open(); // 打开Selector处理Channel
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // 将ServerSocket注册到Selector以接受连接
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());

        while (true) {
            try {
                selector.select(); // 等待需要处理的新事件；阻塞，将一直持续到下一个传入事件
            }
            catch (Throwable t) {
                t.printStackTrace();
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys(); // 获取所有接收事件的SelectionKey实例
            for (SelectionKey selectionKey : readyKeys) {
                try {
                    // 检查事件是否是一个新的已经就绪可以被接受的连接
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate()); // 接受客户端，并将它注册到选择器
                        System.out.println("Accepted connection from " + client);
                    }

                    // 检查套接字是否已经准备好写数据
                    if (selectionKey.isWritable()) {
                        SocketChannel client = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
                        while (buffer.hasRemaining()) {
                            if (client.write(buffer) == 0) {
                                // 将数据写到已连接的客户端
                                break;
                            }
                        }
                        client.close();
                    }
                }
                catch (Throwable t) {
                    selectionKey.cancel();
                    try {
                        selectionKey.channel()
                                    .close();
                    }
                    catch (Throwable t1) {
                        // ignore on close
                    }
                }
            }
        }
    }
}
