package io.vertx.mod.jet.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.jet.atom.JtUri;
import io.vertx.mod.jet.atom.JtWorker;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

class JtDelivery {
    /*
     * Data Structure
     * {
     *      "apiKey":{
     *          {
     *              "key": "API Primary Key",
     *              "order": "Vert.x order",
     *              "api": {
     *              },
     *              "service":{
     *              },
     *              "config":{
     *              },
     *              "appId": "EmApp Key"
     *          }
     *      }
     * }
     */
    static ConcurrentMap<String, JtUri> answer(final JsonObject config) {
        final ConcurrentMap<String, JtUri> uriMap = new ConcurrentHashMap<>();
        for (final String apiKey : config.fieldNames()) {
            /*
             * Each configuration of JtUri
             */
            final JsonObject configData = config.getJsonObject(apiKey);
            final JtUri uri = new JtUri();
            uri.fromJson(configData);
            uriMap.put(apiKey, uri);
        }
        return uriMap;
    }

    /*
     * Data Structure
     * {
     *      "workerClass":{
     *          "apiKey":{
     *              {
     *                  "key": "API Primary Key",
     *                  "order": "Vert.x order",
     *                  "api": {
     *                  },
     *                  "service":{
     *                  },
     *                  "config":{
     *                  },
     *                  "appId": "EmApp Key"
     *              }
     *          }
     *      }
     * }
     */
    static ConcurrentMap<String, JsonObject> ask(final Set<JtUri> uriSet) {
        final ConcurrentMap<String, JsonObject> configMap = new ConcurrentHashMap<>();
        /*
         * Build each worker config as structure here
         */
        uriSet.forEach(uri -> {
            /*
             * Worker here
             */
            final JtWorker worker = uri.worker();
            /*
             * Consider worker name as `key`
             */
            final String key = worker.getWorkerClass().getName();
            /*
             * JsonObject configuration of JsonObject
             */
            final JsonObject config = configMap.getOrDefault(key, new JsonObject());
            /*
             * Api Key = config
             */
            config.put(uri.key(), uri.toJson());
            configMap.put(key, config);
        });
        return configMap;
    }
}
