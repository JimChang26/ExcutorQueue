package queue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ExcutorQueue<T> {

    private boolean run = true;
    private int poolSize;
    private String threadName;

    private ConcurrentHashMap<Integer, LinkedBlockingDeque<T>> queueMap = new ConcurrentHashMap<Integer, LinkedBlockingDeque<T>>();

    private ExecutorService executorService;

    protected ExcutorQueue(String threadName, int poolSize) {
        this.poolSize = poolSize;
        this.threadName = threadName;
        init();
    }

    private void init() {
        executorService = Executors.newFixedThreadPool(poolSize);
        for (int i = 0; i < poolSize; i++) {
            LinkedBlockingDeque<T> queue = new LinkedBlockingDeque<>();
            int index = i;
            queueMap.put(i, queue);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    Thread.currentThread().setName(threadName + "-Thread-" + index);
                    System.out.println(
                            Thread.currentThread().getName() + " is create! And it will handel Queue-" + index);
                    try {
                        task(queue);
                    } catch (InterruptedException e) {
                        if (run) {
                            e.printStackTrace();
                            throw new ExcutorQueueException("task unknow error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new ExcutorQueueException("task unknow error");
                    } finally {
                        System.out.println(Thread.currentThread().getName() + " shutdown!");
                    }
                }
            });
        }
    }

    protected void shutdownUntilFinishAllJobs(int timeout, TimeUnit timeUnit) {
        run = false;
        executorService.shutdown();
        try {
            executorService.awaitTermination(timeout, timeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            executorService.shutdownNow();
        }
    }

    protected void shutdownNow() {
        run = false;
        executorService.shutdownNow();
    }

    protected void put(int index, T T) {
        try {
            queueMap.get(index % poolSize).put(T);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExcutorQueueException("put unknow error");
        }
    }

    protected void putFirst(int index, T T) {
        try {
            queueMap.get(index % poolSize).putFirst(T);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExcutorQueueException("put first unknow error");
        }
    }

    protected void task(LinkedBlockingDeque<T> queue) throws Exception {
        while (run || !queue.isEmpty()) {
            execute(queue.takeFirst());
        }
    }

    protected abstract void execute(T t) throws InterruptedException;

}
