package com.wooyoo.learning.netty;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * 未使用Netty的阻塞网络编程
 */
public class PlainOioServer {
    public void serve(int port) throws IOException {
        final ServerSocket socket = new ServerSocket(port);
        while (true) {
            final Socket clientSocket = socket.accept();
            System.out.println("Accepted connection from " + clientSocket);
            new Thread(() -> {
                try (OutputStream out = clientSocket.getOutputStream()) {
                    out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
                    out.flush();
                    clientSocket.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
