package queue.dog;

import java.util.concurrent.TimeUnit;

import queue.ExcutorQueue;

public class DogQueue extends ExcutorQueue<DogBean> {

    int current = 0;
    int poolSize;
    String threadName;

    public DogQueue(String threadName, int poolSize) {
        super(threadName, poolSize);
        this.poolSize = poolSize;
    }

    @Override
    protected void execute(DogBean dogThings) throws InterruptedException {

        switch (dogThings.getDogAction()) {
        case SIT:
            System.out.println(Thread.currentThread().getName() + "->Dog:" + dogThings.getDogName() + " SIT!");
            break;
        case HAND:
            System.out.println(Thread.currentThread().getName() + "->Dog:" + dogThings.getDogName() + " HAND!");
            break;
        case WALK:
            System.out.println(Thread.currentThread().getName() + "->Dog:" + dogThings.getDogName() + " WALK!");
            break;
        case FAKE_DEATH:
            System.out.println(Thread.currentThread().getName() + "->Dog:" + dogThings.getDogName() + " FAKE DEATH!");
            break;
        default:
            break;
        }
        Thread.sleep(1000);
    }

    public void sit(String dogName) {
        super.put(current++, new DogBean(DogAction.SIT, dogName));
    }

    public void walk(String dogName) {
        super.put(current++, new DogBean(DogAction.WALK, dogName));
    }

    public void done() {
        super.shutdownUntilFinishAllJobs(100, TimeUnit.SECONDS);
    }
}
