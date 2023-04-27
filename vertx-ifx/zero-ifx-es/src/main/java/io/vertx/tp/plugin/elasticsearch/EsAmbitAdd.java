package io.vertx.tp.plugin.elasticsearch;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EsAmbitAdd extends AbstractEsClient implements EsAmbit {
    private transient final String index;

    public EsAmbitAdd(final String index, final JsonObject options) {
        super(options);
        this.index = index;
    }

    @Override
    public JsonObject process(final String documentId, final JsonObject body) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.client();

        try {
            final IndexRequest request = new IndexRequest(this.index)
                .id(documentId)
                .source(this.toDocument(body));

            final IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            result.put("index", response.getIndex()).put("id", response.getId())
                .put("result", response.getResult() == DocWriteResponse.Result.CREATED);
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
                    final IndexRequest indexRequest = new IndexRequest(this.index)
                        .id(documentId)
                        .source(this.toDocument(json));
                    request.add(indexRequest);
                }
            });
            return request;
        });
    }
}
