package io.vertx.tp.plugin.elasticsearch;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractEsClient {

    protected final transient ElasticSearchHelper helper =
        ElasticSearchHelper.helper(this.getClass());
    private final transient JsonObject options = new JsonObject();

    AbstractEsClient(final JsonObject options) {
        if (Ut.isNotNil(options)) {
            this.options.mergeIn(options.copy());
        }
    }

    protected RestHighLevelClient client() {
        // Fix Bug:
        // -- java.lang.IllegalStateException: Request cannot be executed; I/O reactor status: STOPPED
        return this.helper.getClient(this.options);
    }

    protected String getString(final String field) {
        return this.options.getString(field);
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }

    protected Boolean doBatch(final JsonArray documents, final String idField,
                              final Supplier<BulkRequest> executor) {
        if (Ut.isNil(documents)) {
            /*
             * No data, not needed
             */
            return true;
        } else {
            final RestHighLevelClient client = this.client();
            boolean result;
            try {
                final BulkRequest request = executor.get();
                final BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
                if (bulkResponse.hasFailures()) {
                    this.logger().warn("Failure found: {0}", bulkResponse.buildFailureMessage());
                    result = false;
                } else {
                    this.logger().info("Documents have been indexed ( size = {0} ) successfully!", documents.size());
                    result = true;
                }
            } catch (final IOException ioe) {
                this.logger().fatal(ioe);
                result = false;
            }
            this.helper.closeClient(client);
            return result;
        }
    }

    protected Map<String, Object> toDocument(final JsonObject json) {
        final Map<String, Object> originalMap = json.getMap();
        final Map<String, Object> processedMap = new TreeMap<>();
        originalMap.forEach((key, value) -> {
            /*
             * Exclude JsonObject / JsonArray
             */
            if (!(value instanceof JsonObject || value instanceof JsonArray)) {
                processedMap.put(key, value);
            }
        });
        return processedMap;
    }
}
