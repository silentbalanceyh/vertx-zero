package io.vertx.tp.plugin.elasticsearch;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Hongwei
 * @since 2019/12/29, 13:27
 */
public interface ElasticSearchClient {
    static ElasticSearchClient createShared(final Vertx vertx, final JsonObject options) {
        return new ElasticSearchClientImpl(vertx, options);
    }

    boolean connected();
    /* index API */

    /**
     * get index information
     *
     * @param index name of index
     * @return JsonObject for index information
     */
    JsonObject getIndex(String index);

    /**
     * create index with settings and mappings
     *
     * @param index            name of index. this is real index name
     * @param numberOfShards   number of shards, default is 3
     * @param numberOfReplicas number of replicas, default is 2
     * @param mappings         fields were used to create index mapping
     * @return JsonObject like below
     * {
     * "isAcknowledged": true
     * }
     */
    JsonObject createIndex(String index, int numberOfShards, int numberOfReplicas, ConcurrentMap<String, Class<?>> mappings);

    JsonObject createIndex(String index, ConcurrentMap<String, Class<?>> mappings);

    /**
     * delete index
     *
     * @param index            name of index
     * @param numberOfShards   number of shards
     * @param numberOfReplicas number of replicas
     * @return JsonObject like below
     * {
     * "isAcknowledged": true
     * }
     */
    JsonObject updateIndex(String index, int numberOfShards, int numberOfReplicas);

    JsonObject updateIndex(String index);

    /**
     * delete index by name
     *
     * @param index name of index
     * @return JsonObject like below
     * {
     * "isAcknowledged": true
     * }
     */
    JsonObject deleteIndex(String index);

    /* document API */

    /**
     * get document by document id
     *
     * @param index      name of index
     * @param documentId document id
     * @return JsonObject like below
     * {
     * "index"; "",
     * "id": "",
     * "result": true / false,
     * "data": {}
     * }
     */
    JsonObject getDocument(String index, String documentId);

    /**
     * create document from json object, must specify document id
     *
     * @param index      name of index
     * @param documentId document id
     * @param source     json object of document
     * @return JsonObject like below
     * {
     * "index"; "",
     * "id": "",
     * "result": true / false
     * }
     */
    JsonObject createDocument(String index, String documentId, JsonObject source);

    Boolean createDocuments(String index, JsonArray documents);

    Boolean createDocuments(String index, JsonArray documents, String keyField);

    /**
     * update document from json object, must specify document id
     *
     * @param index      name of index
     * @param documentId document id
     * @param source     json object of document
     * @return JsonObject like below
     * {
     * "index"; "",
     * "id": "",
     * "result": true / false
     * }
     */
    JsonObject updateDocument(String index, String documentId, JsonObject source);

    Boolean updateDocuments(String index, JsonArray documents);

    Boolean updateDocuments(String index, JsonArray documents, String keyField);

    /**
     * delete document by document id
     *
     * @param index      name of index
     * @param documentId document id
     * @return JsonObject like below
     * {
     * "index"; "",
     * "id": "",
     * "result": true / false
     * }
     */
    JsonObject deleteDocument(String index, String documentId);

    Boolean deleteDocuments(String index, Set<String> ids);

    /* full test search API */

    /**
     * get search result from ElasticSearch by search text
     *
     * @param params params will be used to search, format likes below
     *               {
     *               "index": "", --- required
     *               "searchText": "", --- required
     *               "from": 0, --- default is 0
     *               "size": 10 --- default is 10
     *               }
     * @return JsonObject like below
     * {
     * "status": "OK",
     * "took": 1,
     * "aggregations": [
     * {
     * "key": "cmdb",
     * "doc_count": 10
     * },
     * ...
     * ],
     * "total": 10,
     * "hits": [
     * {
     * "_index": "aaa",
     * "_type": "_doc",
     * "_id": "2",
     * "_score": 1.1507283,
     * "_source": {},
     * "highlight": {}
     * },
     * ...
     * ]
     * }
     */
    JsonObject search(JsonObject params);

    /*
     * params with precisionMap here
     */
    JsonObject search(JsonObject params, ConcurrentMap<String, String> precisionMap);
}
