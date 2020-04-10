package io.vertx.up.uca.rs;

public interface Extractor<T> {

    T extract(Class<?> clazz);
}
