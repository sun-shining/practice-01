package org.juddar.thread;

import java.lang.management.ManagementFactory;

//这种使用wait和notify的方式不对
public class ShareApple2 implements Runnable{

    private char c = 'a';


    public static void main(String[] args) {
        ShareApple2 sp = new ShareApple2();

        Thread t1 = new Thread(sp);
        Thread t2 = new Thread(sp);
        t1.setName("Jack");
        t2.setName("Tom");
        t2.start();
        t1.start();

        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);//name的值：1051@MacBook-Pro-4.local
// get pid
        String pid = name.split("@")[0];
        System.out.println("Pid is:" + pid);
    }

    @Override
    public  void run() {
        while (c <= 'z') {
            print();
        }
    }

    public synchronized void print(){
        if (c<='z') {
            System.out.println(Thread.currentThread().getName()+":"+c);
            c++;
            notify();
            if (c < 'z'){
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
