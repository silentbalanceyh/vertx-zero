package io.vertx.up.fn;

import io.aeon.runtime.CRunning;
import io.horizon.specification.typed.TCombiner;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

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
class End {

    static JsonObject endJObject(final String key, final JsonArray data, final JsonObject config) {
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
     * TCombiner Process On Web UI
     */
    @SuppressWarnings("unchecked")
    static Future<JsonObject> endWebUi(final JsonObject json, final String field) {
        if (Ut.isNil(json) || !json.containsKey(field) || Ut.isNil(field)) {
            return Future.succeededFuture(json);
        }
        // Class<?>
        final Class<?> clazz = Ut.valueC(json, field, null);
        if (Objects.isNull(clazz)) {
            return Future.succeededFuture(json);
        }
        final TCombiner<JsonObject> combiner = (TCombiner<JsonObject>) CRunning.CC_COMBINER.pick(() -> Ut.instance(clazz), clazz.getName());
        return combiner.executeAsync(json);
    }

    /*
     * mount
     * {
     *     "a": "T",
     *     "e": "T"
     * }
     * ---> 所有的 T 类型的节点都会被处理掉并实现替换
     */
    static <T> Function<JsonObject, Future<JsonObject>> endTree(
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
                return endTree(tree, deeply, executor).compose(segmentData -> {
                    if (Ut.isNotNil(segmentData)) {
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
    private static <T> Future<JsonObject> endTree(
        final JsonObject input, final boolean deeply, final Function<T, Future<JsonObject>> executor) {
        final ConcurrentMap<String, Future<JsonObject>> tree = new ConcurrentHashMap<>();
        final ConcurrentMap<String, Future<JsonObject>> children = new ConcurrentHashMap<>();
        for (final String field : input.fieldNames()) {
            // JsonArray 跳过不执行
            final Object value = input.getValue(field);
            if (value instanceof final JsonObject json) {
                // 遇到 JsonObject 节点，执行子提取，递归
                if (deeply) {
                    children.put(field, endTree(json, true, executor));
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
        return ThenM.combineM(tree).compose(treeMap -> {
            final JsonObject treeData = Ut.toJObject(treeMap);
            input.mergeIn(treeData, true);
            return Future.succeededFuture(input);
        }).compose(response -> ThenM.combineM(children).compose(childMap -> {
            final JsonObject childData = Ut.toJObject(childMap);
            response.mergeIn(childData, true);
            return Future.succeededFuture(response);
        }));
    }
}
