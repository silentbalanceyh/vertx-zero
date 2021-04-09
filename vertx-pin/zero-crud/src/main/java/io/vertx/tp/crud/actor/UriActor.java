package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.metadata.KModule;
import io.vertx.tp.ke.cv.KeField;

/*
 * Uri information
 */
class UriActor extends AbstractActor {
    @Override
    public JsonObject proc(final JsonObject data, final KModule module) {
        final JsonObject metadata = this.getMetadata();
        /*
         * Replace uri and method to refresh parameters.
         * Here you must be replaced with:
         *    requestUri --> uri
         * Here the result is different
         */
        data.put(KeField.URI, metadata.getString(KeField.URI_REQUEST));
        data.put(KeField.METHOD, metadata.getString(KeField.METHOD));
        return data;
    }
}
