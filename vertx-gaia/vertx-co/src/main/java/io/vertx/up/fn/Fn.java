package io.vertx.up.fn;

import io.horizon.annotations.HLinking;
import io.horizon.eon.info.VMessage;
import io.horizon.exception.AbstractException;
import io.horizon.exception.ProgramException;
import io.horizon.fn.*;
import io.horizon.util.HMs;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.UpException;
import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.*;

/**
 * Unique interface to call function in zero framework.
 * 基本格式：
 * --- [] 代表集合，
 * --- () 代表异步
 * 基础标记位处理：
 * 1. 参数类型标记位：
 * --- J: JsonObject
 * --- A: JsonArray
 * --- T: 泛型T
 * --- B: Boolean
 * --- M: Map哈希表
 * --- L: 列表 List
 * --- G: 分组专用
 * 2. 函数标记位（按参数可重载）
 * --- Supplier:    Or
 * --- Function:    Of
 * --- Predicate:   x
 * --- Consumer:    At
 * --- Actuator:    Act
 * 3. 函数异常标记位矩阵：
 * ------------ |  Throwable  |  Exception  |  ZeroException  |  ZeroRunException
 * - Supplier - |
 * - Function - |
 * - Consumer - |
 * - Predicate  |
 */
@SuppressWarnings("all")
public final class Fn {
    private Fn() {
    }

    // ---------------------------------------------------- 编排函数 ----------------------------------------------------

    /**
     * 二选一专用编排函数，执行流程如下：
     *
     * 1. 从 JsonObject 提取对象引用，该引用由 `field` 来提取。
     * 2. 检查对象引用的类型，分别为 `JsonArray` 和 `JsonObject`
     * 3. 根据不同的类型执行不同的逻辑函数
     * -- JsonObject: itemFnJ       ->   JsonObject -> Future[J]
     * -- JsonObject: itemFnA       ->   JsonArray  -> Future[A]
     * 4. 将执行函数的结果回写到 input 中的 `field` 属性。
     *
     * <pre><code>
     *     JsonObject
     *                                    JsonObject
     *                        | -->    fx: itemFnJ   -->    ( j1 )      --> input[field] = j1  |
     *                        |                                                                |
     *     input[field]   --> X                                                                o   -->  ( input )
     *                        |           JsonArray                                            |
     *                        | -->    fx: itemFnA   -->    ( a1 )      --> input[field] = a1  |
     *                        |                                                                |
     *                        |              ?              (default)                          |
     *                        | -->          x       ----------------->     input              |
     * </code></pre>
     *
     * 注意此处的编排函数结构返回值可以是 J 或 A，命名表示通常以 Future[JsonObject] 或 Future[JsonArray] 返回，但核心
     * 执行过程中可定义泛型返回值，但由于回调函数中会将返回结果重新封装回 input[field] 中，所以此处执行结果必须是JsonObject
     * 中可支持的类型，不可支持的类型会导致回写失败，而且当条件不匹配时什么都不做，直接返回 input。
     *
     * @param input   JsonObject 输入的JsonObject对象
     * @param field   String 提取对象引用的字段名
     * @param itemOrJ Function 逻辑函数，输入为 JsonObject，输出为 Future[J]
     * @param itemOrA Function 逻辑函数，输入为 JsonArray，输出为 Future[A]
     * @param <J>     [J] 逻辑函数的输出类型[J]，执行函数 jobject -> Future[J]
     * @param <A>     [A] 逻辑函数的输出类型[A]，执行函数 jarray  -> Future[A]
     *
     * @return 返回自包含的 Future<JsonObject> 对象
     */
    public static <J, A> Future<JsonObject> choiceJ(
        final JsonObject input, final String field,
        final Function<JsonObject, Future<J>> itemOrJ, final Function<JsonArray, Future<A>> itemOrA) {
        return ThenJ.choiceJ(input, field, itemOrJ, itemOrA);
    }
    // ---------------------------------------------------- 组合函数 ----------------------------------------------------
    // >>> 返回：Future<JsonArray>

    /**
     * 一维组合编排函数，其执行流程
     * <pre><code>
     * [
     *                                                       combinerOf
     *                                                       j + j1  =>  j2
     *                   generateOf
     *      j       -->     fx      -->    ( j1 )     -->         fx      -->
     *      j       -->     fx      -->    ( j1 )     -->         fx      -->         ( [j2, j2, j2] )
     *      j       -->     fx      -->    ( j1 )     -->         fx      -->
     * ]
     * </code></pre>
     *
     * 1. 处理单个异步结果
     * 2. 迭代JsonArray并提取 element 类型位 JsonObject 的结果集
     * 3. 针对每个JsonObject 元素执行 generateOf 函数，生成 Future<JsonObject> 结果
     * 4. 将得到的所有结果执行亮亮组合，并且使用拉平操作，得到的最终使用组合后的结果生成一个新的 JsonObject
     * -- combinerOf的参数 (j, j1)
     * ----- 第一参数 j  是原始的输入元素，即 generateOf 函数中的输入
     * ----- 第二参数 j1 是生成函数的输出，即 generateOf 函数的执行结果（异步结果）
     * 5. 最终返回组合后的结果，JsonArray 中的每个元素都是 JsonObject
     *
     * 组合函数在此处实际是针对输入和输出的组合，输入和输出在此函数中的类型是一致的，此处都是 JsonObject
     * - 输入：迭代 JsonArray 中生成的每个 JsonObject 元素
     * - 输出：针对 JsonObject 执行过 generateOf 函数后的结果
     * - 默认情况下不满足条件的（element instanceof JsonObject) 的元素不会执行 generateOf 函数，会直接被过滤掉
     *
     * @param source     Future<JsonArray> 输入的异步结果，结果内是 JsonArray
     * @param generateOf 元素生成函数，针对JsonArray中的每一个 JsonObject 函数执行 generateOf
     * @param combinerOf 组合函数，生成函数结果位 Future<JsonObject>，将所有异步结果执行两两合并
     *
     * @return 返回执行过的结果数组 Future<JsonArray>
     */
    public static Future<JsonArray> combineA(
        final Future<JsonArray> source,
        final Function<JsonObject, Future<JsonObject>> generateOf, final BinaryOperator<JsonObject> combinerOf) {
        return ThenA.combineA(source, generateOf, combinerOf);
    }

