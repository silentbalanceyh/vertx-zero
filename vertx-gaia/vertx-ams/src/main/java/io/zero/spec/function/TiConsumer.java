package io.zero.spec.function;

/*
 * Consumer：单参（Java自带）
 * BiConsumer：双参（Java自带）
 * TiConsumer：三参（Zero扩展）
 */
@FunctionalInterface
public interface TiConsumer<F, S, T> {

    void accept(F f, S s, T t);
}
