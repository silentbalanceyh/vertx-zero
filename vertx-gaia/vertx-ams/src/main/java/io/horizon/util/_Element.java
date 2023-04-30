package io.horizon.util;

import io.horizon.eon.VName;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lang : 2023/4/28
 */
class _Element extends _EDS {
    /**
     * 针对数组执行拷贝，支持泛型数组的拷贝动作
     *
     * @param array   数组
     * @param element 元素
     * @param <T>     泛型
     *
     * @return 拷贝后的数组
     */
    public static <T> T[] elementAdd(final T[] array, final T element) {
        return CAdd.add(array, element);
    }

    /**
     * （引用修改）
     * 针对 JsonArray 对象执行按 field 匹配的添加操作
     * 直接修改当前 JsonArray 中 field 和传入对象匹配的方法，该操作属于
     * Save级，即可直接执行修改操作
     *
     * @param array JsonArray
     * @param json  JsonObject
     * @param field 字段名
     *
     * @return JsonArray
     */
    public static JsonArray elementSave(final JsonArray array, final JsonObject json, final String field) {
        return CSave.save(array, json, field);
    }

    /**
     * （引用修改）
     * 针对 JsonArray 对象执行按 field 匹配执行批量添加操作
     * 直接修改当前 JsonArray 中 field 和传入对象匹配的方法，该操作属于
     * Save级，即可直接执行修改操作，且添加过程中只针对 json 中的 JsonObject 合法对象
     *
     * @param array JsonArray
     * @param json  JsonObject
     * @param field 字段名
     *
     * @return JsonArray
     */
    public static JsonArray elementSave(final JsonArray array, final JsonArray json, final String field) {
        return CSave.save(array, json, field);
    }

    /**
     * （引用修改）
     * 针对 List<T> 集合执行按 field 匹配执行保存操作，反射提取 field 中的值
     *
     * @param list   集合
     * @param entity 实体
     * @param field  字段名
     * @param <T>    泛型
     *
     * @return List<T>
     */
    public static <T> List<T> elementSave(final List<T> list, final T entity, final String field) {
        return CSave.save(list, entity, item -> HInstance.get(item, field));
    }

    /**
     * （引用修改）
     * 针对 List<T> 集合执行按 field 匹配执行保存操作，反射提取 field 中的值
     *
     * @param list    集合
     * @param entity  实体
     * @param fieldFn 字段提取函数（非标准反射）
     * @param <T>     泛型
     *
     * @return List<T>
     */
    public static <T> List<T> elementSave(final List<T> list, final T entity, final Function<T, String> fieldFn) {
        return CSave.save(list, entity, fieldFn);
    }
    // ----------------------- 元素子集运算 ---------------------

    /**
     * 针对 JsonObject 对象按 fields 属性执行子集提取
     *
     * @param input  JsonObject
     * @param fields 字段名
     *
     * @return JsonObject
     */
    public static JsonObject elementSubset(final JsonObject input, final String... fields) {
        return CSubset.subset(input, new HashSet<>(Arrays.asList(fields))); // ArrayL.subset(input, fields);
    }

    /**
     * 针对 JsonObject 对象按 fields 属性执行子集提取
     *
     * @param input  JsonObject
     * @param fields 字段名
     *
     * @return JsonArray
     */
    public static JsonObject elementSubset(final JsonObject input, final Set<String> fields) {
        return CSubset.subset(input, fields); // ArrayL.subset(input, set);
    }

    /**
     * 针对 JsonArray 对象按 fields 属性执行子集提取
     *
     * @param input  JsonArray
     * @param fields 字段名
     *
     * @return JsonArray
     */
    public static JsonArray elementSubset(final JsonArray input, final Set<String> fields) {
        return CSubset.subset(input, fields); // ArrayL.subset(input, set);
    }

    /**
     * 针对 JsonArray 对象按 fields 属性执行子集提取
     *
     * @param input   JsonArray
     * @param matchFn 专用执行函数
     *
     * @return JsonArray
     */
    public static JsonArray elementSubset(final JsonArray input, final Function<JsonObject, Boolean> matchFn) {
        return CSubset.subset(input, matchFn::apply); // ArrayL.subset(input, fnFilter);
    }

    // ------------------------ 集合基本交、并、差运算 ------------------------

    /**
     * 针对 Set 集合执行交集运算
     *
     * @param left  左集合
     * @param right 右集合
     * @param <T>   泛型
     *
     * @return 左右集合交集
     */
    public static <T> Set<T> elementIntersect(final Set<T> left, final Set<T> right) {
        return CArithmetic.intersect(left, right);
    }

