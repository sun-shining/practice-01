package org.juddar.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * 一 完成网络通信的三个核心
 * 1。通道
 *          Channel
 *              |-- SelectableChannel
 *                  |-- SocketChannel
 *                  |-- ServerSocketChannel
 *                  |-- DatagramChannel
 *
 * 2。缓冲区
 * 3。选择器 用于监控selectableChannel的IO状况
 */
public class TestBolockNIO {

    @Test
    public void client() throws IOException {
        // 1。获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        //2。从本地读取一个文件到buf
        FileChannel inChannel = FileChannel.open(Paths.get("1.jpeg"), StandardOpenOption.READ);

        ByteBuffer buf = ByteBuffer.allocate(1024);

        //3。通道socketChannel发送出去
        while (inChannel.read(buf) != -1) {
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        inChannel.close();
        sChannel.close();

    }

    @Test
    public void server() throws IOException {
        //1.获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        //2。绑定连接
        ssChannel.bind(new InetSocketAddress(9898));

        //3。获取客户端连接的通道
        SocketChannel sChannel = ssChannel.accept();

        //4接收客户端的数据，并保存到本地
        FileChannel outChannel = FileChannel.open(Paths.get("2.jpeg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        ByteBuffer buf = ByteBuffer.allocate(1024);

        while (sChannel.read(buf) != -1) {
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }
    }
}
