package io.vertx.tp.atom.refine;

import io.vertx.up.log.Annal;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 空检查专用函数流，
 * 防止空传入，Record 为 null 在整个Origin X中是禁止的，但不抛异常，只是不执行
 */
class AoDo {

    /*
     * Boolean返回值
     */
    static <T> Function<T, Boolean> doBoolean(final Annal logger,
                                              final Function<T, Boolean> function) {
        return executeFun(logger, function);
    }

    static <T> Supplier<T> doSupplier(final Annal logger,
                                      final Supplier<T> supplier) {
        return () -> {
            final T result = supplier.get();
            if (Objects.isNull(result)) {
                logger.error("[ Ox ] 返回值为 null");
            }
            return result;
        };
    }

    static <T> Function<T, Boolean[]> doBooleans(final Annal logger,
                                                 final Function<T, Boolean[]> function) {
        return executeFun(logger, function);
    }

    /*
     * 返回值和输入值相同
     */
    static <T> Function<T, T> doFluent(
            final Annal logger,
            final Function<T, T> function) {
        return executeFun(logger, function);
    }

    static <T, S> BiFunction<T, S, T> doBiFluent(
            final Annal logger,
            final BiFunction<T, S, T> function
    ) {
        return executeBiFun(logger, function);
    }

    /*
     * 输入和返回都是泛型
     */
    static <T, R> Function<T, R> doStandard(
            final Annal logger,
            final Function<T, R> function) {
        return executeFun(logger, function);
    }

    static <F, S, R> BiFunction<F, S, R> doBiStandard(
            final Annal logger,
            final BiFunction<F, S, R> function) {
        return executeBiFun(logger, function);
    }

    private static <F, S, R> BiFunction<F, S, R> executeBiFun(
            final Annal logger,
            final BiFunction<F, S, R> function) {
        return (first, second) -> {
            if (null == first || null == second) {
                logger.error("[ Ox ] 输入参数为 null, 首参：{0}, 二参：{1}", first, second);
                return null;
            } else {
                return function.apply(first, second);
            }
        };
    }

    private static <T, R> Function<T, R> executeFun(final Annal logger,
                                                    final Function<T, R> function) {
        return record -> {
            if (null == record) {
                logger.error("[ Ox ] Record 记录为 null");
                return null;
            } else {
                return function.apply(record);
            }
        };
    }
}
