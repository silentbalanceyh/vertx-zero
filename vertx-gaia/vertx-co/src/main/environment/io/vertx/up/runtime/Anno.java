package io.vertx.up.runtime;

import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * This class defined annotation scanned method in zero framework.
 *
 * @author Lang
 */
public final class Anno {

    private Anno() {
    }

    /**
     * This method scanned a class and build the mappings of annotation
     * name -> Annotation
     *
     * @param clazz The class that will be scanned.
     *
     * @return hash map that stored (name = Annotation) scanned results.
     */
    public static ConcurrentMap<String, Annotation> get(final Class<?> clazz) {
        return Fn.runOr(() -> Ut.elementZip(clazz.getDeclaredAnnotations(),
            (item) -> item.annotationType().getName(),
            (item) -> item), clazz);
    }

    /**
     * Query clazz's methods to getPlugin all annotated spec annotations.
     * Check whether this class contains the methods that annotated with methodCls
     *
     * @param clazz     The target class that will be scanned.
     * @param methodCls The annotation class that has been annotated on the methods that belong to clazz.
     *
     * @return All the annotations that in clazz's method.
     */
    public static Annotation[] query(final Class<?> clazz,
                                     final Class<? extends Annotation> methodCls) {
        return Fn.runOr(() -> Arrays.stream(clazz.getDeclaredMethods())
            .filter(item -> item.isAnnotationPresent(methodCls))
            .map(item -> item.getAnnotation(methodCls))
            .filter(Objects::nonNull)
            .toList().toArray(new Annotation[]{}), clazz, methodCls);
    }

    /**
     * Check whether class contains annotated field
     * Provide annotation set to check whether the clazz contains this kind of fields that
     * have been annotated with any of annotation class.
     *
     * @param clazz   The target class that will be scanned.
     * @param annoCls The annotation set that annotated on the fields that belong to target.
     *
     * @return If class contains this kind of fields, return true, otherwise return false.
     */
    public static boolean isMark(final Class<?> clazz,
                                 final Set<Class<? extends Annotation>> annoCls) {
        return Arrays.stream(clazz.getDeclaredFields()).anyMatch(field -> annoCls.stream().anyMatch(field::isAnnotationPresent));
    }
}
