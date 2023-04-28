package io.vertx.up.util;

import io.horizon.eon.VValue;
import io.horizon.util.HaS;
import io.vertx.up.annotations.Contract;
import io.vertx.up.exception.web._412ContractFieldException;
import io.vertx.up.fn.Fn;
import io.horizon.uca.log.Annal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("all")
final class InstanceField {
    private static final Annal LOGGER = Annal.get(InstanceField.class);

    private InstanceField() {
    }

    static <T> void set(final Object instance, final String name, final T value) {
        if (Objects.nonNull(instance) && Objects.nonNull(name)) {
            final Field field;
            try {
                field = instance.getClass().getDeclaredField(name);
                set(instance, field, value);
            } catch (NoSuchFieldException ex) {
                LOGGER.warn("Class {0} and details: {1}", instance.getClass(), ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    static <T> void set(final Object instance, final Field field, final T value) {
        Fn.runAt(() -> Fn.jvmAt(() -> {
            if (!field.isAccessible()) {
                // field.trySetAccessible();
                field.setAccessible(true);
            }
            field.set(instance, value);
        }, LOGGER), instance, field);
    }

    static Field[] fieldAll(final Object instance, final Class<?> fieldType) {
        final Function<Class<?>, Set<Field>> lookupFun = clazz -> lookUp(clazz, fieldType)
            .collect(Collectors.toSet());
        return Fn.failOr(() -> fieldAll(instance.getClass(), fieldType).toArray(new Field[]{}),
            instance, fieldType);
    }

    private static Set<Field> fieldAll(final Class<?> clazz, final Class<?> fieldType) {
        final Set<Field> fieldSet = new HashSet<>();
        if (Object.class != clazz) {

            /* Self */
            fieldSet.addAll(lookUp(clazz, fieldType).collect(Collectors.toSet()));

            /* Parent Iterator */
            fieldSet.addAll(fieldAll(clazz.getSuperclass(), fieldType));
        }
        return fieldSet;
    }

    private static Field get(final Class<?> clazz,
                             final String name) {
        return Fn.runOr(() -> {
            if (clazz == Object.class) {
                return null;
            }
            final Field[] fields = clazz.getDeclaredFields();
            final Optional<Field> field = Arrays.stream(fields)
                .filter(item -> name.equals(item.getName())).findFirst();
            if (field.isPresent()) {
                return field.get();
            } else {
                final Class<?> parentCls = clazz.getSuperclass();
                return get(parentCls, name);
            }
        }, clazz, name);
    }

    static <T> T getI(final Class<?> interfaceCls, final String name) {
        return Fn.runOr(() -> Fn.failOr(() -> {
                final Field field = interfaceCls.getField(name);
                final Object result = field.get(null);
                if (null != result) {
                    return (T) result;
                } else {
                    return null;
                }
            }, LOGGER)
            , interfaceCls, name);
    }

    static <T> T get(final Object instance,
                     final String name) {
        return Fn.runOr(() -> Fn.failOr(() -> {
                final Field field = get(instance.getClass(), name);
                if (Objects.nonNull(field)) {
                    /*
                     * Field valid
                     */
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    final Object result = field.get(instance);
                    if (null != result) {
                        return (T) result;
                    } else {
                        return null;
                    }
                } else return null;
            }, LOGGER)
            , instance, name);
    }

    static Field[] fields(final Class<?> clazz) {
        final Field[] fields = clazz.getDeclaredFields();
        return Arrays.stream(fields)
            .filter(item -> !Modifier.isStatic(item.getModifiers()))
            .filter(item -> !Modifier.isAbstract(item.getModifiers()))
            .toArray(Field[]::new);
    }

    private static Stream<Field> lookUp(final Class<?> clazz, final Class<?> fieldType) {
        return Fn.failOr(() -> {
            /* Lookup field */
            final Field[] fields = fields(clazz);
            /* Direct match */
            return Arrays.stream(fields)
                .filter(field -> fieldType == field.getType() ||          // Direct match
                    fieldType == field.getType().getSuperclass() ||  // Super
                    HaS.isImplement(field.getType(), fieldType));
        });
    }

    static <T> Field contract(final Class<?> executor, final T instance, final Class<?> fieldType) {
        /*
         * Reflect to set Api reference in target channel here
         * 1) The fields length must be 1
         * 2) The fields length must not be 0
         *  */
        final Field[] fields = fieldAll(instance, fieldType);
        /*
         * Counter
         */
        final Field[] filtered = Arrays.stream(fields)
            .filter(field -> field.isAnnotationPresent(Contract.class))
            .toArray(Field[]::new);
        Fn.out(1 != filtered.length, _412ContractFieldException.class,
            executor, fieldType, instance.getClass(), filtered.length);
        return filtered[VValue.IDX];
    }

    static <T, V> void contract(final Class<?> executor, final T instance, final Class<?> fieldType, final V value) {
        final Field field = contract(executor, instance, fieldType);
        Ut.field(instance, field, value);
    }
}
