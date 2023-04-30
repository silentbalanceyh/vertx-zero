package io.horizon.uca.convert;

/*
 * Vto date here
 */
public interface Vto<T> {

    T to(Object value, Class<?> type);
}
