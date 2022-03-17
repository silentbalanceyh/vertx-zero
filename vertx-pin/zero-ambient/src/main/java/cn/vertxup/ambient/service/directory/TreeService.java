package cn.vertxup.ambient.service.directory;

import cn.vertxup.ambient.service.DatumStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.atom.AtConfig;
import io.vertx.tp.ambient.init.AtPin;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.optic.feature.Arbor;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class TreeService implements TreeStub {
    private static final ConcurrentMap<String, Arbor> POOL_ARBOR = new ConcurrentHashMap<>();
    private static final Annal LOGGER = Annal.get(TreeService.class);
    @Inject
    private transient DatumStub stub;

    /*
     * Here the parameters are mapped to
     * -- appId: X_APP -> KEY
     * -- type:  X_CATEGORY -> TYPE
     */
    @Override
    public Future<JsonArray> seekAsync(final String appId, final String type) {
        return this.stub.treeApp(appId, type).compose(categories -> {
            final List<Future<JsonArray>> futures = new ArrayList<>();
            Ut.itJArray(categories).map(this::seekAsync).forEach(futures::add);
            return Ux.thenCombineArray(futures);
        });
    }

    /*
     * Each category node should contain two dim operations:
     * -- treeComponent/treeConfig
     * -- runComponent/runConfig
     *
     * Terminal when comment in following situations
     * -- 1. treeComponent is null or it's not implement from Arbor interface.
     */
    private Future<JsonArray> seekAsync(final JsonObject input) {
        final String runComponent = input.getString(KName.Component.TREE_COMPONENT);
        final Class<?> arborCls = Ut.clazz(runComponent, null);


        // Terminal 1:
        if (Objects.isNull(arborCls) || !Ut.isImplement(arborCls, Arbor.class)) {
            return Ux.futureA();
        }


        final JsonObject configuration = input.getJsonObject(KName.Component.TREE_CONFIG);
        JsonObject storeRef = configuration.getJsonObject(KName.STORE);
        if (Ut.isNil(storeRef)) {
            storeRef = new JsonObject();
        }
        /*
         * The configuration data came from `treeConfig`, combine the configuration information attached into
         * {
         *      "store": {
         *          "storePath": "",
         *          "runComponent": "Default `Fs` interface component that will be stored into I_DIRECTORY",
         *          "initialize": {
         *              "field1": "value1",
         *              "field2": "value2",
         *              "...": "..."
         *          }
         *      }
         * }
         */
        final AtConfig config = AtPin.getConfig();
        storeRef.put(KName.STORE_PATH, config.getStorePath());
        configuration.put(KName.STORE, storeRef);

        final Arbor arbor = Fn.poolThread(POOL_ARBOR, () -> Ut.instance(arborCls), arborCls.getName());
        At.infoFile(LOGGER, "Arbor = {0}, Configuration = {1}", arborCls.getName(), configuration.encode());
        return arbor.generate(input, configuration);
    }
}