    /**
     * 针对 List 集合执行交集运算
     *
     * @param left  左集合
     * @param right 右集合
     * @param <T>   泛型
     *
     * @return 左右集合交集
     */
    public static <T> List<T> elementIntersect(final List<T> left, final List<T> right) {
        return new ArrayList<>(elementIntersect(new HashSet<>(left), new HashSet<>(right)));
    }

    /**
     * 针对 Set 集合执行交集运算，运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param left  左集合
     * @param right 右集合
     * @param fnGet 专用执行函数
     * @param <T>   泛型
     * @param <V>   泛型
     *
     * @return 左右集合交集
     */
    public static <T, V> Set<T> elementIntersect(final Set<T> left, final Set<T> right, final Function<T, V> fnGet) {
        return CArithmetic.intersect(left, right, fnGet);
    }

    /**
     * 针对 List 集合执行交集运算，运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param left  左集合
     * @param right 右集合
     * @param fnGet 专用执行函数
     * @param <T>   泛型
     * @param <V>   泛型
     *
     * @return 左右集合交集
     */
    public static <T, V> List<T> elementIntersect(final List<T> left, final List<T> right, final Function<T, V> fnGet) {
        return new ArrayList<>(elementIntersect(new HashSet<>(left), new HashSet<>(right), fnGet));
    }

    /**
     * 针对 Set 集合执行并集运算
     *
     * @param left  左集合
     * @param right 右集合
     * @param <T>   泛型
     *
     * @return 左右集合并集
     */
    public static <T> Set<T> elementUnion(final Set<T> left, final Set<T> right) {
        return CArithmetic.union(left, right);
    }

    /**
     * 针对 List 集合执行并集运算
     *
     * @param left  左集合
     * @param right 右集合
     * @param <T>   泛型
     *
     * @return 左右集合并集
     */
    public static <T> List<T> elementUnion(final List<T> left, final List<T> right) {
        return new ArrayList<>(elementUnion(new HashSet<>(left), new HashSet<>(right)));
    }

    /**
     * 针对 Set 集合执行并集运算，运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param left  左集合
     * @param right 右集合
     * @param fnGet 专用执行函数
     * @param <T>   泛型
     * @param <V>   泛型
     *
     * @return 左右集合并集
     */
    public static <T, V> Set<T> elementUnion(final Set<T> left, final Set<T> right, final Function<T, V> fnGet) {
        return CArithmetic.union(left, right, fnGet);
    }

    /**
     * 针对 List 集合执行并集运算，运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param left  左集合
     * @param right 右集合
     * @param fnGet 专用执行函数
     * @param <T>   泛型
     * @param <V>   泛型
     *
     * @return 左右集合并集
     */
    public static <T, V> List<T> elementUnion(final List<T> left, final List<T> right, final Function<T, V> fnGet) {
        return new ArrayList<>(elementUnion(new HashSet<>(left), new HashSet<>(right), fnGet));
    }

    /**
     * 针对 Set 集合执行差集运算，注意这里是 subtrahend - minuend，有顺序
     *
     * @param subtrahend 被减数
     * @param minuend    减数
     * @param <T>        泛型
     *
     * @return 差集
     */
    public static <T> Set<T> elementDiff(final Set<T> subtrahend, final Set<T> minuend) {
        return CArithmetic.diff(subtrahend, minuend);
    }

    /**
     * 针对 List 集合执行差集运算，注意这里是 subtrahend - minuend，有顺序
     *
     * @param subtrahend 被减数
     * @param minuend    减数
     * @param <T>        泛型
     *
     * @return 差集
     */
    public static <T> List<T> elementDiff(final List<T> subtrahend, final List<T> minuend) {
        return new ArrayList<>(elementDiff(new HashSet<>(subtrahend), new HashSet<>(minuend)));
    }

    /**
     * 针对 Set 集合执行差集运算，注意这里是 subtrahend - minuend，有顺序
     * 运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param subtrahend 被减数
     * @param minuend    减数
     * @param fnGet      专用执行函数
     * @param <T>        泛型
     * @param <V>        泛型
     *
     * @return 差集
     */
    public static <T, V> Set<T> elementDiff(final Set<T> subtrahend, final Set<T> minuend, final Function<T, V> fnGet) {
        return CArithmetic.diff(subtrahend, minuend, fnGet);
    }

