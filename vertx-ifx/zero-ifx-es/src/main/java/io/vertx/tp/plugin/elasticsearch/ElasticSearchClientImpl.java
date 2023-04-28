package io.vertx.tp.plugin.elasticsearch;

import io.horizon.eon.em.ChangeFlag;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Hongwei
 * @since 2019/12/29, 13:31
 */

public class ElasticSearchClientImpl implements ElasticSearchClient {
    private static final Annal LOGGER = Annal.get(ElasticSearchClientImpl.class);

    private final transient Vertx vertx;
    private final transient JsonObject options = new JsonObject();
    @Deprecated
    private final transient ElasticSearchHelper helper = ElasticSearchHelper.helper(this.getClass());
    private final transient ElasticQr qr = new ElasticQr();
    // Indexer for index management
    private final transient ElasticIndexer indexer;

    ElasticSearchClientImpl(final Vertx vertx, final JsonObject options) {
        this.vertx = vertx;
        if (Ut.isNotNil(options)) {
            LOGGER.info("[ ZERO ] Elastic Search initialized: {0}", options.encode());
            this.options.mergeIn(options);
            this.qr.bind(options);
        }
        // Create new indexer to simply the index processing
        this.indexer = ElasticIndexer.create(this.options);
    }

    private RestHighLevelClient getClient() {
        // Fix Bug:
        // -- java.lang.IllegalStateException: Request cannot be executed; I/O reactor status: STOPPED
        return this.helper.getClient(this.options);
    }

    @Override
    public boolean connected() {
        // Check connection
        return Fn.failOr(Boolean.FALSE, () -> this.getClient().ping(RequestOptions.DEFAULT));
    }

    @Override
    public JsonObject getIndex(final String index) {
        // Get index information
        return this.indexer.getIndex(index);
    }

    @Override
    public JsonObject deleteIndex(final String index) {
        // Delete index information
        return this.indexer.deleteIndex(index);
    }

    @Override
    public JsonObject createIndex(final String index, final ConcurrentMap<String, Class<?>> mappings) {
        // Create Index ( Default )
        return this.indexer.createIndex(index, 5, 1, mappings);
    }

    @Override
    public JsonObject createIndex(final String index, final int numberOfShards, final int numberOfReplicas, final ConcurrentMap<String, Class<?>> mappings) {
        // Create Index based on input parameters
        return this.indexer.createIndex(index, numberOfShards, numberOfReplicas, mappings);
    }

    @Override
    public JsonObject updateIndex(final String index) {
        // Update Index here
        return this.indexer.updateIndex(index, 5, 1);
    }

    @Override
    public JsonObject updateIndex(final String index, final int numberOfShards, final int numberOfReplicas) {
        // Update Index based on input parameters
        return this.indexer.updateIndex(index, numberOfShards, numberOfReplicas);
    }

    @Override
    public JsonObject createDocument(final String index, final String documentId, final JsonObject source) {
        final EsAmbit ambit = EsAmbit.create(ChangeFlag.ADD, index, this.options);
        return ambit.process(documentId, source);
    }

    @Override
    public Boolean createDocuments(final String index, final JsonArray documents) {
        return this.createDocuments(index, documents, "key");
    }

    @Override
    public Boolean createDocuments(final String index, final JsonArray documents, final String keyField) {
        final EsAmbit ambit = EsAmbit.create(ChangeFlag.ADD, index, this.options);
        return ambit.process(documents, keyField);
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
            LOGGER.fatal(ioe);
        }

        this.helper.closeClient(client);
        return result;
    }

    @Override
    public JsonObject updateDocument(final String index, final String documentId, final JsonObject source) {
        final EsAmbit ambit = EsAmbit.create(ChangeFlag.UPDATE, index, this.options);
        return ambit.process(documentId, source);
    }

    @Override
    public Boolean updateDocuments(final String index, final JsonArray documents) {
        final EsAmbit ambit = EsAmbit.create(ChangeFlag.UPDATE, index, this.options);
        return ambit.process(documents, "key");
    }

    @Override
    public Boolean updateDocuments(final String index, final JsonArray documents, final String keyField) {
        final EsAmbit ambit = EsAmbit.create(ChangeFlag.UPDATE, index, this.options);
        return ambit.process(documents, keyField);
    }

    @Override
    public JsonObject deleteDocument(final String index, final String documentId) {
        final EsAmbit ambit = EsAmbit.create(ChangeFlag.DELETE, index, this.options);
        return ambit.process(documentId, null);
    }

    @Override
    public Boolean deleteDocuments(final String index, final Set<String> ids) {
        final EsAmbit ambit = EsAmbit.create(ChangeFlag.DELETE, index, this.options);
        return ambit.process(Ut.toJArray(ids), null);
    }

    @Override
    public JsonObject search(final JsonObject params) {
        return this.qr.search(params, null);
    }

    @Override
    public JsonObject search(final JsonObject params, final ConcurrentMap<String, String> precisionMap) {
        return this.qr.search(params, precisionMap);
    }
}
