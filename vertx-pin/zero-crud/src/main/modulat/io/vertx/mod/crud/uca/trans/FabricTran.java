package io.vertx.mod.crud.uca.trans;

import io.aeon.experiment.specification.KModule;
import io.aeon.runtime.channel.Pocket;
import io.horizon.spi.component.Dictionary;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.atom.exchange.DConsumer;
import io.vertx.up.atom.exchange.DFabric;
import io.vertx.up.atom.exchange.DSetting;
import io.vertx.up.atom.exchange.DSource;
import io.vertx.up.atom.extension.KTransform;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.crud.refine.Ix.LOG;

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
            return this.fabric(in).compose(Fn.ifNil(() -> data, fabric ->
                this.isFrom ? fabric.inFrom(data) : fabric.inTo(data)));
        } else {
            return Ux.future(data);
        }
    }

    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        if (in.canTransform()) {
            return this.fabric(in).compose(Fn.ifNil(() -> data, fabric ->
                this.isFrom ? fabric.inFrom(data) : fabric.inTo(data)));
        } else {
            return Ux.future(data);
        }
    }

    private Future<DFabric> fabric(final IxMod in) {
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
                final ConcurrentMap<String, DConsumer> connectConsumer = transform.epsilon();
                if (Objects.nonNull(transformConnect)) {
                    connectConsumer.putAll(transformConnect.epsilon());
                }
                return this.fabric(connect, envelop).compose(dictConnect -> {
                    dictMap.putAll(dictConnect);
                    return Ux.future(DFabric.create()
                        .dictionary(dictMap)
                        .epsilon(connectConsumer)
                    );
                });
            } else {
                // No Connect Module
                return Ux.future(DFabric.create()
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
        final ConcurrentMap<String, DConsumer> epsilonMap = transform.epsilon();
        /* Channel Infusion, Here will enable Pool */
        final Dictionary plugin = Pocket.lookup(Dictionary.class);
        /* Dict */
        final DSetting dict = transform.source();
        if (epsilonMap.isEmpty() || Objects.isNull(plugin) || !dict.validSource()) {
            /*
             * Direct returned
             */
            LOG.Rest.info(this.getClass(), "Infusion condition handler, {0}, {1}, {2}",
                epsilonMap.isEmpty(), Objects.isNull(plugin), !dict.validSource());
            return Ux.future(new ConcurrentHashMap<>());
        }
        // Calculation
        final List<DSource> sources = dict.getSource();
        final MultiMap paramMap = MultiMap.caseInsensitiveMultiMap();
        final JsonObject headers = envelop.headersX();
        paramMap.add(KName.SIGMA, headers.getString(KName.SIGMA));
        /*
         * To avoid final in lambda expression
         */
        return plugin.fetchAsync(paramMap, sources);
    }
}