    /**
     * 针对 List 集合执行差集运算，注意这里是 subtrahend - minuend，有顺序
     * 运算时按 fnGet 中提取的值执行基础规则，这种一般是非基础数据集合
     *
     * @param subtrahend 被减数
     * @param minuend    减数
     * @param fnGet      专用执行函数
     * @param <T>        泛型
     * @param <V>        泛型
     *
     * @return 差集
     */
    public static <T, V> List<T> elementDiff(final List<T> subtrahend, final List<T> minuend, final Function<T, V> fnGet) {
        return new ArrayList<>(elementDiff(new HashSet<>(subtrahend), new HashSet<>(minuend), fnGet));
    }

    // ------------------------ 分组运算 ------------------------

    /**
     * 针对 Collection 集合执行分组运算，分组过程最终处理成 ConcurrentMap，分组过程支持自定义 key 和 value
     *
     * @param collection 待分组的集合
     * @param keyFn      key 生成函数
     * @param valueFn    value 生成函数
     * @param <K>        key 类型
     * @param <V>        value 类型
     * @param <E>        待分组集合元素类型
     *
     * @return 分组结果
     */
    public static <K, V, E> ConcurrentMap<K, List<V>> elementGroup(final Collection<E> collection, final Function<E, K> keyFn, final Function<E, V> valueFn) {
        return CGroup.group(collection, keyFn, valueFn);
    }

    /**
     * 针对 Collection 集合执行分组运算，分组过程最终处理成 ConcurrentMap，分组过程支持自定义 key
     * 此时 value 直接使用元素类型代替
     *
     * @param collection 待分组的集合
     * @param keyFn      key 生成函数
     * @param <K>        key 类型
     * @param <E>        待分组集合元素类型
     *
     * @return 分组结果
     */
    public static <K, E> ConcurrentMap<K, List<E>> elementGroup(final Collection<E> collection, final Function<E, K> keyFn) {
        return CGroup.group(collection, keyFn, item -> item);
    }

    /**
     * 针对 JsonArray 集合执行分组运算，分组过程最终处理成 ConcurrentMap，分组过程使用某个 field 执行
     * 该操作只针对 JsonArray 中类型为 JsonObject 的元素相关信息
     *
     * @param source 待分组的JsonArray集合
     * @param field  分组使用的属性名
     *
     * @return 分组结果
     */
    public static ConcurrentMap<String, JsonArray> elementGroup(final JsonArray source, final String field) {
        return CGroup.group(source, item -> item.getString(field));
    }

    /**
     * 针对 JsonArray 集合执行分组运算，分组过程最终处理成 ConcurrentMap，按照某个尺寸折叠分组
     * 该操作主要针对（低阶转高阶）：
     * <pre><code>
     *     [E,E,E,E,E] ->
     *     [
     *        [E,E,E],
     *        [E,E,E]
     *     ]
     * </code></pre>
     *
     * @param source 待分组的JsonArray集合
     * @param size   分组尺寸
     *
     * @return 分组结果
     */
    public static List<JsonArray> elementGroup(final JsonArray source, final Integer size) {
        return CGroup.group(source, size);
    }

    /**
     * 针对 JsonArray 集合执行分组运算，分组过程最终处理成 ConcurrentMap，分组过程使用某个 groupFn 执行分组基础字段计算逻辑
     *
     * @param source  待分组的JsonArray集合
     * @param groupFn 分组基础字段计算逻辑
     *
     * @return 分组结果
     */
    public static ConcurrentMap<String, JsonArray> elementGroup(final JsonArray source, final Function<JsonObject, String> groupFn) {
        return CGroup.group(source, groupFn);
    }

    // ------------------------ 拉平运算 ------------------------

    /**
     * 拉平操作，针对第一集合 List<E> 和第二集合 List<S> 执行 zipFn 的拉平函数处理
     * 拉平后最终返回一个新的 List<T> 集合
     *
     * <pre><code>
     *     [ F, F, F, F, F ] + [ S, S, S, S, S ] -> [ T, T, T, T, T ]
     * </code></pre>
     *
     * 尺寸以第一个集合的尺寸为基础，类似左连接的方式进行拉平
     *
     * @param first  第一集合
     * @param second 第二集合
     * @param zipFn  拉平函数
     * @param <F>    第一集合元素类型
     * @param <S>    第二集合元素类型
     * @param <T>    拉平后的元素类型
     *
     * @return 拉平后的集合
     */
    public static <F, S, T> List<T> elementZip(final List<F> first, final List<S> second, final BiFunction<F, S, T> zipFn) {
        return CZip.zip(first, second, zipFn);
    }

