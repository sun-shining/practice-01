package org.juddar.nio;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.Scanner;

public class TestDatagramChannel {

    public static void main(String[] args) throws IOException {
        DatagramChannel dChannel = DatagramChannel.open();
        dChannel.configureBlocking(false);
//        dChannel.bind(new InetSocketAddress("127.0.0.1",9899));  UDP不用帮IP？

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String str = scanner.next();

            ByteBuffer buf = ByteBuffer.allocate(1024);
            buf.put((LocalTime.now().toString()+"\n"+str).getBytes());
            buf.flip();
            dChannel.send(buf, new InetSocketAddress("127.0.0.1",9899));
            buf.clear();
        }

        dChannel.close();
    }

    @Test
    public void server() throws IOException {
        DatagramChannel dChannel = DatagramChannel.open();
        dChannel.configureBlocking(false);

        dChannel.bind(new InetSocketAddress(9899));

        Selector selector = Selector.open();
        dChannel.register(selector, SelectionKey.OP_READ);

        while (selector.select() > 0) {
            Iterator<SelectionKey> it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey sk = it.next();
                if (sk.isReadable()){
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    dChannel.receive(buf);
                    buf.flip();
                    System.out.println(new String(buf.array(), 0, buf.limit()));
                    buf.clear();
                }
            }
            it.remove();
        }
    }
}
