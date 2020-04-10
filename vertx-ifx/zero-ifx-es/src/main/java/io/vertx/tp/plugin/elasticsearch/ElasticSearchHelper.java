package io.vertx.tp.plugin.elasticsearch;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._404ConfigurationMissingExceptionn;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author Hongwei
 * @since 2019/12/29, 13:31
 */

public class ElasticSearchHelper {
	private static final Annal LOGGER = Annal.get(ElasticSearchHelper.class);

	private transient final Class<?> target;

	private ElasticSearchHelper(final Class<?> target) {
		this.target = target;
	}

	static ElasticSearchHelper helper(final Class<?> target) {
		return Fn.pool(Pool.HELPERS, target.getName(), () -> new ElasticSearchHelper(target));
	}

	RestHighLevelClient getClient(final JsonObject options) {
		Fn.outWeb(Ut.isNil(options), _404ConfigurationMissingExceptionn.class, this.getClass());

		final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
		credentialsProvider.setCredentials(AuthScope.ANY,
			new UsernamePasswordCredentials(options.getString("username"), options.getString("password"))
		);

		return new RestHighLevelClient(
			RestClient.builder(
				new HttpHost(options.getString("hostname"), options.getInteger("port"), options.getString("scheme"))
			)
			.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
				@Override
				public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
					httpAsyncClientBuilder.disableAuthCaching();
					return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
						.setMaxConnTotal(100)
						.setMaxConnPerRoute(100);
				}
			})
			.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
				@Override
				public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
					return builder.setConnectionRequestTimeout(60000)
						.setConnectTimeout(60000)
						.setSocketTimeout(60000);
				}
			})
		);
	}

	void closeClient(final RestHighLevelClient client) {
		try {
			client.close();
		} catch (IOException ioe) {
			LOGGER.error("error occurred when close elasticsearch connection", ioe.getMessage());
		}
	}

	Settings settingsBuilder(final int numberOfShards, final int numberOfReplicas) {
		return Settings.builder()
			.put("index.number_of_shards", numberOfShards > 0 ? numberOfShards : 3)
			.put("index.number_of_replicas", numberOfReplicas > 0 ? numberOfReplicas: 2)
			.build();
	}

	/**
	 * build mappings for index from fields and fields' type
	 * @param mappings fields with type, format like below
	 * {
	 *   "field": "String",
	 *   ...
	 * }
	 * @return translated map object
	 */
	Map<String, Object> mappingsBuilder(final ConcurrentMap<String, Class<?>> mappings) {
		Map<String, Object> properties = new HashMap<>();

		// process field: key
		if (mappings.containsKey("key")) {
			Map<String, Object> keyProp = new HashMap<>();
			keyProp.put("type", "keyword");
			keyProp.put("index", true);
			properties.put("key", keyProp);
			mappings.remove("key");
		}

		// process rest fields
		mappings.forEach((key, val) -> {
			Map<String, Object> props = new HashMap<>();
			if (val == String.class) {
				props.put("type", "text");
				props.put("index", "true");
				props.put("analyzer", "ik_max_word");
			} else if (val == java.time.LocalTime.class || val == java.time.LocalDateTime.class ||
							val == java.time.LocalDate.class || val == java.time.Instant.class) {
				props.put("type", "date");
				props.put("index", "true");
				props.put("format", "yyyy-MM-dd||yyyy-MM-dd HH:mm:ss||yyyy-MM-dd HH:mm:ss.SSS||yyyy-MM-dd'T'HH:mm:ss'Z'||yyyy-MM-dd'T'HH:mm:ss.SSS'Z'||epoch_millis");
			} else if (val == java.lang.Integer.class) {
				props.put("type", "integer");
				props.put("index", "true");
			} else if (val == java.lang.Long.class) {
				props.put("type", "long");
				props.put("index", "true");
			} else if (val == java.lang.Double.class || val == java.lang.Float.class || val == java.math.BigDecimal.class) {
				props.put("type", "double");
				props.put("index", "true");;
			} else if (val == java.lang.Boolean.class) {
				props.put("type", "boolean");
				props.put("index", "true");;
			} else {
				props.put("type", "text");
				props.put("index", "true");
				props.put("analyzer", "ik_max_word");
			}

			properties.put(key, props);
		});

		Map<String, Object> result = new HashMap<>();
		result.put("properties", properties);
		return result;
	}

	SearchSourceBuilder searchSourceBuilder(final String searchText, int from, int size) {
		return new SearchSourceBuilder()
			.query(QueryBuilders.queryStringQuery(searchText))
			.aggregation(AggregationBuilders.terms(Aggregations.AGGREGATIONS_FIELD).field("_index"))
			.highlighter(new HighlightBuilder().field("*").preTags("<strong>").postTags("</strong>").highlighterType("unified"))
			.from(Math.max(0, from))
			.size(Math.max(10, size))
			.timeout(new TimeValue(10, TimeUnit.SECONDS));
	}
}
