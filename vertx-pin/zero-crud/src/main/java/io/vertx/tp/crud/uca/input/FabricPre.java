package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.optic.Pocket;
import io.vertx.tp.optic.component.Dictionary;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.exchange.DictConfig;
import io.vertx.up.commune.exchange.DictEpsilon;
import io.vertx.up.commune.exchange.DictFabric;
import io.vertx.up.commune.exchange.DictSource;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FabricPre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxIn in) {
        final Future<DictFabric> future = this.fabric(in);
        if (Objects.isNull(future)) {
            return Ux.future(data);
        }
        return future.compose(fabric -> fabric.inFrom(data));
    }

    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxIn in) {
        final Future<DictFabric> future = this.fabric(in);
        if (Objects.isNull(future)) {
            return Ux.future(data);
        }
        return future.compose(fabric -> fabric.inFrom(data));
    }

    private Future<DictFabric> fabric(final IxIn in) {
        final Envelop envelop = in.envelop();
        final KModule module = in.module();
        /* Epsilon */
        final ConcurrentMap<String, DictEpsilon> epsilonMap = module.epsilon();
        /* Channel Plugin, Here will enable Pool */
        final Dictionary plugin = Pocket.lookup(Dictionary.class);
        /* Dict */
        final DictConfig dict = module.source();
        if (epsilonMap.isEmpty() || Objects.isNull(plugin) || !dict.validSource()) {
            /*
             * Direct returned
             */
            Ix.Log.rest(this.getClass(), "Plugin condition failure, {0}, {1}, {2}",
                epsilonMap.isEmpty(), Objects.isNull(plugin), !dict.validSource());
            return null;
        } else {
            final List<DictSource> sources = dict.getSource();
            final MultiMap paramMap = MultiMap.caseInsensitiveMultiMap();
            final JsonObject headers = envelop.headersX();
            paramMap.add(KName.SIGMA, headers.getString(KName.SIGMA));
            /*
             * To avoid final in lambda expression
             */
            return plugin.fetchAsync(paramMap, sources).compose(dictData ->
                Ux.future(DictFabric.create()
                    .dictionary(dictData)
                    .epsilon(module.epsilon())
                ));
        }
    }
}
