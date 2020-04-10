package io.vertx.tp.plugin.elasticsearch;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404IndexNameMissingExceptionn;
import io.vertx.tp.error._404SearchTextMissingExceptionn;
import io.vertx.up.fn.Fn;
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
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
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
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

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

	ElasticSearchClientImpl(final Vertx vertx, final JsonObject options) {
		this.vertx = vertx;
		if (Ut.notNil(options)) {
			this.options.mergeIn(options);
		}
	}

	@Override
	public JsonObject getIndex(String index) {
		JsonObject result = new JsonObject();
		RestHighLevelClient client = helper.getClient(options);

		try {
			GetIndexRequest request = new GetIndexRequest(index)
				.includeDefaults(true)
				.indicesOptions(IndicesOptions.lenientExpandOpen());

			GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);

			this.buildGetIndexResult(response, result);
		} catch (IOException ioe) {
			LOGGER.error("failed to get index information");
			LOGGER.error(ioe.getMessage());
		}

		helper.closeClient(client);
		return result;
	}

	@Override
	public JsonObject createIndex(String index, int numberOfShards, int numberOfReplicas, ConcurrentMap<String, Class<?>> mappings) {
		JsonObject result = new JsonObject();
		RestHighLevelClient client = helper.getClient(options);

		try {
			CreateIndexRequest request = new CreateIndexRequest(index)
				.alias(new Alias(options.getString("index")))
				.settings(helper.settingsBuilder(numberOfShards, numberOfReplicas))
				.mapping(helper.mappingsBuilder(mappings));

			CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);

			result.put("isAcknowledged", response.isAcknowledged());
		} catch (IOException ioe) {
			LOGGER.error("failed to create index");
			LOGGER.error(ioe.getMessage());
		}

		helper.closeClient(client);
		return result;
	}

	@Override
	public JsonObject updateIndex(String index, int numberOfShards, int numberOfReplicas) {
		JsonObject result = new JsonObject();
		RestHighLevelClient client = helper.getClient(options);

		try {
			UpdateSettingsRequest request = new UpdateSettingsRequest(index)
				.settings(helper.settingsBuilder(numberOfShards, numberOfReplicas));

			AcknowledgedResponse response = client.indices().putSettings(request, RequestOptions.DEFAULT);

			result.put("isAcknowledged", response.isAcknowledged());
		} catch (IOException ioe) {
			LOGGER.error("failed to update index settings");
			LOGGER.error(ioe.getMessage());
		}

		helper.closeClient(client);
		return result;
	}

	@Override
	public JsonObject deleteIndex(String index) {
		JsonObject result = new JsonObject();
		RestHighLevelClient client = helper.getClient(options);

		try {
			DeleteIndexRequest request = new DeleteIndexRequest(index);

			AcknowledgedResponse response = client.indices().delete(request, RequestOptions.DEFAULT);

			result.put("isAcknowledged", response.isAcknowledged());
		} catch (IOException ioe) {
			LOGGER.debug("failed to delete index", ioe.getMessage());
		} catch (ElasticsearchException ese) {
			LOGGER.debug("failed to delete index: {0}, {1}", ese.status(), ese.getMessage());
		}

		helper.closeClient(client);
		return result;
	}

	@Override
	public JsonObject getDocument(String index, String documentId) {
		JsonObject result = new JsonObject();
		RestHighLevelClient client = helper.getClient(options);

		try {
			GetRequest request = new GetRequest()
				.index(index)
				.id(documentId);

			GetResponse response = client.get(request, RequestOptions.DEFAULT);

			result
				.put("index", response.getIndex())
				.put("id", response.getId())
				.put("result", response.isExists());
			if (response.isExists()) {
				result.put("data", response.getSource());
			}
		} catch (IOException ioe) {
			LOGGER.error("failed to get document, document id is {0}", documentId);
			LOGGER.error(ioe.getMessage());
		}

		helper.closeClient(client);
		return result;
	}

	@Override
	public JsonObject createDocument(String index, String documentId, JsonObject source) {
		JsonObject result = new JsonObject();
		RestHighLevelClient client = helper.getClient(options);

		try {
			IndexRequest request = new IndexRequest(index)
				.id(documentId)
				.source(source.getMap());

			IndexResponse response  = client.index(request, RequestOptions.DEFAULT);

			result
				.put("index", response.getIndex())
				.put("id", response.getId())
				.put("result", response.getResult() == DocWriteResponse.Result.CREATED);
		} catch (IOException ioe) {
			LOGGER.error("failed to create document, document id is {0}", documentId);
			LOGGER.error(ioe.getMessage());
		}

		helper.closeClient(client);
		return result;
	}

	@Override
	public JsonObject updateDocument(String index, String documentId, JsonObject source) {
		JsonObject result = new JsonObject();
		RestHighLevelClient client = helper.getClient(options);

		try {
			UpdateRequest request = new UpdateRequest()
				.index(index)
				.id(documentId)
				.doc(source.getMap());

			UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

			result
				.put("index", response.getIndex())
				.put("id", response.getId())
				.put("result", response.getResult() == DocWriteResponse.Result.UPDATED);
		} catch (IOException ioe) {
			LOGGER.error("failed to update document, document id is {0}", documentId);
			LOGGER.error(ioe.getMessage());
		}

		helper.closeClient(client);
		return result;
	}

	@Override
	public JsonObject deleteDocument(String index, String documentId) {
		JsonObject result = new JsonObject();
		RestHighLevelClient client = helper.getClient(options);

		try {
			DeleteRequest request = new DeleteRequest()
				.index(index)
				.id(documentId);

			DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);

			result
				.put("index", response.getIndex())
				.put("id", response.getId())
				.put("result", response.getResult() == DocWriteResponse.Result.DELETED);
		} catch (IOException ioe) {
			LOGGER.error("failed to delete document, document id is {0}", documentId);
			LOGGER.error(ioe.getMessage());
		}

		helper.closeClient(client);
		return result;
	}

	@Override
	public JsonObject search(JsonObject params) {
		Fn.outWeb(!params.containsKey("index"), _404IndexNameMissingExceptionn.class, this.getClass());
		Fn.outWeb(!params.containsKey("searchText"), _404SearchTextMissingExceptionn.class, this.getClass());
		JsonObject result = new JsonObject();
		RestHighLevelClient client = helper.getClient(options);

		try {
			final String index = params.getString("index");
			final String searchText = params.getString("searchText");
			final int from = params.containsKey("from") ? params.getInteger("from") : 0;
			final int size = params.containsKey("size") ? params.getInteger("size") : 10;

			SearchRequest request = new SearchRequest(index)
				.source(helper.searchSourceBuilder(searchText, from, size));

			SearchResponse response = client.search(request, RequestOptions.DEFAULT);

			result
				.put("index", options.getString("index"))
				.put("status", response.status().name())
				.put("took", response.getTook().seconds())
				.put("total", response.getHits().getTotalHits().value);
			this.getHitsAndAggregationsFromResponse(response, result);
		} catch (IOException ioe) {
			LOGGER.error("failed to get search result from elasticsearch");
			LOGGER.error(ioe.getMessage());
		}

		helper.closeClient(client);
		return result;
	}

	private void buildGetIndexResult(final GetIndexResponse response, final JsonObject result) {
		result.put("index", Arrays.asList(response.getIndices()));

		JsonArray aliases = new JsonArray();
		response.getAliases().forEach((key, val) -> val.forEach(item -> aliases.add(item.getAlias())));
		result.put("aliases", aliases);

		JsonObject settings = new JsonObject();
		response.getSettings().forEach((key, val) -> {
			JsonObject data = new JsonObject();
			val.keySet().forEach(name -> data.put(name, val.get(name)));
			settings.put(key, data);
		});
		result.put("settings", settings);

		JsonObject mappings = new JsonObject();
		response.getMappings().forEach((key, val) -> mappings.put(key, val.getSourceAsMap()));
		result.put("mappings", mappings);
	}

	private void getHitsAndAggregationsFromResponse(final SearchResponse response, final JsonObject result) {
		JsonArray hits = new JsonArray();
		Arrays.stream(response.getHits().getHits()).forEach(hit -> {
			JsonObject data = new JsonObject()
				.put("index", hit.getIndex())
				.put("id", hit.getId())
				.put("score", hit.getScore())
				.put("source", hit.getSourceAsMap());
			hits.add(data);
		});
		result.put("hits", hits);

		JsonArray aggregations = new JsonArray();
		Aggregations aggres = response.getAggregations();
		Terms customAggregation = aggres.get(Aggregations.AGGREGATIONS_FIELD);
		customAggregation.getBuckets()
			.forEach(item -> aggregations.add(new JsonObject()
				.put("key", item.getKeyAsString())
				.put("count", item.getDocCount()))
			);
		result.put("aggregations", aggregations);
	}
}
