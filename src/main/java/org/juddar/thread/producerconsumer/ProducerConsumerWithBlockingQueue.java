package org.juddar.thread.producerconsumer;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumerWithBlockingQueue {
    public static void main(String[] args) {
        Resource3 resource = new Resource3();
        //生产者线程
        ProducerThread3 p = new ProducerThread3(resource);
        //多个消费者
        ConsumerThread3 c1 = new ConsumerThread3(resource);
        ConsumerThread3 c2 = new ConsumerThread3(resource);
        ConsumerThread3 c3 = new ConsumerThread3(resource);

        p.start();
        c1.start();
        c2.start();
        c3.start();
    }

}

class ConsumerThread3 extends Thread {
    private Resource3 resource3;

    public ConsumerThread3(Resource3 resource) {
        this.resource3 = resource;
        //setName("消费者");
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (1000 * Math.random()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource3.remove();
        }
    }
}

class ProducerThread3 extends Thread{
    private Resource3 resource;
    public ProducerThread3(Resource3 resource){
        this.resource = resource;
    }

    public void  run(){
        while (true){
            try {
                Thread.sleep((long) (1000*(Math.random())));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.add();

        }
    }
}

class Resource3{
    private BlockingQueue queue = new LinkedBlockingQueue(10);

    public void add(){
        try {
            queue.put(1);
            System.out.println("生产者" + Thread.currentThread().getName()
                    + "生产一件资源," + "当前资源池有" + queue.size() +
                    "个资源");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void remove(){
        try {
            queue.take();
//            queue.remove();//remove  方法当队列起始为空时会抛NoSuchElementException异常
            System.out.println("消费者" + Thread.currentThread().getName() +
                    "消耗一件资源," + "当前资源池有" + queue.size()
                    + "个资源");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}