package io.vertx.up.uca.adminicle;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.DualItem;
import io.vertx.up.util.Ut;

/*
 * Dual Processing for
 * ActIn / ActOut
 */
public interface Mapper {
    /*
     * Mapping
     * to -> from
     */
    JsonObject in(JsonObject in, DualItem mapping);

    default JsonArray in(final JsonArray in, final DualItem mapping) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(in).map(each -> this.in(each, mapping)).forEach(normalized::add);
        return normalized;
    }

    /*
     * Mapping
     * from -> to
     */
    JsonObject out(JsonObject out, DualItem mapping);

    default JsonArray out(final JsonArray out, final DualItem mapping) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(out).map(each -> this.out(each, mapping)).forEach(normalized::add);
        return normalized;
    }
}
