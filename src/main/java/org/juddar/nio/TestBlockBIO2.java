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
 * 仍然是阻塞试的，但是添加了给客户端的反馈
 */
public class TestBlockBIO2 {

    @Test
    public void client() throws IOException {
        // 1。获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        FileChannel inChannel = FileChannel.open(Paths.get("1.jpeg"), StandardOpenOption.READ);
        ByteBuffer buf = ByteBuffer.allocate(1024);

        while (inChannel.read(buf) != -1) {
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        //通知服务端我发完了
        sChannel.shutdownOutput();

        //接受服务端反馈
        int len = 0;
        while ((len = sChannel.read(buf)) != -1){
            buf.flip();
            System.out.println(new String(buf.array(), 0, len));
            buf.clear();
        }

    }

    @Test
    public void server() throws IOException {
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        ssChannel.bind(new InetSocketAddress(9898));
        SocketChannel sChannel = ssChannel.accept();
        FileChannel outChannel = FileChannel.open(Paths.get("2.jpeg"),StandardOpenOption.WRITE,StandardOpenOption.CREATE);
        ByteBuffer buf = ByteBuffer.allocate(1024);

        while (sChannel.read(buf) != -1) {
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }

        //告诉客户端我接收完了
        sChannel.shutdownInput();
        //给客户端反馈

        ByteBuffer outBuf = buf.put("   Server had received the message!".getBytes());
        outBuf.flip();
        sChannel.write(outBuf);

    }
}
