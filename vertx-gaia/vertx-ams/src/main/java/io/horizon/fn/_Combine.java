package io.horizon.fn;

import io.horizon.util.HUt;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author lang : 2023-06-07
 */
class _Combine extends _Bug {
    protected _Combine() {
    }

    /**
     * 最简单的合并函数，直接将输入的异步结果合并到一起，合并过程中有两点需要注意：
     * <pre><code>
     *     1. 该函数一般为组合函数的后半段，不考虑前置操作，则包含了多个异步结果
     *     2. 该函数中如果出现了异常，在打开 Stack 的模式下 Z_DEV_JVM_STACK 时执行异常打印
     *     3. 默认情况下该函数会直接输出 {@link io.horizon.exception.web._500InternalServerException} 异常
     *       （若返回的 WebException 则输出的异常为 WebException）
     * </code></pre>
     *
     * @param res CompositeFuture 输入的异步结果
     * @param <T> 泛型类型
     *
     * @return Future<List < T>> 返回执行过的结果数组
     */
    public static <T> Future<List<T>> combineT(final CompositeFuture res) {
        return HArrange.combineT(res);
    }

    /**
     * 二元组合函数
     * <pre><code>
     *                          combinerOf
     *                           f + s => ( t )
     *      ( f )        -->       fx              -->     ( t )
     *      ( s )        -->
     * </code></pre>
     * 针对两个异步结果执行合并，这两个异步结果可以是返回不同类型，若类型不相同则使用 combinerOf 组合函数执行
     * 最终结果的组合，组合过程也是可异步执行的操作
     *
     * @param futureF    Future<F> 输入的异步结果，结果内是 F
     * @param futureS    Future<S> 输入的异步结果，结果内是 S
     * @param combinerOf BiFunction<F, S, Future<T>> 组合函数，输入为 F 和 S，输出为 Future<T>
     * @param <F>        第一个异步结果 F
     * @param <S>        第二个异步结果 S
     * @param <T>        组合函数的最终执行结果 T
     *
     * @return Future<T> 返回执行过的结果
     */
    public static <F, S, T> Future<T> combineT(
        final Future<F> futureF, final Future<S> futureS,
        final BiFunction<F, S, Future<T>> combinerOf) {
        return HArrange.combineT(() -> futureF, () -> futureS, combinerOf);
    }

    /**
     * 二元组合函数的顺序模式
     * <pre><code>
     *                                                       combinerOf
     *                                                       f + s -> ( t )
     *       supplierF                                  |
     *          fx      ->   (f)                   f    |
     *                                                  |        fx          -->   ( t )
     *                            functionS             |
     *                        f      fx(f)   ->   (s)   |
     * </code></pre>
     * 二元组合函数的变体，参数可支持延迟执行，执行流程如下：
     * 1. 先执行第一个函数的 supplier 得到第一输出 f
     * 2. 根据第一输出执行第二个函数 function 得到第二输出 s
     * 3. 组合函数将第一输出 f 和第二输出 s 作为参数合并得到最终输出
     * 前两个函数的执行是异步顺序，且第二个函数的输入依赖第一个函数的输出
     *
     * @param supplierF  Supplier<Future<F>> 输入的异步结果，结果内是 F
     * @param functionS  Function<F, Future<S>> 输入的异步结果，结果内是 S
     * @param combinerOf BiFunction<F, S, Future<T>> 组合函数，输入为 F 和 S，输出为 Future<T>
     * @param <F>        第一个异步结果 F
     * @param <S>        第二个异步结果 S
     * @param <T>        组合函数的最终执行结果 T
     *
     * @return Future<T> 返回执行过的结果
     */
    public static <F, S, T> Future<T> combineT(final Supplier<Future<F>> supplierF,
                                               final Function<F, Future<S>> functionS,
                                               final BiFunction<F, S, Future<T>> combinerOf) {
        return HArrange.combineT(supplierF, functionS, combinerOf);
    }

    public static <F, S, T> Future<T> combineT(final Future<F> futureF,
                                               final Function<F, Future<S>> functionS,
                                               final BiFunction<F, S, Future<T>> combinerOf) {
        return HArrange.combineT(() -> futureF, functionS, combinerOf);
    }

