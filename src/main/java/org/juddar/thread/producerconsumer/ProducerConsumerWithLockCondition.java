package org.juddar.thread.producerconsumer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumerWithLockCondition {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition producerCondition = lock.newCondition();
        Condition consumerCondition = lock.newCondition();

        Resource2 resource = new Resource2(lock, producerCondition, consumerCondition);
        ProducerThread2 producerThread2 = new ProducerThread2(resource);
        ConsumerThread2 consumerThread2 = new ConsumerThread2(resource);
        producerThread2.start();
        consumerThread2.start();
    }


}

/**
 * 消费者线程
 */
class ConsumerThread2 extends Thread {
    private Resource2 resource;

    public ConsumerThread2(Resource2 resource) {
        this.resource = resource;
        setName("消费者");
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (1000 * Math.random()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.remove();
        }
    }
}

/**
 * 生产者线程
 *
 * @author tangzhijing
 */
class ProducerThread2 extends Thread {
    private Resource2 resource;

    public ProducerThread2(Resource2 resource) {
        this.resource = resource;
        setName("生产者");
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (1000 * Math.random()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource.add();
        }
    }
}


class Resource2 {

    private int num = 0;
    private int sum = 10;

    private Lock lock;
    private Condition producerCondition;
    private Condition consumerCondition;

    public Resource2(Lock lock, Condition producerCondition, Condition consumerCondition) {
        this.lock = lock;
        this.producerCondition = producerCondition;
        this.consumerCondition = consumerCondition;
    }

    public void add() {
        lock.lock();
        try {
            if (num < sum) {
                num++;
                System.out.println(Thread.currentThread().getName() +
                        "生产一件资源,当前资源池有" + num + "个");
                consumerCondition.signalAll();
            } else {
                try {
                    producerCondition.await();//97行和120行只有97行是wait和await都可以的，切97是wait，120行必须是await
                    System.out.println(Thread.currentThread().getName() + "线程进入等待");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void remove() {
        lock.lock();

        try {
            if (num > 0) {//if (sum < 10) 这么写，sum是常数10，则消费者永远在等待状态
                num--;
                System.out.println("消费者" + Thread.currentThread().getName()
                        + "消耗一件资源," + "当前资源池有" + num + "个");
                producerCondition.signalAll();//唤醒等待的生产者接着生产

            } else {
                try {
                    consumerCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            lock.unlock();
        }

    }

}