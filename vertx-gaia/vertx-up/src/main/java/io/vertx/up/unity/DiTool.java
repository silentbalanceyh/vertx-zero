package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.component.Dictionary;
import io.vertx.up.commune.exchange.DiFabric;
import io.vertx.up.commune.exchange.DiSetting;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.adminicle.FieldMapper;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * DictSource
 * Dict
 * DictEpsilon
 */
@SuppressWarnings("all")
class DiTool {

    private static final ConcurrentMap<Integer, Dictionary> POOL_DICT =
        new ConcurrentHashMap<>();

    static <T> Future<T> dictTo(final T record, final DiFabric fabric) {
        final FieldMapper mapper = new FieldMapper();
        if (record instanceof JsonObject) {
            final JsonObject ref = (JsonObject) record;
            return fabric.inTo(ref)
                .compose(processed -> Ux.future(mapper.out(processed, fabric.mapping())))
                .compose(processed -> Ux.future((T) processed));
        } else if (record instanceof JsonArray) {
            final JsonArray ref = (JsonArray) record;
            return fabric.inTo(ref)
                .compose(processed -> Ux.future(mapper.out(processed, fabric.mapping())))
                .compose(processed -> Ux.future((T) processed));
        } else {
            return Ux.future(record);
        }
    }

    static Future<ConcurrentMap<String, JsonArray>> dictCalc(final DiSetting dict, final MultiMap paramMap) {
        if (Objects.isNull(dict)) {
            /*
             * Not `Dict` configured
             */
            return To.future(new ConcurrentHashMap<>());
        } else {
            /*
             * Dict extract here
             */
            final ConcurrentMap<String, JsonArray> dictData = new ConcurrentHashMap<>();
            if (dict.valid()) {
                /*
                 * Component Extracted
                 */
                final Class<?> dictCls = dict.getComponent();
                if (Ut.isImplement(dictCls, Dictionary.class)) {
                    /*
                     * JtDict instance for fetchAsync
                     */
                    final Dictionary dictStub = Fn.pool(POOL_DICT, dict.hashCode(),
                        () -> Ut.instance(dictCls));
                    /*
                     * Param Map / List<Source>
                     */
                    return dictStub.fetchAsync(paramMap, dict.getSource());
                } else return To.future(dictData);
            }
            return To.future(dictData);
        }
    }
}
