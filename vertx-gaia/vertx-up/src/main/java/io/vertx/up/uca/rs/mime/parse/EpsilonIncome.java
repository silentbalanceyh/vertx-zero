package io.vertx.up.uca.rs.mime.parse;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.eon.Values;
import io.vertx.up.eon.web.ID;
import io.vertx.up.exception.WebException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroSerializer;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.rs.Filler;
import io.vertx.up.util.Ut;
import jakarta.ws.rs.DefaultValue;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Help to extract epsilon
 */
public class EpsilonIncome implements Income<List<Epsilon<Object>>> {

    private static final Annal LOGGER = Annal.get(EpsilonIncome.class);
    private static final Cc<String, Atomic<Object>> CC_ATOMIC = Cc.openThread();

    @Override
    public List<Epsilon<Object>> in(final RoutingContext context,
                                    final Event event)
        throws WebException {
        final Method method = event.getAction();
        final Class<?>[] paramTypes = method.getParameterTypes();
        final Annotation[][] annoTypes = method.getParameterAnnotations();
        final List<Epsilon<Object>> args = new ArrayList<>();
        for (int idx = 0; idx < paramTypes.length; idx++) {

            /* For each field specification **/
            final Epsilon<Object> epsilon = new Epsilon<>();
            epsilon.setArgType(paramTypes[idx]);
            epsilon.setAnnotation(this.getAnnotation(annoTypes[idx]));
            epsilon.setName(this.getName(epsilon.getAnnotation()));

            /* Default Value **/
            epsilon.setDefaultValue(this.getDefault(annoTypes[idx], epsilon.getArgType()));

            /* Epsilon income -> outcome **/
            final Atomic<Object> atomic = CC_ATOMIC.pick(MimeAtomic::new); // Fn.po?lThread(POOL_ATOMIC, MimeAtomic::new);
            final Epsilon<Object> outcome = atomic.ingest(context, epsilon);
            args.add(Fn.orNull(() -> outcome, outcome));
        }
        return args;
    }

    @SuppressWarnings("all")
    private String getName(final Annotation annotation) {
        return Fn.orSemi(null == annotation, LOGGER,
            () -> ID.IGNORE,
            () -> Fn.orSemi(!Filler.NO_VALUE.contains(annotation.annotationType()),
                LOGGER,
                () -> Ut.invoke(annotation, "value"),
                () -> ID.DIRECT));
    }

    private Annotation getAnnotation(final Annotation[] annotations) {
        final List<Annotation> annotationList = Arrays.stream(annotations)
            .filter(item -> Filler.PARAMS.containsKey(item.annotationType()))
            .collect(Collectors.toList());
        return annotationList.isEmpty() ? null : annotationList.get(Values.IDX);
    }

    private Object getDefault(final Annotation[] annotations,
                              final Class<?> paramType) {
        final List<Annotation> annotationList = Arrays.stream(annotations)
            .filter(item -> item.annotationType() == DefaultValue.class)
            .collect(Collectors.toList());
        return Fn.orSemi(annotationList.isEmpty(), LOGGER,
            () -> null,
            () -> {
                final Annotation annotation = annotationList.get(Values.IDX);
                return ZeroSerializer.getValue(paramType,
                    Ut.invoke(annotation, "value"));
            });
    }
}
