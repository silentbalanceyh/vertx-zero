package io.vertx.up.fn;

@FunctionalInterface
public interface TiConsumer<F, S, T> {

    void accept(F f, S s, T t);
}
