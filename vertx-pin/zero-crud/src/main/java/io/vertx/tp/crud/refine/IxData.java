package io.vertx.tp.crud.refine;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.atom.specification.KField;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.tp.ke.atom.specification.KTransform;
import io.vertx.tp.optic.Pocket;
import io.vertx.tp.optic.component.Dictionary;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.exchange.DiConsumer;
import io.vertx.up.commune.exchange.DiFabric;
import io.vertx.up.commune.exchange.DiSetting;
import io.vertx.up.commune.exchange.DiSource;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IxData {
    private static final Annal LOGGER = Annal.get(IxData.class);

    static void audit(final JsonObject auditor, final JsonObject config, final String userId) {
        if (Objects.nonNull(config) && Ut.notNil(userId)) {
            /* User By */
            final String by = config.getString(KName.BY);
            if (Ut.notNil(by)) {
                /* Audit Process */
                IxLog.infoDao(LOGGER, "( Audit ) By -> \"{0}\" = {1}", by, userId);
                auditor.put(by, userId);
            }
            final String at = config.getString(KName.AT);
            if (Ut.notNil(at)) {
                IxLog.infoDao(LOGGER, "( Audit ) At Field -> {0}", at);
                auditor.put(at, Instant.now());
            }
        }
    }

    static Kv<String, HttpMethod> flush(final IxMod in) {
        final KModule module = in.module();
        final String pattern = "/api/{0}/search";
        final String actor = module.getName();
        return Kv.create(MessageFormat.format(pattern, actor), HttpMethod.POST);
    }

    static Kv<String, String> field(final Object value) {
        if (Constants.DEFAULT_HOLDER.equals(value)) {
            return null;
        }
        final String field;
        final String fieldValue;
        if (value instanceof String) {
            // metadata
            field = value.toString().split(",")[0];
            fieldValue = value.toString().split(",")[1];
        } else {
            final JsonObject column = (JsonObject) value;
            if (column.containsKey(KName.METADATA)) {
                // metadata
                final String metadata = column.getString(KName.METADATA);
                if (Ut.notNil(metadata)) {
                    field = metadata.split(",")[0];
                    fieldValue = value.toString().split(",")[1];
                } else {
                    field = null;
                    fieldValue = null;
                }
            } else {
                // dataIndex
                field = column.getString(IxPin.getColumnKey());
                fieldValue = column.getString(IxPin.getColumnLabel());
            }
        }
        if (Objects.nonNull(field) && Objects.nonNull(fieldValue)) {
            return Kv.create(field, fieldValue);
        } else {
            return null;
        }
    }

    static JsonArray matrix(final KField field) {
        final JsonArray priority = new JsonArray();
        final String keyField = field.getKey();
        /*
         * Add key into group as the high est priority
         */
        priority.add(new JsonArray().add(keyField));
        final JsonArray matrix = Ut.sureJArray(field.getUnique());
        priority.addAll(matrix);
        return priority;
    }


    // JqFn
    @SafeVarargs
    static <T> Future<T> passion(final T input, final IxMod in, final BiFunction<T, IxMod, Future<T>>... executors) {
        // Sequence for future management
        Future<T> future = Future.succeededFuture(input);
        for (final BiFunction<T, IxMod, Future<T>> executor : executors) {
            if (Objects.nonNull(executor)) {
                future = future.compose(data -> executor.apply(data, in));
            }
        }
        return future;
    }

    static Future<DiFabric> fabric(final IxMod in) {
        final Envelop envelop = in.envelop();
        final KModule module = in.module();
        final KTransform transform = module.getTransform();
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
            return null;
        } else {
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
}
