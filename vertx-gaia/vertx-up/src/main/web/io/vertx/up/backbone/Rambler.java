package io.vertx.up.backbone;

import io.horizon.exception.BootingException;

import java.lang.annotation.Annotation;

/**
 * Verification for epsilon
 */
public interface Rambler {

    void verify(Class<? extends Annotation> clazz,
                Class<?> type) throws BootingException;
}
