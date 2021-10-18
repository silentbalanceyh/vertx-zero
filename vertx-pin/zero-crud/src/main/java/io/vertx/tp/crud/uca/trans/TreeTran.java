package io.vertx.tp.crud.uca.trans;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.tp.ke.atom.specification.KTransform;
import io.vertx.tp.ke.atom.specification.KTree;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class TreeTran implements Tran {
    private transient final boolean isFrom;

    TreeTran(final boolean isFrom) {
        this.isFrom = isFrom;
    }

    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        if (in.canTransform()) {
            return this.tree(in, this.isFrom).apply(data)
                .compose(map -> this.treeAsync(data, in, map));
        } else {
            return Ux.future(data);
        }
    }

    private Function<JsonArray, Future<ConcurrentMap<String, String>>> tree(final IxMod in, final boolean from) {
        return (data) -> {
            final KModule module = in.module();
            final KTransform transform = module.getTransform();
            final KTree tree = transform.getTree();
            if (Objects.isNull(tree)) {
                return Ux.future(new ConcurrentHashMap<>());
            }
            /* Pick Parent Database Value
             * out = key
             * in = code
             * */
            if (from) {
                /*
                 * From is importing, code will be the condition
                 * code = key
                 */
                final JsonArray values = Ut.toJArray(Ut.mapString(data, tree.getField()));
                final String condition = tree.getIn();
                return this.treeAsync(in, values, condition, tree.getOut()).compose(map -> {
                    /*
                     * Because key could not be modified, it means that the
                     * code = key should keep `map` priority
                     */
                    final ConcurrentMap<String, String> mapInput = this.tree(data, condition, tree.getOut());
                    mapInput.forEach((field, key) -> {
                        if (!map.containsKey(field)) {
                            map.put(field, key);
                        }
                    });
                    return Ux.future(map);
                });
            } else {
                // key = code
                final JsonArray values = Ut.toJArray(Ut.mapArray(data, tree.getField()));
                final String condition = tree.getOut();
                return this.treeAsync(in, values, condition, tree.getIn()).compose(map -> {
                    /*
                     * Because key could not be modified, it means that the
                     * key = code should keep `map` priority
                     */
                    final ConcurrentMap<String, String> mapInput = this.tree(data, condition, tree.getIn());
                    mapInput.forEach((field, key) -> {
                        if (!map.containsKey(field)) {
                            map.put(field, key);
                        }
                    });
                    return Ux.future(map);
                });
            }
        };
    }

    private Future<ConcurrentMap<String, String>> treeAsync(final IxMod in, final JsonArray values,
                                                            final String keyField, final String valueField) {
        final JsonObject criteria = new JsonObject();
        criteria.put(keyField + ",i", values);
        final UxJooq jooq = IxPin.jooq(in);
        return jooq.fetchJAsync(criteria).compose(source -> Ux.future(this.tree(source, keyField, valueField)));
    }

    private ConcurrentMap<String, String> tree(final JsonArray source,
                                               final String keyField, final String valueField) {
        final ConcurrentMap<String, String> data = new ConcurrentHashMap<>();
        Ut.itJArray(source).forEach(record -> {
            final String fromValue = record.getString(keyField);
            final String toValue = record.getString(valueField);
            if (Objects.nonNull(fromValue) && Objects.nonNull(toValue)) {
                data.put(fromValue, toValue);
            }
        });
        return data;
    }

    private Future<JsonArray> treeAsync(final JsonArray data, final IxMod in, final ConcurrentMap<String, String> map) {
        if (!map.isEmpty()) {
            final KModule module = in.module();
            final KTree tree = module.getTransform().getTree();
            final String field = tree.getField();
            Ut.itJArray(data).forEach(record -> {
                if (record.containsKey(field)) {
                    final String value = record.getString(field);
                    final String to = map.get(value);
                    record.put(field, to);
                }
            });
            return Ux.future(data);
        } else {
            return Ux.future(data);
        }
    }
}
