package org.juddar.nio;

import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * 在NIO中负责数据的存取。缓冲区就是数组。
 * 根据数据类型不同，提供了相应类型的缓冲区。boolean除外，其他7种基本数据类型都有。
 *
 * capacity
 * limit
 * position 正在操作数据的位置
 * 0<=mark<=position<=limit<=capacity
 */
public class TestBuffer {

    /**
     * ByteBuffer.allocate() 用的是JVM的缓冲区，
     * ByteBuffer.allocaetDerect() 用的是物理内存中
     */
    @Test
    public void testderectBufandNoinderectBuf(){
        ByteBuffer buf = ByteBuffer.allocateDirect(1024);
        System.out.println(buf.isDirect());
    }

    @Test//mark 记录position的位置，并可以通过reset方法将position复位
    public void testbuffermark2(){
        ByteBuffer buf = ByteBuffer.allocate(1024);
        String str = "abcde";
        buf.put(str.getBytes());

        buf.flip();
        byte[] dsf = new byte[buf.limit()];
        buf.get(dsf, 0, 2);
        System.out.println(new String(dsf, 0, 2));
        System.out.println(buf.position());
        buf.mark();

        buf.get(dsf, 2, 3);

        System.out.println(new String(dsf, 2, 3));
        System.out.println(buf.position());
        buf.reset();
        System.out.println(buf.position());
        buf.rewind();
        System.out.println(buf.mark());//返回的是HeapByteBuffer对象，不是值
        System.out.println(buf.position());
        System.out.println(buf.mark());

    }



    @Test
    public void testbuffer(){
        ByteBuffer buf = ByteBuffer.allocate(1024);

        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        String str = "abcde";
        buf.put(str.getBytes());

        buf.flip();
        byte[] ds = new byte[buf.limit()];
        buf.get(ds);
        System.out.println("--------get()--------");
        System.out.println(new String(ds));
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());

        buf.rewind();//重置position 的位置
        byte[] ds2 = new byte[buf.limit()];
        buf.get(ds2);
        System.out.println("--------get2()--------");
        System.out.println(new String(ds2));
        System.out.println(buf.position());
        System.out.println(buf.limit());
        System.out.println(buf.capacity());



    }
}
