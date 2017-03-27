package com.application.threads;

import static java.lang.Thread.sleep;

public class ThreadExampleTwo {
    public class Lock {
        int total=0;
    }

    Lock lock = new Lock();
    boolean notified = false;

    public static void main(String[] args) {
        new ThreadExampleTwo().startAll();
    }

    public void startAll() {
        Thread t1 = new Thread(r1);
        t1.setName("Thread One");
        t1.start();

        Thread t2 = new Thread(r1);
        t2.setName("Thread Two");
        t2.start();

        for (int i = 0; i < 1000000000; i++) {
            // Just wait
        }

        Thread t3 = new Thread(r2);
        t3.setName("Thread Three");
        t3.start();
    }

    Runnable r1 = new Runnable() {
        @Override
        public void run() {
            synchronized (lock) {
                try {
                    System.out.println("Waiting.");
//                    lock.wait();
                    while (!notified) {
                        ObjWrapper.wait(lock);
                    }
                    lock.total *=2;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(lock.total);
            }
        }
    };

    Runnable r2 = new Runnable() {
        @Override
        public void run() {
            synchronized(lock) {
                for (int i = 0; i < 1000; i++) {
                    lock.total++;
                }
//                lock.notifyAll();
                ObjWrapper.notifyAll(lock);
                notified = true;
                System.out.println( "Notified");
            }
        }
    };
}
