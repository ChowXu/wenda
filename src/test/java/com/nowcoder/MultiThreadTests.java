package com.nowcoder;

import java.util.Objects;
import java.util.concurrent.*;

/**
 * Project: wenda
 * Author: Chow xi
 * Email: zhouxu_1994@163.com
 * Time: 17/2/15 下午2:39
 */
class MyThread extends Thread {
    private int tid;

    public MyThread(int tid) {
        this.tid = tid;

    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; ++i) {
                Thread.sleep(1000);
                System.out.println(String.format("%d: %d", tid, i));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


public class MultiThreadTests {
    public static void testThread() {
//        for (int i = 0; i < 10; ++i) {
//            new MyThread(i).start();
//        }

        for (int i = 0; i < 10; ++i) {
            final int fid = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (int i = 0; i < 10; ++i) {
                            Thread.sleep(1000);
                            System.out.println(String.format("%d: %d", fid, i));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    public static void main(String[] args) {
        //1. 启动一个线程
//        testThread();

        //2. 内置锁
//        testInnerLock();

        //3. blocking queu
//        testBlockingQueue();
//        testThreadLocal();

        //testExecutor();
        testFuture();
    }

    private static void testFuture() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> result = service.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(3000);
                return 1;
            }
        });
        service.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println(2);
            }
        });
        try {
            System.out.println(result.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }


    private static void testExecutor() {
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("executor1");

            }
        });

        service.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("executor2");
            }
        });

        service.shutdown();
        while (!service.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    private static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal() {
        for (int i = 0; i < 10; ++i) {
            final int fid = i;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    threadLocalUserIds.set(fid);
                    userId = fid;
                    try {
                        Thread.sleep(1000);
                        System.out.println(String.format("ThreadLocal:%d %d", fid, threadLocalUserIds.get()));
                        System.out.println(String.format("userId:%d ", userId));

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }

    }

    private static void testInnerLock() {
        try {
            for (int i = 0; i < 10; ++i) {
                testInnerLock1();
                testInnerLock2();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object obj = new Object();


    private static void testInnerLock1() {
        synchronized (obj) {

            try {
                for (int i = 0; i < 10; ++i) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T3: %d", i));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static void testInnerLock2() {
        synchronized (obj) {

            try {
                for (int i = 0; i < 10; ++i) {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4: %d", i));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static void testBlockingQueue() {
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q), "consumer1").start();
        new Thread(new Consumer(q), "consumer2").start();
    }
}

class Consumer implements Runnable {

    private BlockingQueue<String> q;

    public Consumer(BlockingQueue<String> q) {
        this.q = q;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println(Thread.currentThread().getName() + q.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

class Producer implements Runnable {

    private BlockingQueue<String> q;

    public Producer(BlockingQueue<String> q) {
        this.q = q;
    }

    @Override
    public void run() {
        while (true) {
            try {
                for (int i = 0; i < 100; ++i) {
                    Thread.sleep(1000);
                    q.put(String.format("Blocking queue : %d", i));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}