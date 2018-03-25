package org.juddar.thread;

import java.lang.management.ManagementFactory;

//这种使用wait和notify的方式不对,应该是去掉else， 在if前面加个notifyAll
public class ShareApple implements Runnable{

    private int appleCount = 5;
    boolean flag = true;
    synchronized boolean getApple(){

            if (appleCount > 0){
                appleCount--;


                System.out.println(Thread.currentThread().getName() +"拿走了一个苹果.还剩" + appleCount + " 个苹果");
                if (flag){
                    flag = false;
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }else{
                    flag = true;
                    this.notifyAll();

                }
                return true;
            }
            return false;
    }

    @Override
    public void run() {
        boolean flag = getApple();
        while (flag){

            flag = getApple();
        }
        System.out.println(Thread.currentThread().getName()+"线程结束了。");
    }


    public static void main(String[] args) {
        ShareApple sp = new ShareApple();

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
}
