package io.mature.extension.uca.elasticsearch;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.stream.Collectors;


/**
 * @author Hongwei
 * @since 2020/1/8, 21:13
 */
public class EsDeleteIndexer extends AbstractEsIndex {
    public EsDeleteIndexer(final String name) {
        super(name);
    }

    @Override
    public Future<JsonObject> indexAsync(final JsonObject response) {
        return this.runSingle(response, () ->
            this.client.deleteDocument(this.identifier, response.getString(KName.KEY)));
    }

    @Override
    public Future<JsonArray> indexAsync(final JsonArray response) {
        return this.runBatch(response,
            () -> this.client.deleteDocuments(this.identifier, Ut.itJArray(response)
                .map(item -> item.getString(KName.KEY))
                .filter(Ut::isNotNil)
                .collect(Collectors.toSet())));
    }
}
