package io.vertx.tp.crud.refine;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.tp.ke.atom.specification.KTransform;
import io.vertx.tp.ke.atom.specification.KTree;
import io.vertx.tp.optic.Pocket;
import io.vertx.tp.optic.component.Dictionary;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.exchange.DiConsumer;
import io.vertx.up.commune.exchange.DiFabric;
import io.vertx.up.commune.exchange.DiSetting;
import io.vertx.up.commune.exchange.DiSource;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IxTransform {

    static Function<JsonArray, Future<ConcurrentMap<String, String>>> tree(final IxMod in, final boolean from) {
        return (data) -> {
            final KModule module = in.module();
            final KTransform transform = module.getTransform();
            if (Objects.isNull(transform)) {
                return Ux.future(new ConcurrentHashMap<>());
            }
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
                return treeAsync(in, values, condition, tree.getOut()).compose(map -> {
                    /*
                     * Because key could not be modified, it means that the
                     * code = key should keep `map` priority
                     */
                    final ConcurrentMap<String, String> mapInput = tree(data, condition, tree.getOut());
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
                return treeAsync(in, values, condition, tree.getIn()).compose(map -> {
                    /*
                     * Because key could not be modified, it means that the
                     * key = code should keep `map` priority
                     */
                    final ConcurrentMap<String, String> mapInput = tree(data, condition, tree.getIn());
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

    private static Future<ConcurrentMap<String, String>> treeAsync(final IxMod in, final JsonArray values,
                                                                   final String keyField, final String valueField) {
        final JsonObject criteria = new JsonObject();
        criteria.put(keyField + ",i", values);
        final UxJooq jooq = IxPin.jooq(in);
        return jooq.fetchJAsync(criteria).compose(source -> Ux.future(tree(source, keyField, valueField)));
    }

    private static ConcurrentMap<String, String> tree(final JsonArray source,
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

    static Future<DiFabric> fabric(final IxMod in) {
        final Envelop envelop = in.envelop();
        final KModule module = in.module();
        final KTransform transform = module.getTransform();
        if (Objects.isNull(transform)) {
            return Ux.future();
        }
        /* Epsilon */
        final ConcurrentMap<String, DiConsumer> epsilonMap = transform.epsilon();
        /* Channel Plugin, Here will enable Pool */
        final Dictionary plugin = Pocket.lookup(Dictionary.class);
        /* Dict */
        final DiSetting dict = transform.source();
        if (epsilonMap.isEmpty() || Objects.isNull(plugin) || !dict.validSource()) {
            /*
             * Direct returned
             */
            Ix.Log.rest(IxData.class, "Plugin condition failure, {0}, {1}, {2}",
                epsilonMap.isEmpty(), Objects.isNull(plugin), !dict.validSource());
            return Ux.future();
        }
        // Calculation
        final List<DiSource> sources = dict.getSource();
        final MultiMap paramMap = MultiMap.caseInsensitiveMultiMap();
        final JsonObject headers = envelop.headersX();
        paramMap.add(KName.SIGMA, headers.getString(KName.SIGMA));
        /*
         * To avoid final in lambda expression
         */
        return plugin.fetchAsync(paramMap, sources).compose(dictData ->
            Ux.future(DiFabric.create()
                .dictionary(dictData)
                .epsilon(transform.epsilon())
            ));
    }
}
