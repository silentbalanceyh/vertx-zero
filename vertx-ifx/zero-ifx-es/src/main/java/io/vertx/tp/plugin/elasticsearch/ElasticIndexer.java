package io.vertx.tp.plugin.elasticsearch;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 * Index processor for index operations here to split the code
 * logical here for future usage
 *
 * 1) ElasticSearchHelper
 * 2) JsonObject options
 *
 * Singleton for this index management and future usage here.
 */
public class ElasticIndexer extends AbstractEsClient {

    private ElasticIndexer(final JsonObject options) {
        super(options);
    }

    static ElasticIndexer create(final JsonObject options) {
        return new ElasticIndexer(options);
    }

    JsonObject getIndex(final String index) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.client();

        try {
            final GetIndexRequest request = new GetIndexRequest(index)
                    .includeDefaults(true)
                    .indicesOptions(IndicesOptions.lenientExpandOpen());

            final GetIndexResponse response = client
                    .indices()
                    .get(request, RequestOptions.DEFAULT);

            this.toResultGet(response, result);
        } catch (final IOException ioe) {
            this.logger().error("failed to get index information");
            this.logger().error(ioe.getMessage());
        }

        this.helper.closeClient(client);
        return result;
    }

    JsonObject updateIndex(final String index, final int numberOfShards, final int numberOfReplicas) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.client();

        try {
            final UpdateSettingsRequest request = new UpdateSettingsRequest(index)
                    .settings(this.helper.settingsBuilder(numberOfShards, numberOfReplicas));

            final AcknowledgedResponse response = client.indices().putSettings(request, RequestOptions.DEFAULT);

            result.put("isAcknowledged", response.isAcknowledged());
        } catch (final IOException ioe) {
            this.logger().jvm(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    JsonObject createIndex(final String index, final int numberOfShards, final int numberOfReplicas, final ConcurrentMap<String, Class<?>> mappings) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.client();

        try {
            final CreateIndexRequest request = new CreateIndexRequest(index)
                    .alias(new Alias(this.getString("index")))
                    .settings(this.helper.settingsBuilder(numberOfShards, numberOfReplicas))
                    .mapping(this.helper.mappingsBuilder(mappings));

            final CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);

            result.put("isAcknowledged", response.isAcknowledged());
        } catch (final IOException ioe) {
            this.logger().jvm(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    JsonObject deleteIndex(final String index) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.client();

        try {
            final DeleteIndexRequest request = new DeleteIndexRequest(index);

            final AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);

            result.put("isAcknowledged", response.isAcknowledged());
        } catch (final IOException | ElasticsearchException ioe) {
            this.logger().jvm(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    private void toResultGet(final GetIndexResponse response, final JsonObject result) {
        result.put("index", Arrays.asList(response.getIndices()));

        final JsonArray aliases = new JsonArray();
        response.getAliases().forEach((key, val) -> val.forEach(item -> aliases.add(item.getAlias())));
        result.put("aliases", aliases);

        final JsonObject settings = new JsonObject();
        response.getSettings().forEach((key, val) -> {
            final JsonObject data = new JsonObject();
            val.keySet().forEach(name -> data.put(name, val.get(name)));
            settings.put(key, data);
        });
        result.put("settings", settings);

        final JsonObject mappings = new JsonObject();
        response.getMappings().forEach((key, val) -> mappings.put(key, val.getSourceAsMap()));
        result.put("mappings", mappings);
    }
}
