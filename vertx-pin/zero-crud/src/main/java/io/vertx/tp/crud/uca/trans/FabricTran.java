package io.vertx.tp.crud.uca.trans;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.optic.Pocket;
import io.vertx.tp.optic.component.Dictionary;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.exchange.DiConsumer;
import io.vertx.up.commune.exchange.DiFabric;
import io.vertx.up.commune.exchange.DiSetting;
import io.vertx.up.commune.exchange.DiSource;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.specification.KModule;
import io.vertx.up.experiment.specification.KTransform;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class FabricTran implements Tran {
    private transient final boolean isFrom;

    FabricTran(final boolean isFrom) {
        this.isFrom = isFrom;
    }

    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        if (in.canTransform()) {
            return this.fabric(in).compose(Ut.ifNil(() -> data, fabric ->
                this.isFrom ? fabric.inFrom(data) : fabric.inTo(data)));
        } else {
            return Ux.future(data);
        }
    }

    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        if (in.canTransform()) {
            return this.fabric(in).compose(Ut.ifNil(() -> data, fabric ->
                this.isFrom ? fabric.inFrom(data) : fabric.inTo(data)));
        } else {
            return Ux.future(data);
        }
    }

    private Future<DiFabric> fabric(final IxMod in) {
        final Envelop envelop = in.envelop();
        final KModule module = in.module();
        final KTransform transform = module.getTransform();
        return this.fabric(module, envelop).compose(dictData -> {
            final ConcurrentMap<String, JsonArray> dictMap = new ConcurrentHashMap<>(dictData);
            if (in.canJoin()) {
                /*
                 * Nested dictionary
                 */
                final KModule connect = in.connect();
                final KTransform transformConnect = connect.getTransform();
                /*
                 * Combine DiConsumer
                 */
                final ConcurrentMap<String, DiConsumer> connectConsumer = transform.epsilon();
                if (Objects.nonNull(transformConnect)) {
                    connectConsumer.putAll(transformConnect.epsilon());
                }
                return this.fabric(connect, envelop).compose(dictConnect -> {
                    dictMap.putAll(dictConnect);
                    return Ux.future(DiFabric.create()
                        .dictionary(dictMap)
                        .epsilon(connectConsumer)
                    );
                });
            } else {
                // No Connect Module
                return Ux.future(DiFabric.create()
                    .dictionary(dictMap)
                    .epsilon(transform.epsilon())
                );
            }
        });
    }

    private Future<ConcurrentMap<String, JsonArray>> fabric(final KModule module, final Envelop envelop) {
        final KTransform transform = module.getTransform();
        if (Objects.isNull(transform)) {
            return Ux.future(new ConcurrentHashMap<>());
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
            Ix.Log.rest(this.getClass(), "Plugin condition failure, {0}, {1}, {2}",
                epsilonMap.isEmpty(), Objects.isNull(plugin), !dict.validSource());
            return Ux.future(new ConcurrentHashMap<>());
        }
        // Calculation
        final List<DiSource> sources = dict.getSource();
        final MultiMap paramMap = MultiMap.caseInsensitiveMultiMap();
        final JsonObject headers = envelop.headersX();
        paramMap.add(KName.SIGMA, headers.getString(KName.SIGMA));
        /*
         * To avoid final in lambda expression
         */
        return plugin.fetchAsync(paramMap, sources);
    }
}
