package io.mature.extension.uca.graphic;

import io.mature.extension.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class EdgeUpdatePixel extends AbstractPixel {

    public EdgeUpdatePixel(final String identifier) {
        super(identifier);
    }

    @Override
    public Future<JsonObject> drawAsync(final JsonObject item) {
        return this.runSafe(Ox.toEdge(item), this.client::edgeUpdate);
    }

    @Override
    public Future<JsonArray> drawAsync(final JsonArray item) {
        return this.runSafe(Ox.toEdge(item), this.client::edgeUpdate);
    }
}
