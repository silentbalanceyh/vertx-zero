package io.mature.extension.uca.graphic;

import io.mature.extension.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class NodeDeletePixel extends AbstractPixel {

    public NodeDeletePixel(final String identifier) {
        super(identifier);
    }

    @Override
    public Future<JsonObject> drawAsync(final JsonObject item) {
        return this.runSafe(Ox.toNode(item), this.client::nodeRemove);
    }

    @Override
    public Future<JsonArray> drawAsync(final JsonArray item) {
        return this.runSafe(Ox.toNode(item), this.client::nodeRemove);
    }
}
