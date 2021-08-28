package io.vertx.tp.plugin.elasticsearch;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EsAmbitDelete extends AbstractEsClient implements EsAmbit {
    private transient final String index;

    public EsAmbitDelete(final String index, final JsonObject options) {
        super(options);
        this.index = index;
    }

    @Override
    public JsonObject process(final String documentId, final JsonObject body) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.client();

        try {
            final DeleteRequest request = new DeleteRequest()
                .index(this.index)
                .id(documentId);

            final DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

            result
                .put("index", response.getIndex())
                .put("id", response.getId())
                .put("result", response.getResult() == DocWriteResponse.Result.DELETED);
        } catch (final IOException ioe) {
            this.logger().jvm(ioe);
        }
        this.helper.closeClient(client);
        return result;
    }

    @Override
    public Boolean process(final JsonArray data, final String idField) {
        return this.doBatch(data, idField, () -> {
            final BulkRequest request = new BulkRequest();
            Ut.itJString(data).forEach(documentId ->
                request.add(new DeleteRequest()
                    .index(this.index)
                    .id(documentId)));
            return request;
        });
    }
}
