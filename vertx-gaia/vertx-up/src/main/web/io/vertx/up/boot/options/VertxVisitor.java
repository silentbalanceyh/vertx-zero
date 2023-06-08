package io.vertx.up.boot.options;

import io.horizon.eon.VMessage;
import io.horizon.exception.ProgramException;
import io.horizon.uca.log.Annal;
import io.vertx.core.ClusterOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Ruler;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.uca.options.JTransformer;
import io.vertx.up.uca.options.NodeVisitor;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VertxVisitor implements NodeVisitor {
    private static final Annal LOGGER = Annal.get(VertxVisitor.class);

    private static final String KEY = "vertx";
    private transient final JTransformer<VertxOptions>
        transformer = Ut.singleton(VertxSetUp.class);
    private transient final JTransformer<ClusterOptions>
        clusterTransformer = Ut.singleton(ClusterSetUp.class);

    private transient ClusterOptions clusterOptions;

    @Override
    public ConcurrentMap<String, VertxOptions> visit(final String... keys)
        throws ProgramException {
        // 1. Must be the first line, fixed position.
        //        Fn.verifyLenEq(this.getClass(), 0, (Object[]) keys);
        // 2. Visit the node for vertx
        final JsonObject data = ZeroStore.containerJ();
        // 3. Vertx node validation.
        final JsonObject vertxData = data.getJsonObject(KEY);
        LOGGER.info(VMessage.Visitor.V_BEFORE, KEY, this.getClass().getSimpleName(), vertxData);
        Fn.bugAt(() -> Ruler.verify(KEY, vertxData), vertxData);
        // 4. Set cluster options
        this.clusterOptions = this.clusterTransformer.transform(vertxData.getJsonObject(YKEY_CLUSTERED));
        // 5. Transfer Data
        return this.visit(vertxData.getJsonArray(YKEY_INSTANCE));
    }

    @Override
    public ClusterOptions getCluster() {
        return this.clusterOptions;
    }

    private ConcurrentMap<String, VertxOptions> visit(
        final JsonArray vertxData)
        throws ProgramException {
        final ConcurrentMap<String, VertxOptions> map =
            new ConcurrentHashMap<>();
        final boolean clustered = this.clusterOptions.isEnabled();
        Fn.bugIt(vertxData, JsonObject.class, (item, index) -> {
            // 1. Extract single
            final String name = item.getString(YKEY_NAME);
            // 2. Extract VertxOptions
            final VertxOptions options = this.transformer.transform(item);
            if (clustered) {
                options.setClusterManager(this.clusterOptions.getManager());
            }
            // 3. Check the configuration for cluster sync
/*            Fn.outZero(clustered != options.isClustered(), LOGGER,
                ClusterConflictException.class,
                this.getClass(), name, options.toString());*/
            // 4. Put the options into map
            map.put(name, options);
        });
        return map;
    }
}
