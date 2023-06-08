package io.horizon.fn;

import io.horizon.exception.AbstractException;
import io.vertx.core.Future;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * @author lang : 2023-05-27
 */
class _Async {
    protected _Async() {

    }

    /**
     * 并行检查器，检查所有的异步结果，全部为 true 时则通过检查，
     * 最终返回双态 Monad
     *
     * @param response  响应
     * @param error     检查不通过抛出的异常
     * @param executors 执行器
     * @param <T>       响应类型
     * @param <E>       异常类型
     *
     * @return {@link Future}
     */
    public static <T, E extends AbstractException> Future<T> passAll(
        final T response, final E error,
        final Set<Function<T, Future<Boolean>>> executors) {
        return HAsync.pass(response, error, list -> list.stream().allMatch(Boolean::booleanValue), executors);
    }

    /**
     * 并行检查器，检查所有的异步结果，只要有一个为 true 时则通过检查，
     * 最终返回双态 Monad
     *
     * @param response  响应
     * @param error     检查不通过抛出的异常
     * @param executors 执行器
     * @param <T>       响应类型
     * @param <E>       异常类型
     *
     * @return {@link Future}
     */
    public static <T, E extends AbstractException> Future<T> passAny(
        final T response, final E error,
        final Set<Function<T, Future<Boolean>>> executors) {
        return HAsync.pass(response, error, list -> list.stream().allMatch(Boolean::booleanValue), executors);
    }

    /**
     * 并行检查器，检查所有的异步结果，所有结果都为 false 时则通过检查，
     *
     * @param response  响应
     * @param error     检查不通过抛出的异常
     * @param executors 执行器
     * @param <T>       响应类型
     * @param <E>       异常类型
     *
     * @return {@link Future}
     */
    public static <T, E extends AbstractException> Future<T> passNone(
        final T response, final E error,
        final Set<Function<T, Future<Boolean>>> executors) {
        return HAsync.pass(response, error, list -> list.stream().noneMatch(Boolean::booleanValue), executors);
    }

    /**
     * 并行编排器，此种编排器不在意执行结果，只关心执行是否成功，工作流程如下：
     * <pre><code>
     *     input -> executor1 -> output1 -> Future<input>
     *           -> executor2 -> output2
     *           -> executor3 -> output3
     *           ...
     *           -> executorN -> outputN
     * </code></pre>
     *
     * 整体流程如，其中所有的 executor 是同时执行
     * <pre><code>
     *     input -> executor1
     *              executor2
     *              executor3 -> output
     * </code></pre>
     *
     * @param input     输入
     * @param executors 执行器
     * @param <T>       输入类型
     *
     * @return {@link Future}
     */
    public static <T> Future<T> parallel(final T input, final Set<Function<T, Future<T>>> executors) {
        return HAsync.parallel(input, executors);
    }

    /**
     * 并行编排器，{@link _Async#parallel} 的重载版本
     *
     * @param input     输入
     * @param executors 执行器
     * @param <T>       输入类型
     *
     * @return {@link Future}
     */
    public static <T> Future<T> parallel(final T input, final List<Function<T, Future<T>>> executors) {
        return HAsync.parallel(input, new HashSet<>(executors));
    }

    /**
     * 并行编排器，{@link _Async#parallel} 的重载版本
     *
     * @param input     输入
     * @param executors 执行器
     * @param <T>       输入类型
     *
     * @return {@link Future}
     */
    @SafeVarargs
    public static <T> Future<T> parallel(final T input, final Function<T, Future<T>>... executors) {
        return HAsync.parallel(input, new HashSet<>(Arrays.asList(executors)));
    }

    /**
     * 异步串行编排器，工作流程如下：
     * <pre><code>
     *     input -> executor1 -> output1 ->
     *              executor2 -> output2 ->
     *              executor3 -> output3 ->
     *              ...
     *              executorN -> outputN -> Future<outputN>
     * </code></pre>
     * 执行流程过程中每一个步骤的输出结果会作为下一个执行的输入，整体流程如：
     * <pre><code>
     *     input -> executor1 -> executor2 -> executor3 -> executorN
     * </code></pre>
     *
     * @param input     输入
     * @param executors 执行器
     * @param <T>       输入类型
     *
     * @return {@link Future}
     */
    public static <T> Future<T> passion(final T input, final List<Function<T, Future<T>>> executors) {
        return HAsync.passion(input, executors);
    }

    /**
     * 异步串行编排器，{@link _Async#passion} 的重载版本
     *
     * @param input     输入
     * @param executors 执行器
     * @param <T>       输入类型
     *
     * @return {@link Future}
     */
    @SafeVarargs
    public static <T> Future<T> passion(final T input, final Function<T, Future<T>>... executors) {
        return HAsync.passion(input, Arrays.asList(executors));
    }
}
