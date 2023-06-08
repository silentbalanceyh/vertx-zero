package io.mature.extension.uca.elasticsearch;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;


/**
 * @author Hongwei
 * @since 2020/1/8, 21:13
 */
public class EsUpdateIndexer extends AbstractEsIndex {


    public EsUpdateIndexer(final String name) {
        super(name);
    }

    @Override
    public Future<JsonObject> indexAsync(final JsonObject response) {
        return this.runSingle(response, () ->
            this.client.updateDocument(this.identifier, response.getString(KName.KEY), response));
    }

    @Override
    public Future<JsonArray> indexAsync(final JsonArray response) {
        return this.runBatch(response, () ->
            this.client.updateDocuments(this.identifier, response));
    }
}
