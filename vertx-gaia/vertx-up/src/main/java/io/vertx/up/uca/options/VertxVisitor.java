package io.vertx.up.uca.options;

import io.vertx.core.ClusterOptions;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Ruler;
import io.vertx.up.eon.Info;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.marshal.ClusterStrada;
import io.vertx.up.uca.marshal.Transformer;
import io.vertx.up.uca.marshal.VertxStrada;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.uca.yaml.ZeroVertx;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VertxVisitor implements NodeVisitor {
    private static final Annal LOGGER = Annal.get(VertxVisitor.class);

    private static final String KEY = "vertx";

    private transient final Node<JsonObject> NODE
        = Ut.singleton(ZeroVertx.class);
    private transient final Transformer<VertxOptions>
        transformer = Ut.singleton(VertxStrada.class);
    private transient final Transformer<ClusterOptions>
        clusterTransformer = Ut.singleton(ClusterStrada.class);

    private transient ClusterOptions clusterOptions;

    @Override
    public ConcurrentMap<String, VertxOptions> visit(final String... keys)
        throws ZeroException {
        // 1. Must be the first line, fixed position.
        Fn.inLenEq(this.getClass(), 0, keys);
        // 2. Visit the node for vertx
        final JsonObject data = this.NODE.read();
        // 3. Vertx node validation.
        final JsonObject vertxData = data.getJsonObject(KEY);
        LOGGER.info(Info.INF_B_VERIFY, KEY, this.getClass().getSimpleName(), vertxData);
        Fn.onZero(() -> Ruler.verify(KEY, vertxData), vertxData);
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
        throws ZeroException {
        final ConcurrentMap<String, VertxOptions> map =
            new ConcurrentHashMap<>();
        final boolean clustered = this.clusterOptions.isEnabled();
        Fn.etJArray(vertxData, JsonObject.class, (item, index) -> {
            // 1. Extract single
            final String name = item.getString(YKEY_NAME);
            // 2. Extract VertxOptions
            final VertxOptions options = this.transformer.transform(item);
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
