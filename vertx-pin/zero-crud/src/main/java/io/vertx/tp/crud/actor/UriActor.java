package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.up.eon.KName;

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
        data.put(KName.URI, metadata.getString(KName.URI_REQUEST));
        data.put(KName.METHOD, metadata.getString(KName.METHOD));
        return data;
    }
}
