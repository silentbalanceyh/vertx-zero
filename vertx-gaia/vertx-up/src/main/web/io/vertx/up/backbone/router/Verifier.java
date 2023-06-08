package io.vertx.up.backbone.router;

import io.horizon.uca.log.Annal;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.backbone.Filler;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.AnnotationRepeatException;
import io.vertx.zero.exception.EventActionNoneException;
import io.vertx.zero.exception.ParamAnnotationException;
import jakarta.ws.rs.BodyParam;
import jakarta.ws.rs.StreamParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Verifier {

    private static final Annal LOGGER = Annal.get(Verifier.class);

    @SuppressWarnings("all")
    public static void verify(final Event event) {
        final Method method = event.getAction();
        Fn.outBoot(null == method, LOGGER, EventActionNoneException.class,
            Verifier.class, event);
        /* Specification **/
        verify(method, BodyParam.class);
        verify(method, StreamParam.class);
        /* Field Specification **/
        for (final Parameter parameter : method.getParameters()) {
            verify(parameter);
        }
    }

    public static void verify(final Method method, final Class<? extends Annotation> annoCls) {
        final Annotation[][] annotations = method.getParameterAnnotations();
        final AtomicInteger integer = new AtomicInteger(0);
        Ut.itMatrix(annotations, (annotation) -> {
            if (annotation.annotationType() == annoCls) {
                integer.incrementAndGet();
            }
        });
        final int occurs = integer.get();

        Fn.outBoot(1 < occurs, LOGGER, AnnotationRepeatException.class,
            Verifier.class, method.getName(), annoCls, occurs);
    }

    public static void verify(final Parameter parameter) {
        final Annotation[] annotations = parameter.getDeclaredAnnotations();
        final List<Annotation> annotationList = Arrays.stream(annotations)
            .filter(item -> Filler.PARAMS.containsKey(item.annotationType()))
            .collect(Collectors.toList());

        final int multi = annotationList.size();
        Fn.outBoot(1 < multi, LOGGER, ParamAnnotationException.class,
            Verifier.class, parameter.getName(), multi);
    }
}
