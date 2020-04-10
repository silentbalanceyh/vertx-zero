package io.vertx.up.uca.rs;

import io.vertx.up.exception.UpException;

import java.lang.annotation.Annotation;

/**
 * Verification for epsilon
 */
public interface Rambler {

    void verify(Class<? extends Annotation> clazz,
                Class<?> type) throws UpException;
}