    /**
     * 拉平操作，针对第一集合 List<E> 和第二集合 List<S> 执行拉平处理，拉平之后形成一个
     * 哈希表，key 为第一集合元素，value 为第二集合元素
     *
     * @param keys   key 集合
     * @param values value 集合
     * @param <F>    key 类型
     * @param <T>    value 类型
     *
     * @return 拉平后的哈希表
     */
    public static <F, T> ConcurrentMap<F, T> elementZip(final List<F> keys, final List<T> values) {
        return CZip.zip(keys, values);
    }

    /**
     * 拉平操作，针对一个列表中的元素执行双属性的拉平
     * 最终拉平之后生成一个哈希表，key 为第一个属性，value 为第二个属性
     *
     * @param collection 待拉平的集合
     * @param keyFn      key 生成函数
     * @param valueFn    value 生成函数
     * @param <K>        key 类型
     * @param <V>        value 类型
     * @param <E>        待拉平集合元素类型
     *
     * @return 拉平后的哈希表
     */
    public static <K, V, E> ConcurrentMap<K, V> elementZip(final Collection<E> collection,
                                                           final Function<E, K> keyFn, final Function<E, V> valueFn) {
        return CZip.zip(collection, keyFn, valueFn);
    }

    /**
     * （重载）拉平操作，针对一个列表中的元素执行双属性的拉平
     * 最终拉平之后生成一个哈希表，key 为第一个属性，value 为第二个属性
     *
     * @param collection 待拉平的集合
     * @param keyFn      key 生成函数
     * @param valueFn    value 生成函数
     * @param <K>        key 类型
     * @param <V>        value 类型
     * @param <E>        待拉平集合元素类型
     *
     * @return 拉平后的哈希表
     */
    public static <K, V, E> ConcurrentMap<K, V> elementZip(final E[] collection,
                                                           final Function<E, K> keyFn, final Function<E, V> valueFn) {
        return CZip.zip(Arrays.asList(collection), keyFn, valueFn);
    }

    /**
     * 双哈希表的拉平操作，针对两个哈希表执行拉平叠加
     *
     * <pre><code>
     *     Map1: key = value
     *     Map2: value = element
     *     最终计算结果
     *     Map3: key = element
     * </code></pre>
     *
     * @param source 源哈希表
     * @param target 目标哈希表
     * @param <K>    源哈希表 key 类型
     * @param <T>    源哈希表 value 类型
     * @param <V>    目标哈希表 value 类型
     *
     * @return 拉平后的哈希表
     */
    public static <K, T, V> ConcurrentMap<K, V> elementZip(final ConcurrentMap<K, T> source,
                                                           final ConcurrentMap<T, V> target) {
        return CZip.zip(source, target);
    }

    // ------------------------ 映射运算 ------------------------

    /**
     * 映射函数，直接将一个 List<V> 中的值映射成
     * key = V 的格式，键使用 keyFn 生成，而 V 就是元素本身的类型
     *
     * @param dataList 数据列表
     * @param keyFn    key 生成函数
     * @param <K>      key 类型
     * @param <V>      value 类型
     *
     * @return 映射后的哈希表
     */
    public static <K, V> ConcurrentMap<K, V> elementMap(final List<V> dataList, final Function<V, K> keyFn) {
        return CMap.map(dataList, keyFn, item -> item);
    }

    /**
     * 映射函数，直接将一个 List<E> 中的值映射成
     * key = keyFn(E) value = valueFn(E) 的格式
     *
     * @param dataList 数据列表
     * @param keyFn    key 生成函数
     * @param valueFn  value 生成函数
     * @param <K>      key 类型
     * @param <V>      value 类型
     * @param <E>      数据列表元素类型
     *
     * @return 映射后的哈希表
     */
    public static <K, V, E> ConcurrentMap<K, V> elementMap(final List<E> dataList, final Function<E, K> keyFn, final Function<E, V> valueFn) {
        return CMap.map(dataList, keyFn, valueFn);
    }

    /**
     * 映射函数，直接将一个 JsonArray 对象按 field 的值生成映射（有重复值则以后入集合的值为主）
     *
     * @param dataArray 数据列表
     * @param field     字段名
     *
     * @return 映射后的哈希表
     */
    public static ConcurrentMap<String, JsonObject> elementMap(final JsonArray dataArray, final String field) {
        return CMap.map(dataArray, field);
    }

