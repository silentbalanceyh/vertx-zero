package io.vertx.zero.exception;

import io.horizon.exception.BootingException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class AnnotationRepeatException extends BootingException {

    public AnnotationRepeatException(final Class<?> clazz,
                                     final Method method,
                                     final Class<? extends Annotation> annoCls,
                                     final int occurs) {
        super(clazz, method.getName(), "@" + annoCls.getSimpleName(), occurs);
    }

    @Override
    public int getCode() {
        return -40029;
    }
}
