package org.juddar.thread.producerconsumer;

/**
 *  Q：第二行，当生产者线程已经生产了一个资源，但是消费者扔处于等待状态的原因？
 Thread-1生产一件资源，当前资源池有1个
 消费者Thread-0线程进入等待状态
 Thread-1生产一件资源，当前资源池有2个
 消费者Thread-0消耗一件资源，当前线程池有1个
 Thread-1生产一件资源，当前资源池有2个
 消费者Thread-0消耗一件资源，当前线程池有1个
 Thread-1生产一件资源，当前资源池有2个
 消费者Thread-0消耗一件资源，当前线程池有1个
 消费者Thread-0消耗一件资源，当前线程池有0个
 Thread-1生产一件资源，当前资源池有1个
 *
 *
 */
public class ProducerConsumerWithWaitNofity {
    public static void main(String[] args) {
        Resource resource = new Resource();
        Consumer consumer = new Consumer(resource);

        Thread th1 = new Thread(consumer);
        Producer th2 = new Producer(resource);
        th1.start();
        th2.start();
    }

}
class Resource{
    private int num = 0;//资源数量
    private int size = 10;//仓库容量

    public synchronized void remove(){//同步方法使用的是类锁，是个抽象概念，与同步块里的this不是一个锁，this是对象锁
        if (num > 0){
            num --;
            System.out.println("消费者" + Thread.currentThread().getName() +
                    "消耗一件资源，" + "当前线程池有" + num + "个");
            notifyAll();
        }else{
            try {
                wait();
                System.out.println("消费者" + Thread.currentThread().getName() + "线程进入等待状态");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void add(){
        if (num < size ){
            num++;
            System.out.println(Thread.currentThread().getName() + "生产一件资源，当前资源池有"
                    + num + "个");
            notifyAll();
        }else{
            try {
                wait();
                System.out.println(Thread.currentThread().getName()+"线程进入等待");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class Consumer implements Runnable{
    private Resource resource;
    public  Consumer(Resource resource){
        this.resource = resource;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.remove();
        }
    }
}

/**
 * 生产者线程
 */
class Producer extends Thread{
    private Resource resource;
    public Producer(Resource resource){
        this.resource = resource;
    }
    @Override
    public void run() {
        //不断地生产资源
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.add();
        }
    }

}
