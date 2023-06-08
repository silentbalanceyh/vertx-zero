package io.vertx.mod.crud.uca.trans;

import io.aeon.experiment.specification.KModule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.atom.extension.KTransform;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class MapTran implements Tran {

    private transient final boolean isFrom;

    MapTran(final boolean isFrom) {
        this.isFrom = isFrom;
    }

    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        if (in.canTransform()) {
            Ut.itJArray(data).forEach(each -> {
                this.mapping(each, in.module());
                if (in.canJoin()) {
                    this.mapping(each, in.connect());
                }
            });
        }
        return Ux.future(data);
    }

    private void mapping(final JsonObject data, final KModule module) {
        final KTransform transform = module.getTransform();
        if (Objects.isNull(transform)) {
            return;
        }
        // Mapping
        final JsonObject mapping = transform.getMapping();
        if (Ut.isNil(mapping)) {
            return;
        }
        Ut.<JsonObject>itJObject(mapping, (config, field) -> {
            // Append Only
            if (data.containsKey(field)) {
                final ConcurrentMap<String, String> map = this.mapping(config);
                final String value = data.getString(field);
                final String to = map.get(value);
                data.put(field, to);
            }
        });
    }

    private ConcurrentMap<String, String> mapping(final JsonObject config) {
        /*
         * {
         *     "IN": "中文",
         *     "OUT": "中文"
         * }
         * Left: to, Right: from
         */
        final ConcurrentMap<String, String> vector = new ConcurrentHashMap<>();
        Ut.<String>itJObject(config, (value, key) -> {
            // isFrom = true
            // import
            // value = key
            if (this.isFrom) {
                vector.put(value, key);
            } else {
                vector.put(key, value);
            }
        });
        return vector;
    }
}