    /**
     * 映射函数，直接将一个 JsonArray 对象按 field 的值生成映射（有重复值则以后入集合的值为主）
     * 返回值为 JsonObject 类型
     *
     * @param dataArray 数据列表
     * @param field     字段名
     *
     * @return 映射后的哈希表
     */
    public static JsonObject elementMapJ(final JsonArray dataArray, final String field) {
        return HJson.toJObject(CMap.map(dataArray, field));
    }

    /**
     * 映射函数，直接将一个 JsonArray 对象按 field 的值生成映射（有重复值则以后入集合的值为主）
     * 此处存在 field -> to 的映射关系，即 field 的值映射到 to 字段的拷贝模式，处理过程中
     * 可执行内部转换实现协变型映射
     *
     * @param dataArray 数据列表
     * @param field     字段名
     * @param to        映射的目标字段
     * @param <T>       映射的目标字段类型
     *
     * @return 映射后的哈希表
     */
    public static <T> ConcurrentMap<String, T> elementMap(final JsonArray dataArray, final String field, final String to) {
        return CMap.map(dataArray, field, to);
    }

    // ------------------------ 连接运算 ------------------------

    /**
     * 连接两个JsonArray专用的连接函数
     *
     * @param source    源JsonArray
     * @param target    目标JsonArray
     * @param sourceKey 源JsonArray中的连接key
     * @param targetKey 目标JsonArray中的连接key
     *
     * @return 连接后的JsonArray
     */
    public static JsonArray elementJoin(final JsonArray source, final JsonArray target,
                                        final String sourceKey, final String targetKey) {
        return CJoin.join(source, target, sourceKey, targetKey);
    }

    /**
     * 连接两个JsonArray专用的连接函数
     *
     * @param source  源JsonArray
     * @param target  目标JsonArray
     * @param bothKey 源JsonArray和目标JsonArray中的连接key（同Key）
     *
     * @return 连接后的JsonArray
     */
    public static JsonArray elementJoin(final JsonArray source, final JsonArray target,
                                        final String bothKey) {
        return CJoin.join(source, target, bothKey, bothKey);
    }

    /**
     * 连接两个JsonArray专用的连接函数（都使用key）
     *
     * @param source 源JsonArray
     * @param target 目标JsonArray
     *
     * @return 连接后的JsonArray
     */
    public static JsonArray elementJoin(final JsonArray source, final JsonArray target) {
        return CJoin.join(source, target, VName.KEY, VName.KEY);
    }

    // ------------------------ 移除运算 ------------------------

    /**
     * 移除某个列表 List<T> 中所有元素 T 的 field 属性（设 null）
     *
     * @param list   列表
     * @param entity 元素
     * @param field  属性
     * @param <T>    列表元素类型
     *
     * @return 移除后的列表
     */
    public static <T> List<T> elementRemove(final List<T> list, final T entity, final String field) {
        return CRemove.remove(list, entity, item -> HInstance.get(item, field));
    }

    /**
     * 移除某个列表 List<T> 中所有元素 T 的 field 属性，带 field 检查条件函数
     *
     * @param list    列表
     * @param entity  元素
     * @param matchFn 检查条件函数
     * @param <T>     列表元素类型
     *
     * @return 移除后的列表
     */
    public static <T> List<T> elementRemove(final List<T> list, final T entity, final Function<T, String> matchFn) {
        return CRemove.remove(list, entity, matchFn);
    }

    // ------------------------ Count运算 ------------------------

    /**
     * Count 统计 input 中的 fields 字段的出现的次数
     *
     * @param input  输入列表
     * @param fields 字段列表
     *
     * @return 统计结果
     */
    public static ConcurrentMap<String, Integer> elementCount(final JsonArray input, final String... fields) {
        return CCount.count(input, Arrays.stream(fields).collect(Collectors.toSet()));
    }

    /**
     * （重载）Count 统计 input 中的 fields 字段的出现的次数
     *
     * @param input    输入列表
     * @param fieldSet 字段集合
     *
     * @return 统计结果
     */
    public static ConcurrentMap<String, Integer> elementCount(final JsonArray input, final Set<String> fieldSet) {
        return CCount.count(input, fieldSet);
    }

    /**
     * （重载）Count 统计 input 中的 fields 字段的出现的次数
     *
     * @param input      输入列表
     * @param fieldArray 字段数组
     *
     * @return 统计结果
     */
    public static ConcurrentMap<String, Integer> elementCount(final JsonArray input, final JsonArray fieldArray) {
        final Set<String> fieldSet = HaS.toSet(fieldArray);
        return CCount.count(input, fieldSet);
    }
}
