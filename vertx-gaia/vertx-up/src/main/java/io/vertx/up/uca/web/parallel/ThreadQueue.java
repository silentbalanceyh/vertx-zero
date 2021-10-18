package io.vertx.up.uca.web.parallel;

import io.vertx.up.fn.Actuator;
import io.vertx.up.log.Annal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ThreadQueue {

    private static final Annal LOGGER = Annal.get(ThreadQueue.class);
    private final CountDownLatch counter;
    private final List<Thread> threads = new ArrayList<>();

    public ThreadQueue(final int size) {
        this.counter = new CountDownLatch(size);
    }

    public void add(final Actuator runnable,
                    final String name) {
        final Thread thread = new ThreadAtom(this.counter, runnable);
        thread.setName(name);
        this.threads.add(thread);
    }

    public void startSync() {
        this.startAsync();
        try {
            this.counter.await();
        } catch (final InterruptedException ex) {
            LOGGER.jvm(ex);
        }
    }

    private void startAsync() {
        for (final Thread thread : this.threads) {
            thread.start();
        }
    }
}
