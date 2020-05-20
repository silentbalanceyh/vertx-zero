package io.vertx.tp.plugin.elasticsearch;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.settings.put.UpdateSettingsRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
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
 * @author Hongwei
 * @since 2019/12/29, 13:31
 */

public class ElasticSearchClientImpl implements ElasticSearchClient {
    private static final Annal LOGGER = Annal.get(ElasticSearchClientImpl.class);

    private final transient Vertx vertx;
    private final transient JsonObject options = new JsonObject();
    private final transient ElasticSearchHelper helper = ElasticSearchHelper.helper(this.getClass());
    private final transient ElasticQr qr = new ElasticQr();

    ElasticSearchClientImpl(final Vertx vertx, final JsonObject options) {
        this.vertx = vertx;
        if (Ut.notNil(options)) {
            LOGGER.info("[ ZERO ] Elastic Search initialized: {0}", options.encode());
            this.options.mergeIn(options);
            this.qr.bind(options);
        }
    }

    private RestHighLevelClient getClient() {
        // Fix Bug:
        // -- java.lang.IllegalStateException: Request cannot be executed; I/O reactor status: STOPPED
        return this.helper.getClient(this.options);
    }

    public boolean connected() {
        try {
            return this.getClient().ping(RequestOptions.DEFAULT);
        } catch (final Throwable ex) {
            return false;
        }
    }

    @Override
    public JsonObject getIndex(final String index) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.getClient();

        try {
            final GetIndexRequest request = new GetIndexRequest(index)
                    .includeDefaults(true)
                    .indicesOptions(IndicesOptions.lenientExpandOpen());

            final GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);

            this.buildGetIndexResult(response, result);
        } catch (final IOException ioe) {
            LOGGER.error("failed to get index information");
            LOGGER.error(ioe.getMessage());
        }

        this.helper.closeClient(client);
        return result;
    }

    @Override
    public JsonObject createIndex(final String index, final int numberOfShards, final int numberOfReplicas, final ConcurrentMap<String, Class<?>> mappings) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.getClient();

        try {
            final CreateIndexRequest request = new CreateIndexRequest(index)
                    .alias(new Alias(this.options.getString("index")))
                    .settings(this.helper.settingsBuilder(numberOfShards, numberOfReplicas))
                    .mapping(this.helper.mappingsBuilder(mappings));

            final CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);

            result.put("isAcknowledged", response.isAcknowledged());
        } catch (final IOException ioe) {
            LOGGER.jvm(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    @Override
    public JsonObject updateIndex(final String index, final int numberOfShards, final int numberOfReplicas) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.getClient();

        try {
            final UpdateSettingsRequest request = new UpdateSettingsRequest(index)
                    .settings(this.helper.settingsBuilder(numberOfShards, numberOfReplicas));

            final AcknowledgedResponse response = client.indices().putSettings(request, RequestOptions.DEFAULT);

            result.put("isAcknowledged", response.isAcknowledged());
        } catch (final IOException ioe) {
            LOGGER.jvm(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    @Override
    public JsonObject deleteIndex(final String index) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.getClient();

        try {
            final DeleteIndexRequest request = new DeleteIndexRequest(index);

            final AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);

            result.put("isAcknowledged", response.isAcknowledged());
        } catch (final IOException | ElasticsearchException ioe) {
            LOGGER.jvm(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    @Override
    public JsonObject getDocument(final String index, final String documentId) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.getClient();

        try {
            final GetRequest request = new GetRequest()
                    .index(index)
                    .id(documentId);

            final GetResponse response = client.get(request, RequestOptions.DEFAULT);

            result
                    .put("index", response.getIndex())
                    .put("id", response.getId())
                    .put("result", response.isExists());
            if (response.isExists()) {
                result.put("data", response.getSource());
            }
        } catch (final IOException ioe) {
            LOGGER.jvm(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    @Override
    public JsonObject createDocument(final String index, final String documentId, final JsonObject source) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.getClient();

        try {
            final IndexRequest request = new IndexRequest(index)
                    .id(documentId)
                    .source(source.getMap());

            final IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            result
                    .put("index", response.getIndex())
                    .put("id", response.getId())
                    .put("result", response.getResult() == DocWriteResponse.Result.CREATED);
        } catch (final IOException ioe) {
            LOGGER.jvm(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    @Override
    public JsonObject updateDocument(final String index, final String documentId, final JsonObject source) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.getClient();

        try {
            final UpdateRequest request = new UpdateRequest()
                    .index(index)
                    .id(documentId)
                    .doc(source.getMap());

            final UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

            result
                    .put("index", response.getIndex())
                    .put("id", response.getId())
                    .put("result", response.getResult() == DocWriteResponse.Result.UPDATED);
        } catch (final IOException ioe) {
            LOGGER.jvm(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    @Override
    public JsonObject deleteDocument(final String index, final String documentId) {
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.getClient();

        try {
            final DeleteRequest request = new DeleteRequest()
                    .index(index)
                    .id(documentId);

            final DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

            result
                    .put("index", response.getIndex())
                    .put("id", response.getId())
                    .put("result", response.getResult() == DocWriteResponse.Result.DELETED);
        } catch (final IOException ioe) {
            LOGGER.jvm(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    @Override
    public JsonObject search(final JsonObject params) {
        return this.qr.search(params, null);
    }

    private void buildGetIndexResult(final GetIndexResponse response, final JsonObject result) {
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

    @Override
    public JsonObject search(final JsonObject params, final ConcurrentMap<String, String> precisionMap) {
        return this.qr.search(params, precisionMap);
    }
}
