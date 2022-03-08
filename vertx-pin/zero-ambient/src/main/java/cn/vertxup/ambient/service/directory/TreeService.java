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

    @Override
    public Future<JsonArray> seekAsync(final String appId, final String type) {
        return this.stub.treeApp(appId, type).compose(categories -> {
            final List<Future<JsonObject>> futures = new ArrayList<>();
            Ut.itJArray(categories).map(this::seekAsync).forEach(futures::add);
            return Ux.thenCombine(futures);
        });
    }

    private Future<JsonObject> seekAsync(final JsonObject input) {
        final String runComponent = input.getString(KName.Component.TREE_COMPONENT);
        final Class<?> arborCls = Ut.clazz(runComponent, null);
        if (Objects.isNull(arborCls) || !Ut.isImplement(arborCls, Arbor.class)) {
            // Nothing To Do
            return Ux.future(input);
        }
        final JsonObject configuration = this.seekConfig(input);
        final Arbor arbor = Fn.poolThread(POOL_ARBOR, () -> Ut.instance(arborCls), arborCls.getName());
        At.infoFile(LOGGER, "Arbor = {0}, Configuration = {1}", arborCls.getName(), configuration.encode());
        return arbor.generate(input, configuration).compose(children -> {
            /*
             * Configuration Extract Mapping
             * {
             *      "field1": "treeField1",
             *      "field2": "treeField2",
             *      "field3": "treeField3",
             *      "field4": "treeField4",
             * }
             */
            final JsonObject mapping = configuration.getJsonObject(KName.MAPPING, new JsonObject());
            mapping.fieldNames().forEach(from -> {
                final String to = mapping.getString(from);
                Ut.itJArray(children).forEach(child -> child.put(to, child.getValue(from)));
            });
            input.put(KName.CHILDREN, children);
            return Ux.future(input);
        });
    }

    private JsonObject seekConfig(final JsonObject input) {
        final JsonObject configuration = input.getJsonObject(KName.Component.TREE_CONFIG);
        JsonObject storeRef = configuration.getJsonObject(KName.STORE);
        if (Ut.isNil(storeRef)) {
            storeRef = new JsonObject();
        }
        /*
         * Combine the configuration information attached into
         * {
         *      "store": {
         *          "storeRoot": ""
         *          "storePath": ""
         *      }
         * }
         */
        final AtConfig config = AtPin.getConfig();
        storeRef.put(KName.STORE_ROOT, config.getStoreRoot());
        storeRef.put(KName.STORE_PATH, config.getStorePath());
        configuration.put(KName.STORE, storeRef);
        return configuration;
    }
}
