package io.vertx.up.util;

import io.horizon.eon.VValue;
import io.horizon.uca.log.Annal;
import io.horizon.util.HaS;
import io.vertx.up.annotations.Contract;
import io.vertx.up.exception.web._412ContractFieldException;
import io.vertx.up.fn.Fn;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("all")
final class InstanceField {
    private static final Annal LOGGER = Annal.get(InstanceField.class);

    private InstanceField() {
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
