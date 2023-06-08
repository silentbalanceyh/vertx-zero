package io.vertx.up.plugin.elasticsearch;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.error.elasticsearch._404IndexNameMissingExceptionn;
import io.vertx.up.error.elasticsearch._404SearchTextMissingExceptionn;
import io.vertx.up.fn.Fn;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;

class ElasticQr {

    private static final Annal LOGGER = Annal.get(ElasticQr.class);

    private final transient ElasticSearchHelper helper = ElasticSearchHelper.helper(this.getClass());
    private final transient JsonObject options = new JsonObject();

    ElasticQr bind(final JsonObject options) {
        this.options.mergeIn(options.copy(), true);
        return this;
    }

    JsonObject search(final JsonObject params, final ConcurrentMap<String, String> precision) {
        /* Params Checking */
        this.paramRequired(params);
        /* Client Extraction */
        final JsonObject result = new JsonObject();
        final RestHighLevelClient client = this.helper.getClient(this.options);
        try {
            final String index = params.getString("index");
            final String searchText = params.getString("searchText");
            final int from = params.containsKey("from") ? params.getInteger("from") : 0;
            final int size = params.containsKey("size") ? params.getInteger("size") : 10;

            final SearchSourceBuilder condition = this.helper.searchSourceBuilder(searchText, precision, from, size);
            final SearchRequest request = new SearchRequest(index).source(condition);
            final SearchResponse response = client.search(request, RequestOptions.DEFAULT);

            result
                .put("index", this.options.getString("index"))
                .put("status", response.status().name())
                .put("took", response.getTook().seconds())
                .put("total", response.getHits().getTotalHits().value);
            /*
             * Response Building
             */
            this.getHitsAndAggregationsFromResponse(response, result);
        } catch (final IOException ioe) {
            LOGGER.fatal(ioe);
        }
        this.helper.closeClient(client);
        return result;
    }

    private void getHitsAndAggregationsFromResponse(final SearchResponse response, final JsonObject result) {
        final JsonArray hits = new JsonArray();
        Arrays.stream(response.getHits().getHits()).forEach(hit -> {
            final JsonObject data = new JsonObject()
                .put("index", hit.getIndex())
                .put("id", hit.getId())
                .put("score", hit.getScore())
                .put("source", hit.getSourceAsMap());
            hits.add(data);
        });
        result.put("hits", hits);

        final JsonArray aggregations = new JsonArray();
        final Aggregations aggres = response.getAggregations();
        final Terms customAggregation = aggres.get(Aggregations.AGGREGATIONS_FIELD);
        customAggregation.getBuckets()
            .forEach(item -> aggregations.add(new JsonObject()
                .put("key", item.getKeyAsString())
                .put("count", item.getDocCount()))
            );
        result.put("aggregations", aggregations);
    }

    private void paramRequired(final JsonObject params) {
        Fn.outWeb(!params.containsKey("index"), _404IndexNameMissingExceptionn.class, this.getClass());
        Fn.outWeb(!params.containsKey("searchText"), _404SearchTextMissingExceptionn.class, this.getClass());
    }
}
