package cn.vertxup.ambient.service.file;

import cn.vertxup.ambient.domain.tables.daos.XCategoryDao;
import io.horizon.spi.feature.Arbor;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.atom.AtConfig;
import io.vertx.mod.ambient.init.AtPin;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.vertx.mod.ambient.refine.At.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DocBuilder implements DocBStub {
    private static final Cc<String, Arbor> CC_ARBOR = Cc.openThread();
    private static final Annal LOGGER = Annal.get(DocReader.class);

    // ------------------------- Document Management Tree -------------------------
    /*
     * Here the parameters are mapped to
     * -- appId: X_APP -> KEY
     * -- type:  X_CATEGORY -> TYPE
     */
    @Override
    public Future<JsonArray> initialize(final String appId, final String type) {
        final JsonObject condition = this.qrCond(appId, type, null);
        return Ux.Jooq.on(XCategoryDao.class).fetchJAsync(condition)
            .compose(Fn.ofJArray(
                KName.METADATA,
                KName.Component.TREE_CONFIG,
                KName.Component.RUN_CONFIG
            ))
            .compose(categories -> {
                final List<Future<JsonArray>> futures = new ArrayList<>();
                Ut.itJArray(categories).map(this::seekAsync).forEach(futures::add);
                return Fn.compressA(futures);
            });
    }

    @Override
    public Future<JsonArray> initialize(final String appId, final String type, final String name) {
        final JsonObject condition = this.qrCond(appId, type, name);
        return Ux.Jooq.on(XCategoryDao.class).fetchJOneAsync(condition)
            .compose(Fn.ofJObject(
                KName.METADATA,
                KName.Component.TREE_CONFIG,
                KName.Component.RUN_CONFIG
            ))
            .compose(this::seekAsync);
    }

    private JsonObject qrCond(final String appId, final String type, final String name) {
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.APP_ID, appId);
        condition.put(KName.TYPE, type);
        condition.put(KName.ACTIVE, Boolean.TRUE);
        if (Ut.isNotNil(name)) {
            condition.put(KName.NAME, name);
        }
        return condition;
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

        final Arbor arbor = CC_ARBOR.pick(() -> Ut.instance(arborCls), arborCls.getName());
        // Fn.po?lThread(POOL_ARBOR, () -> Ut.instance(arborCls), arborCls.getName());
        LOG.File.info(LOGGER, "Arbor = {0}, Configuration = {1}", arborCls.getName(), configuration.encode());
        return arbor.generate(input, configuration);
    }
}
