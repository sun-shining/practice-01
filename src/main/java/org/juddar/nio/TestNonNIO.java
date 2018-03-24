package org.juddar.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * 非阻塞试
 */
public class TestNonNIO {
    public static void main(String[] args) throws IOException {
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        sChannel.configureBlocking(false);

        ByteBuffer buf = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String str = scanner.nextLine();
            buf.put((LocalTime.now().toString()+"\n" + str).getBytes());

            buf.flip();
            sChannel.write(buf);
            buf.clear();

        }

        sChannel.close();
    }

    @Test
    public void client() throws IOException {
        //1。打开通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        //2。将通道转换成非阻塞试
        sChannel.configureBlocking(false);

        //3。从键盘接收数据写入buf并发送给服务端
        ByteBuffer buf = ByteBuffer.allocate(1024);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String str = scanner.next();
            buf.put((LocalTime.now().toString()+"\n" + str).getBytes());

            buf.flip();
            sChannel.write(buf);
            buf.clear();

        }


        /*int len ;
        while ((len = sChannel.read(buf)) != -1) {
            buf.flip();
            System.out.println(new String(buf.array(), 0, len));
            buf.clear();
        }*/
        sChannel.close();
    }

    @Test
    public void server() throws IOException {
        //1。打开ServerSocketChannel ，它是一个可以监听新进来的TCP连接的通道，就像标准IO里的ServerSocket一样
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //2。将通道设置成为非阻塞试
        ssChannel.configureBlocking(false);
        //3。绑定端口
        ssChannel.bind(new InetSocketAddress(9898));

        //4。获取选择器（**思想很重要：这个地方有个需要注意的就是参照ServerSocketChannel应该能想到Selector的创建也是通过open方法）
        Selector selec = Selector.open();
        //5。 将通道注册到选择器上，并指定监听事件（SelectionKey指我把这个通道注册到选择器上，想让选择器监控哪种类型的操作
        //  源码里也写了选择器其实监控到有变化时，会把对应的状态码添加到对应channel的selected-key set里）
        ssChannel.register(selec, SelectionKey.OP_ACCEPT);

        //6。遍历选择器，当监控到哪个channel准备好IO操作时，会被放入select set里
        while (selec.select() >0) {
            //7。获取选择键set
            Set<SelectionKey> selectionKeys = selec.selectedKeys();
            //8。迭代选择键
            Iterator<SelectionKey> it =selectionKeys.iterator();
            while (it.hasNext()){
                SelectionKey sk = it.next();
                //9。判断选择键的类型，此步和5里选择器监控时放入的是对应的，在这儿取出来判断，根据不同的时间类型进行具体的操作
                if (sk.isAcceptable()){
                    //10。如果是接收类型，那我可以获取到对应的socketChannel
                    SocketChannel sChannel = ssChannel.accept();
                    //11。将获取到的sChannel也转换成非阻塞试的
                    sChannel.configureBlocking(false);
                    //12。注册到选择器上，注册读操作
                    // TODO 但是这儿有个不明白的地方，它和else里获取到的SocketChannel有什么关系
                    sChannel.register(selec, SelectionKey.OP_READ);

                } else if(sk.isReadable()){
                    //13。如果是读，从里面获取channel
                    SocketChannel socketChannel = (SocketChannel) sk.channel();
                    ByteBuffer buf = ByteBuffer.allocate(1024);

                    //14。可以肆意妄为了
                    int len;
                    while ((len = socketChannel.read(buf)) > 0) {
                        buf.flip();
                        System.out.println(new String(buf.array(), 0, len));
                        buf.clear();
                    }
                }
                //15。使用过的迭代器要移除
                it.remove();

            }

        }




    }
}
