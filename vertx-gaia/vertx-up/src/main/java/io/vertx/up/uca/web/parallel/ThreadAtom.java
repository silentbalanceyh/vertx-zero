package io.vertx.up.uca.web.parallel;

import io.zero.spec.function.RunActuator;

import java.util.concurrent.CountDownLatch;

public class ThreadAtom extends Thread {

    private final transient CountDownLatch counter;
    private final transient RunActuator consumer;

    ThreadAtom(final CountDownLatch counter,
               final RunActuator consumer) {
        this.counter = counter;
        this.consumer = consumer;
    }

    @Override
    public void run() {
        this.consumer.execute();
        this.counter.countDown();
    }
}
