package org.juddar.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;

/**
 * 1。用于源节点与目标节点的连接。负责缓冲区中数据的传输，本身不存储数据，需要配合缓冲区来完成操作。好像是铁路
 * 2。FileChannel 从文件中读数据，用于本地文件
 * DatagramChannel UDP
 * SocketChannel TCP
 * ServerSocketChannel
 * <p>
 * 3。获取通道  FileInputStream/FileOutputStream/RandomAccessFile/   Socket/ServerSocket/DatagramSocket .getChannel()
 * jdk 1.7后有个静态方法， open（）
 * Files 工具类， newByteChannel()
 */
public class TestChannel {

    //直接缓冲区的方式
    @Test
    public void test3() throws IOException {
        FileChannel openChannel = FileChannel.open(Paths.get("1.jpeg"), StandardOpenOption.READ);
        FileChannel openChannel2 = FileChannel.open(Paths.get("3.jpeg"), StandardOpenOption.WRITE,StandardOpenOption.READ, StandardOpenOption.CREATE);
        openChannel.transferTo(0,openChannel.size(), openChannel2);
        openChannel2.transferFrom(openChannel, 0, openChannel.size());
    }

    //直接缓冲区的方式
    @Test
    public void test2() throws IOException {
        FileChannel openChannel = FileChannel.open(Paths.get("1.jpeg"), StandardOpenOption.READ);
        FileChannel openChannel2 = FileChannel.open(Paths.get("3.jpeg"), StandardOpenOption.WRITE,StandardOpenOption.READ, StandardOpenOption.CREATE);

        //直接缓冲区的方式，只有字节buf支持
        MappedByteBuffer mappedByteBuffer = openChannel.map(FileChannel.MapMode.READ_ONLY, 0, openChannel.size());
        MappedByteBuffer outByteBuffer = openChannel2.map(FileChannel.MapMode.READ_WRITE, 0, openChannel.size());

        byte[] dsf = new byte[mappedByteBuffer.limit()];
        mappedByteBuffer.get(dsf);
        outByteBuffer.put(dsf);
    }

    //利用通道完成文件的复制
    @Test
    public void test() throws IOException {
        FileInputStream fs = new FileInputStream("1.jpeg");

        FileOutputStream outputStream = new FileOutputStream("2.jpeg");

        FileChannel channel = fs.getChannel();
        FileChannel channel1 = outputStream.getChannel();

        ByteBuffer buf = ByteBuffer.allocate(1024);
        //通道从缓冲区读
        while (channel.read(buf) != -1) {
            buf.flip();//将缓冲区切换成写
            channel1.write(buf); //读到
            buf.clear();

        }

        channel.close();
        channel1.close();
        fs.close();
        outputStream.close();

    }
}