    /**
     * 一维组合编排函数，其执行流程
     * <pre><code>
     * [
     *                   generateOf
     *      j       -->     fx      -->    ( j1 )
     *      j       -->     fx      -->    ( j1 )    -->     ( [j1,j1,j1] )
     *      j       -->     fx      -->    ( j1 )
     * ]
     * </code></pre>
     *
     * 1. 针对 JsonArray 执行迭代，提取每个类型为 JsonObject 的元素
     * 2. 针对每个类型为 JsonObject 的元素执行生成函数 generateOf，生成 Future<JsonObject> 结果
     * 3. 将所有的 Future<JsonObject> 执行组合操作，生成新的 Future<JsonArray> 结果
     *
     * @param input      JsonArray 输入的异步结果，结果内是 JsonArray
     * @param generateOf 元素生成函数，针对JsonArray中的每一个 JsonObject 函数执行 generateOf
     *
     * @return 返回执行过的结果数组 Future<JsonArray>
     */
    public static Future<JsonArray> combineA(final JsonArray input,
                                             final Function<JsonObject, Future<JsonObject>> generateOf) {
        final List<Future<JsonObject>> futures = new ArrayList<>();
        Ut.itJArray(input).map(generateOf).forEach(futures::add);
        return ThenA.combineA(futures);
    }

    /**
     * 一维组合编排函数，其执行流程
     * <pre><code>
     * [
     *      ( j )
     *      ( j )         -->      ( [j, j, j] )
     *      ( j )
     * ]
     * </code></pre>
     *
     * 最简单的异步编排组合函数，直接将输入的列表型的异步结果组合到一起
     * List<Future<T>> 到 Future<List<T>> 的转换模式
     *
     * @param source Future<JsonArray> 输入的异步结果，结果内是 JsonArray
     *
     * @return 返回执行过的结果数组 Future<JsonArray>
     */
    public static Future<JsonArray> combineA(final List<Future<JsonObject>> source) {
        return ThenA.combineA(source);
    }

    /*
     * Workflow:
     *
     * [
     *      t   -->     fx    --> ( [...] )
     *      t   -->     fx    --> ( [...] )       --> ( [... ... ... ... ...] )
     *      t   -->     fx    --> ( [...] )
     * ]
     *
     * The o = Class<T>
     * 「1 Dim」
     * This method is common comic, the input data structure is collection ( JsonArray ) as:
     *
     * 1. Filter the `source` based on `clazz` type, other type will be ignored.
     * 2. 「F」Process each element t1 ( selected Class<T> ) to generate the new collection.
     * 3. Until this step the final result set is matrix: [[]]
     * 4. Combine the matrix from 2 dim to 1 dim: [[]] => []
     */

    /**
     * 防重载的JsonArray再行的 combineA 的变体组合函数，由于和普通的JsonArray再行的组合函数冲突，所以需要重命名该函数，执行流程
     * 如下（该函数为扩散型）：
     * <pre><code>
     * e 的类型为 Class<T>
     * [
     *                      generateOf
     *     e      -->            fx       ( [t, t, t, ...] )
     *     e      -->            fx       ( [t, t, t, ...] )    -->  ( [..., ..., ...] )
     *     e      -->            fx       ( [t, t, t, ...] )
     * ]
     * </code></pre>
     * 该方法是合并变体函数，它的输入为一个JsonArray，内部元素 e 的类型为传入类型
     *
     * 1. 使用类型对 JsonArray 传入集合执行过滤，只有满足类型条件的会生效，其他内容会被忽略。
     * 2. 筛选出合法元素之后根据合法元素执行 generateOf 且返回值为 JsonArray 异步结果。
     * 3. 将异步结果全部拉平合并到一个 JsonArray 中，二阶转一阶：[[],[],[]] => []
     *
     * @param source     JsonArray 输入的集合
     * @param clazz      Class<T> 输入的类型，限定集合中元素类型
     * @param generateOf 元素生成函数，针对JsonArray中的每一个 JsonObject 函数执行 generateOf
     * @param <T>        泛型类型
     *
     * @return 返回执行过的结果数组 Future<JsonArray>
     */
    public static <T> Future<JsonArray> combineA_(final JsonArray source, final Class<T> clazz,
                                                  final Function<T, Future<JsonArray>> generateOf) {
        final List<Future<JsonArray>> futures = new ArrayList<>();
        Ut.itJArray(source, clazz, (item, index) -> futures.add(generateOf.apply(item)));
        return ThenA.compressA(futures);
    }

    public static Future<JsonArray> combineA_(final JsonArray source,
                                              final Function<JsonObject, Future<JsonArray>> generateFun) {
        return combineA_(source, JsonObject.class, generateFun);
    }

