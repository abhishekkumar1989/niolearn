package com.abhishek_k.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

import static java.nio.channels.SelectionKey.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Selector selector = Selector.open();

                    ServerSocketChannel socketChannel = ServerSocketChannel.open();
                    socketChannel.configureBlocking(false);
                    socketChannel.socket().bind(new InetSocketAddress(9999));
                    socketChannel.register(selector, OP_ACCEPT);

                    DatagramChannel udpChannel = DatagramChannel.open();
                    udpChannel.configureBlocking(false);
                    udpChannel.socket().bind(new InetSocketAddress(8888));
                    udpChannel.register(selector, OP_READ);

                    while (true) {
                        int selected = selector.select();
                        if (selected > 0) {
                            Set<SelectionKey> selectionKeys = selector.selectedKeys();
                            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                            while (keyIterator.hasNext()) {
                                SelectionKey key = keyIterator.next();
                                if (key.isAcceptable()) {
                                    SocketChannel socket = socketChannel.accept();
                                    socket.write(ByteBuffer.allocate(40).put("ABC".getBytes()));
                                    socket.close();
                                } else if (key.isReadable()) {
                                    SocketAddress receive = udpChannel.receive(ByteBuffer.allocate(10));

                                    int i = 0;
//                                    udpChannel.

                                }
                                keyIterator.remove();
                            }
                        }
                    }
                } catch (IOException e) {
                    System.out.println("APP: Exception in listening: " + e.getMessage());
                }

            }
        }).start();

        Thread.sleep(1000);

        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.connect(new InetSocketAddress(8888));
        datagramChannel.read(ByteBuffer.allocate(10));
//        int udpBytesSent = datagramChannel.send(ByteBuffer.allocate(1), new InetSocketAddress(8888));
//        System.out.println("APP: UDP bytes read: " + udpBytesSent);


//        SocketChannel socketChannel = SocketChannel.open();
//        socketChannel.connect(new InetSocketAddress(9999));
//        ByteBuffer buffer = ByteBuffer.allocate(100);
//        int bytesRead = socketChannel.read(buffer);
//        buffer.flip();
//        while (buffer.hasRemaining()) {
//            System.out.println("APP: Connection complete: " + buffer.get());
//        }
        int i = 0;
    }

}
