package io.vertx.up.uca.web.thread;

import io.reactivex.Observable;
import io.vertx.up.annotations.Qualifier;
import io.vertx.up.eon.Plugins;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import io.vertx.zero.exception.MultiAnnotatedException;
import io.vertx.zero.exception.NamedImplementionException;
import io.vertx.zero.exception.NamedNotFoundException;
import io.vertx.zero.exception.QualifierMissedException;

import javax.inject.Inject;
import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@SuppressWarnings("all")
public class AffluxThread extends Thread {

    private static final Annal LOGGER = Annal.get(AffluxThread.class);

    private final ConcurrentMap<String, Class<?>> fieldMap = new ConcurrentHashMap<>();

    private final transient Class<?> reference;
    private final transient Set<Class<?>> namedSet;
    private final transient Set<Class<?>> classes;

    public AffluxThread(final Class<?> clazz, final Set<Class<?>> classes) {
        this.setName("zero-injection-scanner-" + this.getId());
        this.reference = clazz;
        this.classes = classes;
        this.namedSet = classes.stream()
                .filter((item) -> item.isAnnotationPresent(Named.class))
                .collect(Collectors.toSet());
    }

    @Override
    public void run() {
        if (null != this.reference) {
            // 1. Read all inject point
            final List<Field> fields = Arrays.stream(this.reference.getDeclaredFields())
                    .filter(field -> Plugins.INJECT_ANNOTATIONS.stream().anyMatch(field::isAnnotationPresent))
                    .collect(Collectors.toList());
            // 2. Convert to fields
            for (final Field field : fields) {
                // 3. Specific
                if (field.isAnnotationPresent(Inject.class)) {
                    this.scanStandard(field);
                } else {
                    this.scanSpecific(field);
                }
            }
        }
    }

    private void scanStandard(final Field field) {
        // JSR 330
        final Class<?> type = field.getType();
        if (type.isInterface()) {
            // Interface
            final List<Class<?>> target = this.classes.stream().filter(
                    item -> Ut.isImplement(item, type)
            ).collect(Collectors.toList());
            // Unique Implementation Processing
            if (target.isEmpty()) {
                // 0 size of current size here
                final String fieldName = field.getName();
                final String typeName = field.getDeclaringClass().getName();
                LOGGER.warn(Info.SCANNED_JSR311, typeName, fieldName, type.getName());
            } else {
                // Unique
                if (Values.ONE == target.size()) {
                    final Class<?> targetCls = target.get(Values.IDX);
                    LOGGER.info(Info.SCANNED_FIELD, this.reference,
                            field.getName(), targetCls.getName(), Inject.class);
                    this.fieldMap.put(field.getName(), targetCls);
                } else {
                    // By Named and Qualifier
                    this.scanQualifier(field, target);
                }
            }
        } else {
            this.fieldMap.put(field.getName(), type);
            LOGGER.info(Info.SCANNED_FIELD, this.reference,
                    field.getName(), type.getName(), Inject.class);
        }

    }

    private void scanQualifier(final Field field,
                               final List<Class<?>> instanceCls) {
        // Log for instanceCls
        final List<String> instanceNames = instanceCls.stream().map(Class::getName).collect(Collectors.toList());
        LOGGER.info(Info.SCANNED_INSTANCES, Ut.fromJoin(instanceNames));
        // Field must annotated with @Qualifier
        final Annotation annotation = field.getAnnotation(Qualifier.class);

        Fn.outUp(null == annotation,
                LOGGER, QualifierMissedException.class,
                this.getClass(), field.getName(), field.getDeclaringClass().getName());

        // All implementation class must be annotated with @Named
        final boolean match = instanceCls.stream()
                .allMatch(item -> item.isAnnotationPresent(Named.class));

        final Set<String> names = instanceCls.stream()
                .map(Class::getName).collect(Collectors.toSet());

        Fn.outUp(!match,
                LOGGER, NamedImplementionException.class,
                this.getClass(), names, field.getType().getName());

        // Named value must be reflect with @Qualifier
        final String value = Ut.invoke(annotation, "value");

        final Optional<Class<?>> verified = instanceCls.stream()
                .filter(item -> {
                    final Annotation target = item.getAnnotation(Named.class);
                    final String targetValue = Ut.invoke(target, "value");
                    return value.equals(targetValue)
                            && !Ut.isNil(targetValue);
                }).findAny();

        Fn.outUp(!verified.isPresent(),
                LOGGER, NamedNotFoundException.class,
                this.getClass(), names, value);

        // Passed all specification
        this.fieldMap.put(field.getName(), verified.get());
    }

    private void scanSpecific(final Field field) {
        // Vert.x Defined
        final Set<Class<? extends Annotation>> defineds
                = Plugins.INFIX_MAP.keySet();
        final Annotation[] annotations = field.getDeclaredAnnotations();
        // Annotation counter
        final Set<String> set = new HashSet<>();
        final Annotation hitted = Observable.fromArray(annotations)
                .filter(annotation -> defineds.contains(annotation.annotationType()))
                .map(annotation -> {
                    set.add(annotation.annotationType().getName());
                    return annotation;
                }).blockingFirst();
        // Duplicated annotated
        Fn.outUp(Values.ONE < set.size(), LOGGER,
                MultiAnnotatedException.class, this.getClass(),
                field.getName(), field.getDeclaringClass().getName(), set);
        // Fill typed directly.
        LOGGER.info(Info.SCANNED_FIELD, this.reference,
                field.getName(),
                field.getDeclaringClass().getName(),
                hitted.annotationType().getName());
        this.fieldMap.put(field.getName(), field.getType());
    }

    public ConcurrentMap<String, Class<?>> getFieldMap() {
        return this.fieldMap;
    }

    public Class<?> getClassKey() {
        return this.reference;
    }

    public boolean isEmpty() {
        return this.fieldMap.isEmpty();
    }
}
