package org.juddar.thread;

/**
 * 在一个线程内调用另一个线程的join方法，则给那个线程让路，等待别的线程执行完毕才执行。
 */
public class TestThreadJoin extends Thread{

    public void run(){
        for (int i = 0; i < 100000; i++) {
            System.out.println(Thread.currentThread().getName()+":"+i);
        }

    }

    public static void main(String[] args) throws InterruptedException {
        TestThreadJoin t = new TestThreadJoin();

        t.start();
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName()+":"+i);
            if (i==10){
                t.join();//等待这个线程死亡
            }
        }
    }
}
