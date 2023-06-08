package io.vertx.up.plugin.elasticsearch;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EsAmbitUpdate extends AbstractEsClient implements EsAmbit {
    private transient final String index;

    public EsAmbitUpdate(final String index, final JsonObject options) {
        super(options);
        this.index = index;
    }

    @Override
    public JsonObject process(final String documentId, final JsonObject body) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.client();

        try {
            final UpdateRequest request = new UpdateRequest()
                .index(this.index)
                .id(documentId)
                .doc(this.toDocument(body));

            final UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

            result
                .put("index", response.getIndex())
                .put("id", response.getId())
                .put("result", response.getResult() == DocWriteResponse.Result.UPDATED);
        } catch (final IOException ioe) {
            this.logger().fatal(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    @Override
    public Boolean process(final JsonArray documents, final String idField) {
        return this.doBatch(documents, idField, () -> {
            final BulkRequest request = new BulkRequest();
            Ut.itJArray(documents).forEach(json -> {
                final String documentId = json.getString(idField);
                if (Ut.isNotNil(documentId)) {
                    final UpdateRequest indexRequest = new UpdateRequest()
                        .index(this.index)
                        .id(documentId)
                        .doc(this.toDocument(json));
                    request.add(indexRequest);
                }
            });
            return request;
        });
    }
}