    // >>> 返回：Future<Boolean>
    // 内部调用 combineT，不关心结果，此处不做特殊注释说明
    public static <T> Future<Boolean> combineB(final List<Future<T>> futures) {
        return ThenT.combineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    public static <T> Future<Boolean> combineB(final Set<Future<T>> futures) {
        return ThenT.combineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    public static <I, T> Future<Boolean> combineB(final List<I> source, final Function<I, Future<T>> generateFun) {
        final List<Future<T>> futures = new ArrayList<>();
        Ut.itList(source).map(generateFun).forEach(futures::add);
        return ThenT.combineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    public static <I, T> Future<Boolean> combineB(final Set<I> source, final Function<I, Future<T>> generateFun) {
        final Set<Future<T>> futures = new HashSet<>();
        Ut.itSet(source).map(generateFun).forEach(futures::add);
        return ThenT.combineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    // >>> 返回：Future<JsonObject>

    /**
     * JsonObject格式的变参组合编排函数，生成数据结构有所变化，它的执行流程如：
     * <pre><code>
     *                                  (
     *     (j1)                           0     = j1
     *     (j2)               ->          1     = j2
     *     ...
     *     (jn)                           n - 1 = jn
     *                                  )
     * </code></pre>
     *
     * @param futures Future<JsonObject>... 变参集合信息
     *
     * @return Future<JsonObject> 返回的数据结构是 JsonObject
     */
    public static Future<JsonObject> combineJ(final Future<JsonObject>... futures) {
        return ThenJ.combineJ(futures);
    }

    // ----------------------- 防重载组合函数, combineJ_ 变体 -----------------------

    /**
     * 防止重载的JsonObject类型 combineJ 的变体组合函数，由于会和普通的JsonObject类型的组合函数冲突，所以需要重命名该组合函数，其执行
     * 流程如下
     * <pre><code>
     *                          generateOf                                              combinerOf
     *                       j  =>  ([j1,j1,j1])                                          j + j1  =>  j2
     *      t         -->         fx          -->      ( [j1,j1,j1] )          -->         fx          -->     (j2)
     *
     * </code></pre>
     * 该函数执行流程相对复杂
     *
     * 1. 先将输入作为生成函数的输入，会生成一个列表信息，这种模式会拓展生成多个 JsonObject 列表，而且生成过程中
     * 每个函数本身都是异步结果。
     * 2. 根据一部结果针对每个元素执行一次合并，得到最终结果如：
     * <pre><code>
     *     j          -->         j1 ( index = 0 )       -->       combinerOf ( index = 0 )      --> j2
     *                            j1 ( index = 1 )       -->       combinerOf ( index = 1 )      --> j2
     *                            ...                    -->       ...                           --> ...
     * </code></pre>
     * 拉平处理之后执行组合，最终会返回合并后的唯一的JsonObject，该函数可能会有副作用，导致最终结果受到影响。
     *
     * 这个接口和其他接口不同，combinerOf 会有两个版本
     * 1. 如果 source 被修改，那么 combinerOf 可以执行修改操作，这种模式会影响输入数据，带有副作用
     * 2.
     *
     * @param source     输入的JsonObject
     * @param generateOf 生成函数，输入为 JsonObject，输出为 List<Future>
     * @param combinerOf 组合函数，输入为 JsonObject 和 JsonObject，输出为 JsonObject
     *
     * @return Future<JsonObject> 返回执行过的结果
     */
    public static Future<JsonObject> combineJ_(
        final Future<JsonObject> source, final Function<JsonObject, List<Future>> generateOf,
        final BiConsumer<JsonObject, JsonObject>... combinerOf) {
        return ThenJ.combineJ(source, generateOf, combinerOf);
    }

    public static Future<JsonObject> combineJ_(
        final JsonObject source, final Function<JsonObject, List<Future>> generateFun,
        final BiConsumer<JsonObject, JsonObject>... operatorFun) {
        return ThenJ.combineJ(Future.succeededFuture(source), generateFun, operatorFun);
    }

    /**
     * 哈希表组合函数
     * <pre><code>
     * [                                               (
     *      k=(t)                                          k=t,
     *      k=(t)         -->      (t)           =         k=t,
     *      k=(t)                                          k=t,
     * ]                                               )
     * </code></pre>
     * 哈希表专用的组合函数，针对每一个键值提供异步结果，此处的类型必须使用 ConcurrentMap，由于每一个键值对是同时执行
     * 且相互之间不依赖，在并行环境下，只有 ConcurrentMap 才能保证线程安全。
     *
     * @param futureMap ConcurrentMap<K, Future<T>> 输入的异步结果，结果内是 ConcurrentMap<K, Future<T>>
     * @param <K>       键类型
     * @param <T>       值类型
     *
     * @return 返回执行过的结果数组 Future<ConcurrentMap<K, T>>
     */
    public static <K, T> Future<ConcurrentMap<K, T>> combineM(final ConcurrentMap<K, Future<T>> futureMap) {
        return ThenM.combineM(futureMap);
    }
    // >>> 返回：Future<T>

    /**
     * 最简单的合并函数，直接将输入的异步结果合并到一起，合并过程中有两点需要注意：
     * 1. 该函数一般为组合函数的后半段，不考虑前置操作，则包含了多个异步结果
     * 2. 该函数中如果出现了异常，在打开 Stack 的模式下 Z_DEV_JVM_STACK 时执行异常打印
     * 3. 默认情况下该函数会直接输出 _500InternalError 异常（若返回的 WebException 则输出的异常为 WebException）
     *
     * @param res CompisiteFuture 输入的异步结果
     * @param <T> 泛型类型
     *
     * @return Future<List < T>> 返回执行过的结果数组
     */
    public static <T> Future<List<T>> combineT(final CompositeFuture res) {
        return ThenT.combineT(res);
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
        return ThenT.combineT(() -> futureF, () -> futureS, combinerOf);
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
        return ThenT.combineT(supplierF, functionS, combinerOf);
    }

    public static <F, S, T> Future<T> combineT(final Future<F> futureF,
                                               final Function<F, Future<S>> functionS,
                                               final BiFunction<F, S, Future<T>> combinerOf) {
        return ThenT.combineT(() -> futureF, functionS, combinerOf);
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
        return ThenT.combineT(futures);
    }

    public static <T> Future<Set<T>> combineT(final Set<Future<T>> futures) {
        return ThenT.combineT(futures);
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
        Ut.itList(source).map(combinerOf).forEach(futures::add);
        return ThenT.combineT(futures);
    }

    public static <I, T> Future<Set<T>> combineT(final Set<I> source,
                                                 final Function<I, Future<T>> combinerOf) {
        final Set<Future<T>> futures = new HashSet<>();
        Ut.itSet(source).map(combinerOf).forEach(futures::add);
        return ThenT.combineT(futures);
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
     * @return
     */
    public static <F, S, T> Future<T> combineT(final Supplier<Future<F>> supplierF, final Supplier<Future<S>> supplierS,
                                               final BiFunction<F, S, Future<T>> combinerOf) {
        return ThenT.combineT(supplierF, supplierS, combinerOf);
    }

    // ---------------------------------------------------- 压缩函数 ----------------------------------------------------

    // >>> 返回：Future<JsonArray>

    /**
     * 压缩函数，二阶转一阶
     * [[t1, t2, t3], [t4, t5, t6]] -> [t1, t2, t3, t4, t5, t6]
     * 该压缩函数在很多地方会使用到，主要用于将：集合的集合转换成集合（拉平后处理，类似 flatMap 效果，只是该函数支持异步）
     *
     * @param futures List<Future<JsonArray>> 输入的异步结果，结果内是 JsonArray
     *
     * @return Future<JsonArray> 返回执行过的压缩结果
     */
    public static Future<JsonArray> compressA(final List<Future<JsonArray>> futures) {
        return ThenA.compressA(futures);
    }

    /*
     * Workflow:
     *
     * [
     *      ( [t,t] )           -->    ...
     *      ( [t,t,t,t] )       -->    ...   -->  ( [t,t,   t,t,t,t,   t] )
     *      ( [t] )             -->    ...
     * ]
     *
     * 「2 Dim」
     * The input is matrix, this method will compress the 2 dim matrix to 1 dim, the code logical is:
     *
     * 1. Process each element ( Future<List<T>> ) async operation to get the result ( List<T> ).
     * 2. Combine the result   ( List<List<T>> )  =>  ( List<T> ) only.
     * 3. The final data structure is:  [[]] => []
     */

    /**
     * 压缩函数，二阶转一阶
     *
     * <pre><code>
     * [
     *      ( [t,t] )           -->    ...
     *      ( [t,t,t,t] )       -->    ...   -->  ( [t,t,   t,t,t,t,   t] )
     *      ( [t] )             -->    ...
     * ]
     * </code></pre>
     * 该函数的输入通常是一个矩阵，这个方法会将2阶矩阵压缩成1阶列表：
     *
     * 1. 执行单元素的一部返回结果，得到 List<T>
     * 2. 针对该结果执行组合操作，将 List<List<T>> 转换成 List<T>
     * 3. 最终的结果就是一个单列表，[[], [], []] => []
     *
     * @param futures List<Future<List<T>>> 输入的异步结果，结果内是 List<T>
     * @param <T>     输入类型T
     *
     * @return Future<List < T>> 返回执行过的压缩结果
     */
    public static <T> Future<List<T>> compressL(final List<Future<List<T>>> futures) {
        return ThenL.compressL(futures);
    }


    /**
     * 哈希表压缩函数，二阶转一阶
     * <pre><code>
     * [
     *      ( [k=t,k=t,k=t] )   -->     ...
     *      ( [k=t] )           -->     ...     --> ( [k=t,k=t,k=t,   k=t,   k=t,k=t] )
     *      ( [k=t,k=t] )       -->     ...
     * ]
     * </code></pre>
     * 该函数输入有两层容器，第一层是 List，第二层是 ConcurrentMap，两层容器为 2 阶数据结构，所以该输入不能单纯看成
     * 矩阵，代码逻辑如：
     *
     * 1. 执行 List 的每一个元素（ Future<ConcurrentMap<String, T>> ) 一部操作得到结果 ( ConcurrentMap<String, T> ).
     * 2. 一个最终结果组合成集合 ( List<ConcurrentMap<String, T>> ) 通过计算得到最终的 ( ConcurrentMap<String, T> ).
     * 默认版本组合函数直接调用哈希表的 addAll 操作
     * 3. 最终的结果为：[[k=t],[k=t]] => [k=t, k=t]
     *
     * @param futures    List<Future<ConcurrentMap<String, T>>> 输入的异步结果，结果内是 ConcurrentMap<String, T>
     * @param combinerOf BiFunction<T, T, T> 组合函数，用于将 ConcurrentMap<String, T> 组合成 ConcurrentMap<String, T>
     * @param <T>        输入类型T
     *
     * @return Future<Map < String, T>> 返回执行过的压缩结果
     */
    public static <T> Future<ConcurrentMap<String, T>> compressM(final List<Future<ConcurrentMap<String, T>>> futures,
                                                                 final BinaryOperator<T> combinerOf) {
        return ThenM.compressM(futures, combinerOf);
    }

    public static Future<ConcurrentMap<String, JsonArray>> compressM(final List<Future<ConcurrentMap<String, JsonArray>>> futures) {
        return ThenM.compressM(futures, (original, latest) -> original.addAll(latest));
    }

    // ---------------------------------------------------- 原子函数 ----------------------------------------------------
    /*
     * 全部 p 前缀的原子函数
     * - parallel: 并行编排
     * - passion:  串行编排
     * - pool:     池化（缓存函数）
     */
    public static <T> Future<T> parallel(final T input, final Set<Function<T, Future<T>>> executors) {
        return Atomic.parallel(input, executors);
    }

    public static <T> Future<T> parallel(final T input, final List<Function<T, Future<T>>> executors) {
        return Atomic.parallel(input, new HashSet<>(executors));
    }

    public static <T> Future<T> parallel(final T input, final Function<T, Future<T>>... executors) {
        return Atomic.parallel(input, new HashSet<>(Arrays.asList(executors)));
    }

    public static <T> Future<T> passion(final T input, final List<Function<T, Future<T>>> executors) {
        return Atomic.passion(input, executors);
    }

    public static <T> Future<T> passion(final T input, final Function<T, Future<T>>... executors) {
        return Atomic.passion(input, Arrays.asList(executors));
    }

    @HLinking(refer = HMs.class)
    public static <K, V> V pool(final ConcurrentMap<K, V> pool, final K key, final Supplier<V> poolFn) {
        return HMs.pool(pool, key, poolFn);
    }

    @HLinking(refer = HMs.class)
    public static <V> V poolThread(final ConcurrentMap<String, V> pool, final Supplier<V> poolFn) {
        return HMs.poolThread(pool, poolFn);
    }

    @HLinking(refer = HMs.class)
    public static <V> V poolThread(final ConcurrentMap<String, V> pool, final Supplier<V> poolFn, final String key) {
        return HMs.poolThread(pool, poolFn, key);
    }

    // ---------------------------------------------------- 响应函数 ----------------------------------------------------
    // ------ ZeroException out
    public static void outZero(final boolean condition, final Annal logger, final Class<? extends ProgramException> zeroClass, final Object... args) throws ProgramException {
        if (condition) {
            Warning.outZero(logger, zeroClass, args);
        }
    }

    // ------ ZeroException to RunZeroException out
    public static void outUp(final ProgramActuator actuator, final Annal logger) {
        Wait.wrapper(logger, actuator);
    }

    // ------ RunZeroException out
    /* Old Style */
    public static void outUp(final boolean condition, final Annal logger, final Class<? extends AbstractException> upClass, final Object... args) {
        if (condition) {
            Warning.outUp(logger, upClass, args);
        }
    }

    /* New Style */
    public static void outUp(final boolean condition, final Class<? extends UpException> upClass, final Object... args) {
        if (condition) {
            Warning.outUp(upClass, args);
        }
    }

    /**
     * New structure for exception out ( RuntimeException )
     * UpException | WebException supported
     * --
     * outUp + outWeb
     *
     * @param condition The condition for throwing
     * @param clazz     The exception clazz here ( because all the exception class first argument type is Class<?>, it means
     *                  you can initialize logger inner this method instead of input
     * @param args      dynamic objects for exception
     */
    public static void out(final boolean condition, final Class<?> clazz, final Object... args) {
        if (condition) {
            Warning.out(clazz, args);
        }
    }

    // ------ WebException out
    /* Old Style */
    public static void outWeb(final boolean condition, final Annal logger, final Class<? extends WebException> webClass, final Object... args) {
        if (condition) {
            Warning.outWeb(logger, webClass, args);
        }
    }

    /* New Style */
    public static void outWeb(final boolean condition, final Class<? extends WebException> webClass, final Object... args) {
        if (condition) {
            Warning.outWeb(webClass, args);
        }
    }

    /*
     * Program Used:
     * outOr / outQr
     * 1. This point should be first line of one method:
     *    if(condition){
     *        throw out exception of `_412NullValueException.class`
     *    }
     * 2. This point should be QR result checking after Future<T>
     *    Future.compose(record -> {
     *        if(Objects.isNull(record)){
     *             throw out exception of `_412NullValueException.class`
     *        }
     *    })
     *
     * The default message:
     * 1. Or -> [ Program ] Null Input
     * 2. Qr -> [ Program ] Null Record in database
     * */
    public static <T> void outOr(final T condition, final Class<?> clazz, final String message) {
        Warning.outOr(condition, clazz, message);
    }

    public static <T> void outOr(final T condition, final Class<?> clazz) {
        outOr(condition, clazz, VMessage.PROGRAM_NULL);
    }

    public static <T> void outQr(final T condition, final Class<?> clazz) {
        outOr(condition, clazz, VMessage.PROGRAM_QR);
    }
    // ------ Jvm Safe

    public static void safeJvm(final ExceptionActuator actuator, final Annal logger) {
        Wall.jvmVoid(actuator, logger);
    }

    public static void safeJvm(final ExceptionActuator actuator) {
        Wall.jvmVoid(actuator, null);
    }

    public static <T> T safeJvm(final ExceptionSupplier<T> supplier, final Annal logger) {
        return Wall.jvmReturn(supplier, logger);
    }

    public static <T> T safeJvm(final ExceptionSupplier<T> supplier) {
        return Wall.jvmReturn(supplier, null);
    }

    public static <T> T orJvm(final ExceptionSupplier<T> supplier, final Object... input) {
        return ZeroErr.getJvm(null, supplier, input);
    }

    public static <T> T orJvm(final T defaultValue, final ExceptionSupplier<T> supplier, final Object... input) {
        return ZeroErr.getJvm(defaultValue, supplier, input);
    }

    // ------ Zero Safe
    public static <T> T orZero(final ProgramSupplier<T> supplier, final Annal logger) {
        return Wall.zeroReturn(supplier, logger);
    }

    public static void safeZero(final ProgramActuator actuator, final Annal logger) {
        Wall.zeroVoid(actuator, logger);
    }

    // ------ Null Safe
    public static void safeNull(final Actuator actuator, final Object... input) {
        ZeroErr.exec(actuator, input);
    }

    public static <T> void safeNull(final Consumer<T> consumer, final T input) {
        ZeroErr.exec(consumer, input);
    }

    /*
     * 修改原 get 前缀为 or，代表有可能得情况，这部分API改动量巨大，且和if可能会有些许重复
     * 重复部分暂时先维持原始信息，等之后合并
     */
    public static <T> T orNull(final Supplier<T> supplier, final Object... input) {
        return ZeroErr.get(null, supplier, input);
    }

    public static <T> T orNull(final T defaultValue, final Supplier<T> supplier, final Object... input) {
        return ZeroErr.get(defaultValue, supplier, input);
    }

    public static <T> T orNull(final T defaultValue, final Supplier<T> supplier) {
        return Wall.execReturn(supplier, defaultValue);
    }

    public static <T> T orEmpty(final Supplier<T> supplier, final String... input) {
        return ZeroErr.getEmpty(null, supplier, input);
    }

    public static <T> T orEmpty(final T defaultValue, final Supplier<T> supplier, final String... input) {
        return ZeroErr.getEmpty(defaultValue, supplier, input);
    }

    // ------ Semi Safe
    public static void safeSemi(final boolean condition, final Annal logger, final Actuator tSupplier, final Actuator fSupplier) {
        Wall.exec(condition, logger, tSupplier, fSupplier);
    }

    public static void safeSemi(final boolean condition, final Annal logger, final Actuator tSupplier) {
        Wall.exec(condition, logger, tSupplier, null);
    }

    public static void safeSemi(final boolean condition, final Actuator tSupplier) {
        Wall.exec(condition, null, tSupplier, null);
    }

    public static <T> void safeSemi(final Supplier<T> supplier, final Consumer<T> consumer) {
        Wall.safeT(supplier, consumer);
    }

    public static <T> T orSemi(final boolean condition, final Annal logger, final Supplier<T> tSupplier, final Supplier<T> fSupplier) {
        return Wall.zeroReturn(() -> Wall.execZero(condition, tSupplier::get, fSupplier::get), logger);
    }

    public static <T> T orSemi(final boolean condition, final Annal logger, final Supplier<T> tSupplier) {
        return Wall.zeroReturn(() -> Wall.execZero(condition, tSupplier::get, null), logger);
    }

    public static <T> T orSemi(final boolean condition, final ProgramSupplier<T> tSupplier, final ProgramSupplier<T> fSupplier) throws ProgramException {
        return Wall.execZero(condition, tSupplier, fSupplier);
    }

    // ------ Specification for JsonFormat
    public static <T> T outRun(final Supplier<T> supplier, final Class<? extends AbstractException> runCls, final Object... args) {
        return Warning.execRun(supplier, runCls, args);
    }

    public static <T> void verifyJObject(final JsonObject data, final ProgramBiConsumer<T, String> fnIt) throws ProgramException {
        Wall.execZero(data, fnIt);
    }

    public static <T> void verifyJArray(final JsonArray dataArray, final Class<T> clazz, final ProgramBiConsumer<T, Integer> fnIt) throws ProgramException {
        Wall.execZero(dataArray, clazz, fnIt);
    }

    public static <T> void verifyJArray(final JsonArray dataArray, final ProgramBiConsumer<T, String> fnIt) throws ProgramException {
        Wall.execZero(dataArray, fnIt);
    }

    // ------ Must throw out exception in these two methods
    public static void safeRun(final Actuator actuator, final Annal logger) {
        Warning.outRun(actuator, logger);
    }

    public static void safeZero(final ProgramActuator actuator, final Object... input) throws ProgramException {
        ZeroErr.execZero(actuator, input);
    }


    // ------- War ( New Version )


    public static <T> Future<T> unbox(final Consumer<Promise<T>> consumer) {
        return Wait.then(consumer);
    }

    public static <T> Future<T> unbox(final Object result, final Promise<T> future, final Throwable ex) {
        return Wait.then(result, future, ex);
    }

    public static <T> Future<T> error(final Class<? extends WebException> clazz, final Object... args) {
        return War.thenError(clazz, args);
    }

    public static <T> Future<T> error(final Class<?> clazz, final String sigma, final Supplier<Future<T>> supplier) {
        return War.thenError(clazz, sigma, supplier);
    }

    // ---------------- Arrange Async Future ----------------------
    /*
     * This arrange part will replace `thenCombine?` method. Here are detail readme information:
     *
     * 1. Flag
     *    Here the flag identified the return value of internal ( Generic T )
     * -  A:        JsonArray
     * -  J:        JsonObject
     * -  T:        Generice <T>
     * -  M:        Map
     * -  L:        List
     *
     * 2. Prefix
     * - combine,   From element to container, this situation often happens as following
     *              1) The element of collection contains async operation.
     *              2) The result should be collection and major thread must wait for each element async operation finished.
     * - comic,     Expand element to multi, this situation often happens as following
     *              1) The element of collection is single type.
     *              2) The single type will generate multi elements into collection formed
     * - compress,  Compress collection to single, this situation often happens as following
     *              1) The collection has 2 layers and each element is also another collection.
     *              2) This kind of API will compress the collection into 1 layer ( single collection ).
     *
     * 3. The mark of data structure
     *
     *      o  -  Pure element type without any attached information.
     *            If there are more types, I'll use o1, o2, o3.
     *     [o] - 「Container」Collection type and the element type is o.
     *     (o) - 「Container」Async type and the element type is o.
     *     fx  -  Consumer Function
     */


    // ------ Function Processing for Output
    /*
     * 1）wrap / wrapAsync：执行封装
     * 2）wrapJ：最终返回的是 Future<JsonObject>,  JS = Json Sync，同步返回
     * 3）wrapB: 最终返回的是 Future<Boolean>
     * 4）wrapTo / wrapOn: 最终返回的是 Function，且返回值是 Future<JsonObject>
     * --- 只有异步，没有同步
     *
     * To:  T -> mount( field = T )
     * On:  T -> T ( field = mount )
     */
    public static <T> T wrap(final ErrorSupplier<T> supplier, final T defaultValue) {
        return Wait.wrapper(supplier, defaultValue);
    }

    public static <T> Future<T> wrapAsync(final ErrorSupplier<Future<T>> supplier, final T defaultValue) {
        return Wait.wrapperAsync(supplier, defaultValue);
    }

    // bool -> json
    public static Future<JsonObject> wrapJ(final String field, final boolean result) {
        return Future.succeededFuture(Wander.wrapJ(field, result));
    }

    public static JsonObject wrapJS(final String field, final boolean result) {
        return Wander.wrapJ(field, result);
    }

    public static Future<JsonObject> wrapJ(final boolean result) {
        return Future.succeededFuture(Wander.wrapJ(KName.RESULT, result));
    }

    // JsonArray -> json
    public static Future<JsonObject> wrapJ(final String field, final JsonArray data) {
        return Future.succeededFuture(Wander.wrapJ(field, data));
    }

    public static Future<JsonObject> wrapJ(final String field, final JsonObject data) {
        return Future.succeededFuture(Wander.wrapJ(field, data));
    }

    public static Future<JsonObject> wrapJ(final JsonArray data) {
        return Future.succeededFuture(Wander.wrapJ(KName.DATA, data));
    }

    public static Future<JsonObject> wrapJ(final JsonArray data, final JsonObject config) {
        return Future.succeededFuture(Wander.wrapJ(KName.DATA, data, config));
    }

    public static Future<JsonObject> wrapJ(final String field, final JsonArray data, final JsonObject config) {
        return Future.succeededFuture(Wander.wrapJ(field, data, config));
    }

    // json -> bool
    public static Future<Boolean> wrapB(final String field, final JsonObject input) {
        return Future.succeededFuture(Wander.wrapB(field, input));
    }

    public static Future<Boolean> wrapB(final JsonObject input) {
        return Future.succeededFuture(Wander.wrapB(KName.RESULT, input));
    }

    // json -> data( field = json )
    public static <T> Function<T, Future<JsonObject>> wrapTo(final String field, final JsonObject data) {
        return t -> Future.succeededFuture(Wander.wrapTo(field, data).apply(t));
    }

    // json -> json ( field = data )
    public static <T> Function<JsonObject, Future<JsonObject>> wrapOn(
        final String field, final Function<T, Future<JsonObject>> executor) {
        return Wander.wrapOn(field, executor);
    }

    public static <T> Function<JsonObject, Future<JsonObject>> wrapTree(
        final String field, final Function<T, Future<JsonObject>> executor) {
        return Wander.wrapTree(field, false, executor);
    }

    public static <T> Function<JsonObject, Future<JsonObject>> wrapTree(
        final String field, final boolean deeply, final Function<T, Future<JsonObject>> executor) {
        return Wander.wrapTree(field, deeply, executor);
    }

    public static Future<JsonObject> wrapWeb(final JsonObject json, final String field) {
        return Wander.wrapWeb(json, field);
    }

    public static Function<JsonObject, Future<JsonObject>> wrapWeb(final String field) {
        return json -> wrapWeb(json, field);
    }

    // ------ 防御式专用API
    /*
     * smart 功能未开启之前的检查必备
     * 1) ifString - 检查JsonObject中的 JsonArray / JsonObject，转换成 String
     *    ifStrings - 检查JsonArray中....
     * 上述方法对应的逆方法
     *    ifJObject
     *    ifJArray
     * 2) ifPage - 分页专用方法规范：list / count 属性，特定场景使用
     * 3) ifMerge - 合并双对象专用
     * 4) ifField - 提取字段执行专用，存在一个单字段消费链接专用的API，生成 Consumer<JsonObject>
     * 5) ifCopy / ifCopies - 复制专用，由于复制的第二参和第三参有可能会引起重载时JVM的错误判断，所以采用单复数模式
     * 6) ifDefault - 原来的 ifJValue
     * 7) 扩展非空检查专用方法
     *    ifNil     - 异步空检查             ofNil  默认值异步
     *    ifNull    - 同步空检查             ofNull 默认值异步
     *    ifEmpty   - 异步集合检查（无同步）
     */
    public static JsonObject ifString(final JsonObject json, final String... fields) {
        Arrays.stream(fields).forEach(field -> Wag.ifString(json, field));
        return json;
    }

    public static Function<JsonObject, Future<JsonObject>> ifString(final String... fields) {
        return json -> Future.succeededFuture(ifString(json, fields));
    }

    public static JsonArray ifStrings(final JsonArray array, final String... fields) {
        Ut.itJArray(array).forEach(json -> ifString(json, fields));
        return array;
    }

    public static Function<JsonArray, Future<JsonArray>> ifStrings(final String... fields) {
        return array -> Future.succeededFuture(ifStrings(array, fields));
    }

    public static JsonObject ifJObject(final JsonObject json, final String... fields) {
        Arrays.stream(fields).forEach(field -> Wag.ifJson(json, field));
        return json;
    }

    public static Function<JsonObject, Future<JsonObject>> ifJObject(final String... fields) {
        return json -> Future.succeededFuture(ifJObject(json, fields));
    }

    public static JsonArray ifJArray(final JsonArray array, final String... fields) {
        Ut.itJArray(array).forEach(json -> ifJObject(json, fields));
        return array;
    }

    public static Function<JsonArray, Future<JsonArray>> ifJArray(final String... fields) {
        return array -> Future.succeededFuture(ifJArray(array, fields));
    }

    public static JsonObject ifDefault(final JsonObject record, final String field, final Object value) {
        return Wag.ifDefault(record, field, value);
    }

    public static Function<JsonObject, Future<JsonObject>> ifDefault(final String field, final Object value) {
        return record -> Future.succeededFuture(ifDefault(record, field, value));
    }

    // ======================= 和业务些许相关的复杂操作（特殊类API）

    public static JsonObject ifCopy(final JsonObject record, final String from, final String to) {
        return Wag.ifCopy(record, from, to);
    }

    public static Function<JsonObject, Future<JsonObject>> ifCopy(final String from, final String to) {
        return json -> Future.succeededFuture(ifCopy(json, from, to));
    }

    public static JsonObject ifCopies(final JsonObject target, final JsonObject source, final String... fields) {
        return Wag.ifCopies(target, source, fields);
    }

    public static Function<JsonObject, Future<JsonObject>> ifCopies(final JsonObject source, final String... fields) {
        return target -> Future.succeededFuture(ifCopies(target, source, fields));
    }

    /*
     * 「双态型」两种形态
     */
    public static JsonObject ifPage(final JsonObject pageData, final String... fields) {
        return Wag.ifPage(pageData, fields);
    }

    public static Function<JsonObject, Future<JsonObject>> ifPage(final String... fields) {
        return pageData -> Future.succeededFuture(ifPage(pageData, fields));
    }

    // 单模式特殊方法（无第二形态）
    public static <T> Function<T, Future<JsonObject>> ifMerge(final JsonObject input) {
        return t -> Future.succeededFuture(Wag.ifField(input, null, t));
    }

    public static <T> Function<T, Future<JsonObject>> ifField(final JsonObject input, final String field) {
        return t -> Future.succeededFuture(Wag.ifField(input, field, t));
    }

    public static <T, V> Consumer<JsonObject> ifField(final String field, final Function<V, T> executor) {
        return input -> {
            final JsonObject inputJ = Ut.valueJObject(input);
            if (inputJ.containsKey(field)) {
                final Object value = inputJ.getValue(field);
                if (Objects.nonNull(value)) {
                    executor.apply((V) value);
                }
            }
        };
    }

    // ------------------------------- 异步处理 -----------------
    // 直接包装
    public static <I, T> Function<I, Future<T>> ifNil(final Function<I, Future<T>> executor) {
        return Wash.ifNil(executor);
    }

    // 默认值同步
    public static <I, T> Function<I, Future<T>> ifNil(final Supplier<T> supplier, final Supplier<Future<T>> executor) {
        return ifNil(supplier, (i) -> executor.get() /* Function */);
    }


    public static <I> Function<I, Future<JsonObject>> ifJObject(final Supplier<JsonObject> executor) {
        return ofJObject(() -> Future.succeededFuture(executor.get()));
    }

    public static <I> Function<I, Future<JsonObject>> ifJObject(final Function<I, JsonObject> executor) {
        return ofJObject(item -> Future.succeededFuture(executor.apply(item)));
    }

    public static <I> Function<I, Future<JsonArray>> ifJArray(final Supplier<JsonArray> executor) {
        return ofJArray(() -> Future.succeededFuture(executor.get()));
    }

    public static <I> Function<I, Future<JsonArray>> ifJArray(final Function<I, JsonArray> executor) {
        return ofJArray(item -> Future.succeededFuture(executor.apply(item)));
    }

    public static <I, T> Function<I, Future<T>> ifNil(final Supplier<T> supplier, final Function<I, Future<T>> executor) {
        return input -> ofNil(() -> Future.succeededFuture(supplier.get()), executor).apply(input);
    }

    // 默认值异步
    public static <I, T> Function<I, Future<T>> ofNil(final Supplier<Future<T>> supplier, final Supplier<Future<T>> executor) {
        return ofNil(supplier, i -> executor.get() /* Function */);
    }

    public static <I, T> Function<I, Future<T>> ofNil(final Supplier<Future<T>> supplier, final Function<I, Future<T>> executor) {
        return Wash.ifNil(supplier, executor);
    }

    // 变种（全异步，默认值同步）JsonObject
    public static <I> Function<I, Future<JsonObject>> ofJObject(final Function<I, Future<JsonObject>> executor) {
        return ofNil(() -> Future.succeededFuture(new JsonObject()), executor);
    }

    public static <I> Function<I, Future<JsonObject>> ofJObject(final Supplier<Future<JsonObject>> executor) {
        return ofJObject(i -> executor.get());
    }

    // 变种（全异步，默认值同步）JsonArray
    public static <I> Function<I, Future<JsonArray>> ofJArray(final Function<I, Future<JsonArray>> executor) {
        return ofNil(() -> Future.succeededFuture(new JsonArray()), executor);
    }

    public static <I> Function<I, Future<JsonArray>> ofJArray(final Supplier<Future<JsonArray>> executor) {
        return ofJArray(i -> executor.get());
    }

    // ------------------------------- 同步处理 -----------------
    public static <I, T> Function<I, Future<T>> ifNul(final Function<I, T> executor) {
        return Wash.ifNul(executor);
    }

    // 默认值同步
    public static <I, T> Function<I, Future<T>> ifNul(final Supplier<T> supplier, final Supplier<T> executor) {
        return ifNul(supplier, (i) -> executor.get() /* Function */);
    }

    public static <I, T> Function<I, Future<T>> ifNul(final Supplier<T> supplier, final Function<I, T> executor) {
        return input -> ofNul(() -> Future.succeededFuture(supplier.get()), executor).apply(input);
    }

    // 默认值异步
    public static <I, T> Function<I, Future<T>> ofNul(final Supplier<Future<T>> supplier, final Supplier<T> executor) {
        return ofNul(supplier, i -> executor.get() /* Function */);
    }

    public static <I, T> Function<I, Future<T>> ofNul(final Supplier<Future<T>> supplier, final Function<I, T> executor) {
        return Wash.ifNul(supplier, executor);
    }

    public static <T> Function<T[], Future<T[]>> ifEmpty(final Function<T[], Future<T[]>> executor) {
        return Wash.ifEmpty(executor);
    }
}
