package org.juddar.thread;

/**
 * 一。线程的生命周期：New--》Start--》Run-》Blocking--》Dead
 * 使用sleep或join，可以使线程进入blocking状态，sleep超时，interrupt可使线程恢复执行状态，join后别的线程执行完了
 *
 */
public class MyRunnable implements Runnable{
    int i = 0;

    public int getI() {
        return i;
    }

    @Override
    public void run() {

            for (; i < 100; i++) {
                System.out.println(Thread.currentThread().getName()+":"+i);

            }


    }

    public static void main(String[] args) throws InterruptedException {
        MyRunnable rm = new MyRunnable();

        //1.当i是线程执行体内的私有变量时，启动多个线程，各自打印私有变量
        //如是：for (int i = 0; i < 10; i++)
        /*Thread t1 = new Thread(rm);
        Thread t2 = new Thread(rm);*/

        //2。将i变成runnable实现类的局部变量，则可以让多个线程共享同一个变量，即实现多个线程交替打印相应数字
        Thread t1 = new Thread(rm);

        Thread t2 = new Thread(rm);
        t1.start();
        t2.start();

    }
}
