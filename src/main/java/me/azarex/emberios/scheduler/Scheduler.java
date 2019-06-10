package me.azarex.emberios.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {

    private final ScheduledExecutorService asyncExecutor = Executors.newSingleThreadScheduledExecutor();

    public void runAsyncTask(Runnable task) {
        asyncExecutor.execute(task);
    }

    public void runAsyncTaskLater(Runnable task, long delay, TimeUnit unit) {
        asyncExecutor.schedule(task, delay, unit);
    }

    public void runRepeatingAsyncTask(Runnable task, long delay, TimeUnit unit) {
        asyncExecutor.scheduleAtFixedRate(task, 0L, delay, unit);
    }

}
