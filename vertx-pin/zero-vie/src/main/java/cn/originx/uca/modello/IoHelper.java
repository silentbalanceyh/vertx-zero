package cn.originx.uca.modello;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.modular.plugin.OExpression;
import io.vertx.up.commune.Record;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.DataFormat;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IoHelper {
    private static final ConcurrentMap<String, BiFunction<Record, JsonObject, JsonObject>> RULES =
        new ConcurrentHashMap<String, BiFunction<Record, JsonObject, JsonObject>>() {
            {
                this.put("PREFIX", FnRule::doPrefix);
            }
        };

    /**
     * @param combineData {@link JsonObject}
     *
     * @return {@link ConcurrentMap}
     */
    static ConcurrentMap<String, OExpression> afterExpression(final JsonObject combineData) {
        final JsonObject sourceNorm = Ut.sureJObject(combineData.getJsonObject(KName.SOURCE_EXPR));
        final ConcurrentMap<String, OExpression> exprMap = new ConcurrentHashMap<>();
        Ut.<String>itJObject(sourceNorm, (className, field) -> {
            final Class<?> expression = Ut.clazz(className, null);
            if (Objects.nonNull(expression)) {
                exprMap.put(field, Ut.singleton(expression));
            }
        });
        return exprMap;
    }

    /**
     * ```json
     * // <pre><code class="json">
     * {
     *     "sourceNorm": {
     *          "rule": "PREFIX",
     *          "result": "ONE"
     *     },
     *     "attribute": {
     *          "name": "xxx",
     *          "alias": "Text",
     *          "format": "JsonArray | JsonObject | Elementary"
     *     },
     *     "sourceData": {},
     *     "source": "来源模型identifier",
     *     "sourceField": "来源属性信息",
     *     "sourceParams": {
     *          "sigma": "xxx"
     *     }
     * }
     * // </code></pre>
     * ```
     *
     * @param combineData {@link JsonObject}
     *
     * @return {@link JsonObject}
     */
    static JsonObject configCompress(final JsonObject combineData) {
        final JsonObject config = new JsonObject();
        config.mergeIn(Ut.sureJObject(combineData.getJsonObject(KName.SOURCE_NORM)));
        config.mergeIn(Ut.sureJObject(combineData.getJsonObject(KName.ATTRIBUTE)));
        return config;
    }

    /**
     * ```json
     * // <pre><code class="json">
     * {
     *     "name": "当前属性名称",
     *     "format": "当前属性数据类型",
     *     "rule": "PREFIX,...",
     *     "result": "ONE"
     * }
     * // </code></pre>
     * ```
     *
     * @param ruleKey {@link String}
     *
     * @return {@link BiFunction}
     */
    static BiFunction<Record, JsonObject, JsonObject> compressFn(final String ruleKey) {
        if (Ut.isNil(ruleKey)) {
            return (r, j) -> null;
        } else {
            return RULES.getOrDefault(ruleKey, (r, j) -> null);
        }
    }

    /**
     * @param item   {@link JsonObject}
     * @param config {@link JsonObject}
     *
     * @return {@link Object}
     */
    static Object end(final JsonObject item, final JsonObject config) {
        final DataFormat format = Ut.toEnum(() -> config.getString(KName.FORMAT), DataFormat.class, DataFormat.Elementary);
        if (DataFormat.JsonObject == format) {
            return item;
        } else {
            final JsonArray array = new JsonArray();
            array.add(item);
            return array;
        }
    }

    private static class FnRule {

        static JsonObject doPrefix(final Record record, final JsonObject config) {
            final String prefix = config.getString(KName.NAME);
            final JsonObject data = new JsonObject();
            final Set<String> removedSet = new HashSet<>();
            record.declaredFields().stream()
                .filter(field -> field.contains("."))
                .filter(field -> field.startsWith(prefix))
                .filter(record::isValue)
                .forEach(field -> {
                    final String attrName = field.split("\\.")[1];
                    final Object value = record.get(field);
                    data.put(attrName, value);
                    removedSet.add(field);
                });
            removedSet.forEach(record::remove);
            return data;
        }
    }
}
