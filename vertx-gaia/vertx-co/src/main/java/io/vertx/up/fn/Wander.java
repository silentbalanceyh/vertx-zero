package io.vertx.up.fn;

import io.aeon.runtime.H3H;
import io.horizon.specification.cloud.action.HCombiner;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import io.horizon.constant.em.Result;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * 原输出类专用函数，Ux.中的 attach, out, futureB, futureJA 等类似输出型方法，全部转移到该类中
 * 统一使用前缀 wrap 执行处理
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class Wander {
    /*
     * 构造 data = ? 的最终数据结构
     * 默认不分组，直接构造，config =
     * {
     *     "group": "field"
     * }
     * 这种模式下会根据 field 执行分组构造，最终的 data = JsonObject
     */
    static JsonObject wrapJ(final String key, final ClusterSerializable data) {
        return new JsonObject().put(key, data);
    }

    static JsonObject wrapJ(final String key, final JsonArray data, final JsonObject config) {
        final String group = Ut.valueString(config, KName.GROUP);
        final JsonObject response = new JsonObject();
        if (Ut.isNil(group)) {
            response.put(key, data);
        } else {
            final ConcurrentMap<String, JsonArray> grouped = Ut.elementGroup(data, group);
            response.put(KName.DATA, Ut.toJObject(grouped));
        }
        return response;
    }
    /*
     * 三种情况：
     * 1）field = value ->
     * {
     *     field: value
     * }
     * 2）field = true | field = false
     * 3）true|false  ->
     * {
     *     "RESULT": SUCCESS | FAILURE
     * }
     */

    /*
     * key = true | false
     *
     * -->
     *
     * key = SUCCESS | FAILURE
     */
    static JsonObject wrapJ(final String key, final boolean checked) {
        final Result response = checked ? Result.SUCCESS : Result.FAILURE;
        return new JsonObject().put(key, response.name());
    }

    /*
     * key = SUCCESS | FAILURE
     *
     * -->
     *
     * true | false
     */
    static Boolean wrapB(final String key, final JsonObject input) {
        final JsonObject inputJ = Ut.valueJObject(input);
        final String literal = inputJ.getString(key);
        final Result resultValue = Ut.toEnum(() -> literal, Result.class, Result.FAILURE);
        return Result.SUCCESS == resultValue;
    }

    /*
     * Item:  field = input
     * mount ----->  mount +        ------> mount
     *               field = input
     */
    static <T> Function<T, JsonObject> wrapTo(final String field, final JsonObject mount) {
        return input -> {
            // 默认返回引用
            final JsonObject mountJ = Ut.valueJObject(mount);
            if (Objects.nonNull(input)) {
                mountJ.put(field, input);
            }
            return mountJ;
        };
    }

    /*
     * Item:  field = T
     * mount --->  mount -> t    ------> mount
     *             t -> json
     *             field = json
     */
    @SuppressWarnings("unchecked")
    static <T> Function<JsonObject, Future<JsonObject>> wrapOn(
        final String field, final Function<T, Future<JsonObject>> executor) {
        return mount -> {
            if (Ut.isNil(field) ||
                !mount.containsKey(field) ||
                Objects.isNull(executor)) {
                // Nothing
                return Future.succeededFuture(mount);
            }
            final T value = (T) mount.getValue(field);
            if (Objects.isNull(value)) {
                // Nothing
                return Future.succeededFuture(mount);
            }
            // Function Processing
            return executor.apply(value).compose(data -> {
                if (Ut.notNil(data)) {
                    mount.put(field, data);
                }
                return Future.succeededFuture(mount);
            });
        };
    }

    /*
     * HCombiner Process On Web UI
     */
    @SuppressWarnings("unchecked")
    static Future<JsonObject> wrapWeb(final JsonObject json, final String field) {
        if (Ut.isNil(json) || !json.containsKey(field) || Ut.isNil(field)) {
            return Future.succeededFuture(json);
        }
        // Class<?>
        final Class<?> clazz = Ut.valueC(json, field, null);
        if (Objects.isNull(clazz)) {
            return Future.succeededFuture(json);
        }
        final HCombiner<JsonObject> combiner = (HCombiner<JsonObject>) H3H.CC_COMBINER.pick(() -> Ut.instance(clazz), clazz.getName());
        return combiner.combine(json);
    }

    /*
     * mount
     * {
     *     "a": "T",
     *     "e": "T"
     * }
     * ---> 所有的 T 类型的节点都会被处理掉并实现替换
     */
    static <T> Function<JsonObject, Future<JsonObject>> wrapTree(
        final String field, final boolean deeply, final Function<T, Future<JsonObject>> executor) {
        return mount -> {
            if (Ut.isNil(field) ||
                !mount.containsKey(field) ||
                Objects.isNull(executor)) {
                // Nothing
                return Future.succeededFuture(mount);
            }
            // 切换算法处理，执行 Map 操作
            final Object vSegment = mount.getValue(field);
            if (vSegment instanceof final JsonObject tree) {
                return wrapTree(tree, deeply, executor).compose(segmentData -> {
                    if (Ut.notNil(segmentData)) {
                        mount.put(field, segmentData);
                    }
                    return Future.succeededFuture(mount);
                });
            } else {
                return Future.succeededFuture(mount);
            }
        };
    }

    /*
     * 将树上所有节点的：field = T 转换成 field = T -> JsonObject，且支持
     * 递归和异步，属于深度计算算法，比原始的只处理两级层级更深，递归终止条件为原始数据不存在 JsonObject 节点
     * 注意递归终止是处理原始数据，这种计算是从配置树到数据树的一种转换
     */
    @SuppressWarnings("all")
    private static <T> Future<JsonObject> wrapTree(
        final JsonObject input, final boolean deeply, final Function<T, Future<JsonObject>> executor) {
        final ConcurrentMap<String, Future<JsonObject>> tree = new ConcurrentHashMap<>();
        final ConcurrentMap<String, Future<JsonObject>> children = new ConcurrentHashMap<>();
        for (final String field : input.fieldNames()) {
            // JsonArray 跳过不执行
            final Object value = input.getValue(field);
            if (value instanceof final JsonObject json) {
                // 遇到 JsonObject 节点，执行子提取，递归
                if (deeply) {
                    children.put(field, wrapTree(json, true, executor));
                }
            } else {
                T cast = null;
                try {
                    cast = (T) input.getValue(field);
                } catch (final Throwable ex) {
                }
                if (Objects.nonNull(cast)) {
                    tree.put(field, executor.apply(cast));
                }
            }
        }
        return War.combineM(tree).compose(treeMap -> {
            final JsonObject treeData = Ut.toJObject(treeMap);
            input.mergeIn(treeData, true);
            return Future.succeededFuture(input);
        }).compose(response -> War.combineM(children).compose(childMap -> {
            final JsonObject childData = Ut.toJObject(childMap);
            response.mergeIn(childData, true);
            return Future.succeededFuture(response);
        }));
    }
}
