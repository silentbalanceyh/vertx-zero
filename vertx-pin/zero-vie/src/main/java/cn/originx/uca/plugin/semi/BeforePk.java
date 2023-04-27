package cn.originx.uca.plugin.semi;

import cn.originx.refine.Ox;
import cn.originx.scaffold.plugin.AbstractBefore;
import io.horizon.specification.modeler.HRecord;
import io.horizon.spi.plugin.BeforePlugin;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BeforePk extends AbstractBefore {
    private transient DataAtom atom;

    @Override
    public BeforePlugin bind(final DataAtom atom) {
        this.atom = atom;
        return this;
    }

    @Override
    public Future<JsonObject> beforeAsync(final JsonObject record, final JsonObject options) {
        final ConcurrentMap<String, JsonObject> config = this.configField(options);
        final ConcurrentMap<String, Future<HRecord>> futures = new ConcurrentHashMap<>();
        config.forEach((field, fieldConfig) -> {
            final Object viValue = record.getValue(fieldConfig.getString(field));
            if (Objects.nonNull(viValue)) {
                final String identifier = fieldConfig.getString(KName.IDENTIFIER);
                final String viField = fieldConfig.getString(KName.SOURCE);
                futures.put(field, Ox.viGet(this.atom, identifier, viField, viValue));
            }
        });
        return Fn.combineM(futures).compose(map -> {
            config.keySet().forEach(field -> {
                final HRecord ref = map.getOrDefault(field, null);
                final Object value = this.extractValue(ref, config.getOrDefault(field, new JsonObject()));
                if (Objects.nonNull(value)) {
                    record.put(field, value);
                }
            });
            return Ux.future(record);
        });
    }

    @Override
    public Future<JsonArray> beforeAsync(final JsonArray records, final JsonObject options) {
        final ConcurrentMap<String, JsonObject> config = this.configField(options);
        final ConcurrentMap<String, Future<ConcurrentMap<String, HRecord>>> futures = new ConcurrentHashMap<>();
        config.forEach((field, fieldConfig) -> {
            final Set<String> values = Ut.valueSetString(records, field);
            if (Objects.nonNull(values) && !values.isEmpty()) {
                final String identifier = fieldConfig.getString(KName.IDENTIFIER);
                final String viField = fieldConfig.getString(KName.SOURCE);
                futures.put(field, Ox.viGetMap(this.atom, identifier, viField, Ut.toJArray(values)));
            }
        });
        return Fn.combineM(futures).compose(map -> {
            // 单字段读取
            Ut.itJArray(records).forEach(record -> map.forEach((field, valueMap) -> {
                /*
                 * 1. field -> deployDb, record -> JsonObject
                 * -- 得到 deployDb 中存在的 recordRef
                 * 2. 从 recordRef 抽取值
                 */
                if (Ut.isNotNil(record.getString(field))) {
                    final HRecord ref = valueMap.get(record.getString(field));
                    final Object value = this.extractValue(ref, config.getOrDefault(field, new JsonObject()));
                    if (Objects.nonNull(value)) {
                        record.put(field, value);
                    } else {
                        record.remove(field);
                    }
                }
            }));
            return Ux.future(records);
        });
    }

    private Object extractValue(final HRecord ref, final JsonObject config) {
        if (Objects.nonNull(ref)) {
            final String targetField = config.getString("target");
            return ref.get(targetField);
        } else {
            return null;
        }
    }

    private ConcurrentMap<String, JsonObject> configField(final JsonObject options) {
        final ConcurrentMap<String, JsonObject> configMap = new ConcurrentHashMap<>();
        final JsonObject configuration = options.getJsonObject(KName.CONFIG, new JsonObject());
        configuration.fieldNames().forEach(field -> configMap.put(field, configuration.getJsonObject(field)));
        return configMap;
    }
}
