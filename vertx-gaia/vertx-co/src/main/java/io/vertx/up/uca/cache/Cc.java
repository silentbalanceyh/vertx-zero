package io.vertx.up.uca.cache;

import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * Component Cache / Component Container
 * It's new structure for different component cache stored instead of single one ( ConcurrentMap ) here
 *
 * 1) It's not for data store but component instead.
 * 2) Capture all the point ConcurrentMap here for building.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Cc<K, T> {

    static <K, T> Cc<K, T> open() {
        /*
         * Create new Cc reference, here are all situations here for different component cache
         * 1) When the Cc created, it is often called in STATIC context
         * 2) Each Cc has three method for:
         * -- Global Context: ( key = T )
         * -- Thread Context: ( thread = T )
         * -- Thread Context with different interface: ( root + thread = T )
         *
         * Be careful of that do not store any ( config/data ) into cache because it's only for
         * component cache only, please do not call `Cc.create()` in NON-STATIC context because of
         * that it create new Cc in each time, here is no cache for Cc it-self
         *
         * This data structure should replace all the `ConcurrentMap` data structure in:
         * - Zero Framework
         * - Zero Extension Framework
         * - Zero Based Application
         *
         * All above three parts will be changed, in future you can replace the implementation class
         * such as XXX instead of `CcMemory`, then the reference of each object could be stored in
         * other place to cache.
         */
        return new CcMemory<>();
    }

    /**
     * Non-Thread Mode
     *
     * @param key      The pool key to stored supplier.get() reference here
     * @param supplier The reference supplier building method
     *
     * @return Cached reference
     */
    T pick(K key, Supplier<T> supplier);

    /**
     * Thread Mode
     *
     * thread name = reference
     *
     * This method is for single thread only pool, it often stored following references:
     * - 1) The cache key is thread name
     * - 2) The reference should be ( Interface + 1 Impl )
     *
     * * Multi Impl will be not support in this kind of situation
     *
     * @param supplier The reference supplier building method
     *
     * @return Cached reference
     */
    T pick(Supplier<T> supplier);

    /**
     * Thread Mode
     *
     * root + thread name = reference
     *
     * @param supplier The reference supplier building method
     * @param root     The root of thread cache key
     *
     * @return Cached reference
     */
    T pick(Supplier<T> supplier, String root);

    /**
     * Thread Mode
     *
     * clazz name + thread name = reference
     *
     * @param supplier The reference supplier building method
     * @param clazz    The root class to get thread cache key
     *
     * @return Cached reference
     */
    T pick(Supplier<T> supplier, Class<?> clazz);

    // Data Part ( For Configuration )
    boolean is(K key);

    void data(K key, T value);

    T data(K key);

    ConcurrentMap<K, T> data();
}
