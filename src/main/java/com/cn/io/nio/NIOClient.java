package com.cn.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @autor: lvbiao
 * @version: 1.0
 * @descript:
 * @date: 2019-10-05 18:14
 */
public class NIOClient {

    private SocketChannel socketChannel;
    private ByteBuffer send = ByteBuffer.allocate(1024);
    private ByteBuffer receive = ByteBuffer.allocate(1024);

    public NIOClient() throws IOException {
        socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8080));
    }

    private void send() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String content = null;
        while((content =scanner.nextLine()) != null){
            send.put(content.getBytes());
            send.flip();
            while(send.hasRemaining()){
                socketChannel.write(send);
            }
            send.clear();
        }
    }

    private void receive() throws IOException {
        receive.clear();
        socketChannel.read(receive);
        System.out.println("服务器：" + new String(receive.array()));
    }

    private void close() throws IOException {
        if(socketChannel != null && socketChannel.isOpen()){
            socketChannel.close();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        NIOClient client = new NIOClient();
        Thread receive = new Thread(() -> {
            try {
                client.receive();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread send = new Thread(() -> {
            try {
                client.send();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        receive.start();
        send.start();
        receive.join();
        send.join();
        client.close();
    }
}
