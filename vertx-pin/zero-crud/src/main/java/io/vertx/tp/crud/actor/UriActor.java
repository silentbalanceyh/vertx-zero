package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.tp.ke.cv.KeField;

/*
 * Uri information
 */
class UriActor extends AbstractActor {
    @Override
    public JsonObject proc(final JsonObject data, final IxModule module) {
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
