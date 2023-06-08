package io.mature.extension.uca.graphic;

import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/*
 * 像素级，处理
 * 1. Node
 * 2. Edge
 */
public interface Pixel {
    /*
     * Neo4j 关系服务
     */
    static Pixel edge(final ChangeFlag type, final String identifier) {
        final Function<String, Pixel> executor = Pool.POOL_EDGE_SUPPLIER.get(type);
        final ConcurrentMap<String, Pixel> pool = Pool.POOL_EDGE.get(type);
        if (Objects.isNull(pool)) {
            return executor.apply(identifier);
        } else {
            return Cc.pool(pool, identifier, () -> executor.apply(identifier));
        }
    }

    /*
     * Neo4j 节点服务
     */
    static Pixel node(final ChangeFlag type, final String identifier) {
        final Function<String, Pixel> executor = Pool.POOL_NODE_SUPPLIER.get(type);
        final ConcurrentMap<String, Pixel> pool = Pool.POOL_NODE.get(type);
        if (Objects.isNull(pool)) {
            return executor.apply(identifier);
        } else {
            return Cc.pool(pool, identifier, () -> executor.apply(identifier));
        }
    }

    /**
     * Node / Edge 节点单独添加
     */
    Future<JsonObject> drawAsync(JsonObject item);

    /*
     *　Node / Edge 节点批量添加
     */
    Future<JsonArray> drawAsync(JsonArray item);
}