    /**
     * 二阶组合函数
     * <pre><code>
     * ( [                       [
     *                                       combinerOf
     *     i                         i  -->     fx      ( t )
     *     i           --->          i  -->     fx      ( t )            --> ( [ t, t, t ] )
     *     i                         i  -->     fx      ( t )
     * ] )                       ]
     * </code></pre>
     *
     * 针对异步集合结果中的每个元素执行二阶组合，最终生成一个新的集合异步结果：
     * 1. 先提取 futureL 中的最终结果 List<I>
     * 2. 然后遍历结果集合
     *
     * @param futureL    Future<List<S>> 输入的异步结果，结果内是 List<S>
     * @param combinerOf Function<S, Future<T>> 组合函数，输入为 S，输出为 Future<T>
     * @param <I>        第一个异步结果 I
     * @param <T>        组合函数的最终执行结果 T
     *
     * @return Future<List < T>> 返回执行过的结果数组
     */
    public static <I, T> Future<List<T>> combineT(final Future<List<I>> futureL, final Function<I, Future<T>> combinerOf) {
        return futureL.compose(source -> combineT(source, combinerOf));
    }

    /**
     * 组合函数最简单的模式
     * <pre><code>
     * [
     *      (t)
     *      (t)     -->     ( [ t, t, t ] )
     *      (t)
     * ]
     * </code></pre>
     *
     * @param futures List<Future<T>> 输入的异步结果，结果内是 T
     * @param <T>     泛型类型
     *
     * @return Future<List> 返回执行过的结果数组
     */
    public static <T> Future<List<T>> combineT(final List<Future<T>> futures) {
        return HArrange.combineT(futures);
    }

    public static <T> Future<Set<T>> combineT(final Set<Future<T>> futures) {
        return HArrange.combineT(futures);
    }

    /**
     * 组合函数的同步模式
     * <pre><code>
     * [
     *               combinerOf
     *      i  -->       fx      ( t )
     *      i  -->       fx      ( t )            --> ( [ t, t, t ] )
     *      i  -->       fx      ( t )
     * ]
     * </code></pre>
     *
     * @param source     输入的集合 List<I>
     * @param combinerOf 组合函数，输入为 I，输出为 Future<T>
     * @param <I>        输入类型I
     * @param <T>        输出类型T
     *
     * @return Future<List> 返回执行过的结果数组
     */
    public static <I, T> Future<List<T>> combineT(final List<I> source,
                                                  final Function<I, Future<T>> combinerOf) {
        final List<Future<T>> futures = new ArrayList<>();
        HUt.itList(source).map(combinerOf).forEach(futures::add);
        return HArrange.combineT(futures);
    }

    public static <I, T> Future<Set<T>> combineT(final Set<I> source,
                                                 final Function<I, Future<T>> combinerOf) {
        final Set<Future<T>> futures = new HashSet<>();
        HUt.itSet(source).map(combinerOf).forEach(futures::add);
        return HArrange.combineT(futures);
    }


    /**
     * 二元组合函数
     * <pre><code>
     *                                  combinerOf
     *                                    f + s -> ( t )
     *     supplierF               |
     *         fx   ->  f    -->   |
     *                             o        fx       -->     (t3)
     *     supplierS               |
     *         fx   ->  s    -->   |
     * </code></pre>
     * 二元组合函数的变体，参数可支持延迟执行功能，执行流程如下
     * 1. 先执行两个 supplier 得到第一输出 f 和第二输出 s
     * 2. 后续步骤和标准二元函数一致
     * 该方法为并行执行，第一结果和第二结果互不影响的模式，最终得到合并之后的结果
     *
     * @param supplierF  Supplier<Future<F>> 输入的异步结果执行函数，结果内是 F
     * @param supplierS  Supplier<Future<S>> 输入的异步结果执行函数，结果内是 S
     * @param combinerOf BiFunction<F, S, Future<T>> 组合函数，输入为 F 和 S，输出为 Future<T>
     * @param <F>        第一个异步结果 F
     * @param <S>        第二个异步结果 S
     * @param <T>        组合函数的最终执行结果 T
     *
     * @return Future<T> 返回执行过的结果
     */
    public static <F, S, T> Future<T> combineT(final Supplier<Future<F>> supplierF, final Supplier<Future<S>> supplierS,
                                               final BiFunction<F, S, Future<T>> combinerOf) {
        return HArrange.combineT(supplierF, supplierS, combinerOf);
    }

    // >>> 返回：Future<Boolean>
    // 内部调用 combineT，不关心结果，此处不做特殊注释说明
    public static <T> Future<Boolean> combineB(final List<Future<T>> futures) {
        return HArrange.combineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    public static <T> Future<Boolean> combineB(final Set<Future<T>> futures) {
        return HArrange.combineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    public static <I, T> Future<Boolean> combineB(final List<I> source, final Function<I, Future<T>> generateFun) {
        final List<Future<T>> futures = new ArrayList<>();
        HUt.itList(source).map(generateFun).forEach(futures::add);
        return HArrange.combineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    public static <I, T> Future<Boolean> combineB(final Set<I> source, final Function<I, Future<T>> generateFun) {
        final Set<Future<T>> futures = new HashSet<>();
        HUt.itSet(source).map(generateFun).forEach(futures::add);
        return HArrange.combineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }
}
