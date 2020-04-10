package io.vertx.up.experiment.brain;

/*
 * Vto date here
 */
public interface Vto<T> {

    T to(Object value, Class<?> type);
}
