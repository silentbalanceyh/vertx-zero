package io.vertx.up.atom.agent;

import io.vertx.up.backbone.Filler;
import io.vertx.up.eon.KWeb;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * FeignDepot to extract the method parameters
 */
public class Depot implements Serializable {

    private final List<String> paramNames = new ArrayList<>();
    private final Event event;
    private final List<Class<? extends Annotation>> paramAnnos = new ArrayList<>();
    private List<Class<?>> paramTypes = new ArrayList<>();
    private List<Object> paramValues = new ArrayList<>();

    private Depot(final Event event) {
        // 1. Extract types for parameters
        this.initTypes(event.getAction());
        // 2. Extract annotation for parameters
        this.initAnnotationsWithName(event.getAction());
        // 3. Reference to event
        this.event = event;
    }

    public static Depot create(final Event event) {
        return new Depot(event);
    }

    private void initTypes(final Method method) {
        final Class<?>[] paramTypes = method.getParameterTypes();
        this.paramTypes = Arrays.asList(paramTypes);
    }

    private void initAnnotationsWithName(final Method method) {
        final Annotation[][] annotations = method.getParameterAnnotations();
        Ut.itArray(annotations, (annotationArr, index) -> {
            // Find annotation class
            final Annotation annotation = this.findAnnotation(annotationArr);
            final Class<? extends Annotation> annoCls = (null == annotation)
                ? null : annotation.annotationType();
            this.paramAnnos.add(annoCls);
            // Check names
            if (null != annoCls) {
                if (Filler.NO_VALUE.contains(annoCls)) {
                    this.paramNames.add(KWeb.ARGS.MIME_DIRECT);
                } else {
                    final String name = Ut.invoke(annotation, "value");
                    this.paramNames.add(name);
                }
            } else {
                this.paramNames.add(KWeb.ARGS.MIME_IGNORE);
            }
            // Besure the params are length match others.
            this.paramValues.add(null);
        });
    }

    private Annotation findAnnotation(final Annotation[] annotations) {
        Annotation annotationCls = null;
        for (final Annotation annotation : annotations) {
            if (Filler.PARAMS.containsKey(annotation.annotationType())) {
                annotationCls = annotation;
            }
        }
        return annotationCls;
    }

    public ConcurrentMap<String, Class<?>> getTypes() {
        return Ut.elementZip(this.paramNames, this.paramTypes);
    }

    public ConcurrentMap<String, Object> getValues() {
        return Ut.elementZip(this.paramNames, this.paramValues);
    }

    public ConcurrentMap<String, Class<? extends Annotation>> getAnnotations() {
        return Ut.elementZip(this.paramNames, this.paramAnnos);
    }

    public void setParamValues(final Object[] parameters) {
        this.paramValues = Arrays.asList(parameters);
    }

    public Event getEvent() {
        return this.event;
    }
}
