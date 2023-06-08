package io.vertx.up.fn;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;

/**
 * @author lang : 2023/4/27
 */
@SuppressWarnings("all")
class _Combine extends _Atomic {
    protected _Combine() {
    }
    /*

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
}
