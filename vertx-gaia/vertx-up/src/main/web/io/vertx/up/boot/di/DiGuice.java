package io.vertx.up.boot.di;

import com.google.inject.AbstractModule;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface DiGuice<T extends I, I> {

    boolean success(Class<?> clazz);

    AbstractModule module(ConcurrentMap<Class<I>, Set<Class<T>>> tree, Set<Class<T>> flat);
}
