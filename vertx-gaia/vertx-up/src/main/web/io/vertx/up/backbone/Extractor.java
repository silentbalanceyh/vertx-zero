package io.vertx.up.backbone;

public interface Extractor<T> {

    T extract(Class<?> clazz);
}
