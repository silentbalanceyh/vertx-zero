package io.vertx.tp.optic.feature;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Operation on XAttachment for files
 * - 1. Insert
 * - 2. Delete
 *
 * The condition should be following:
 *
 * // <pre><code class="json">
 * {
 *     "modelId": "identifier for MODEL_ID",
 *     "modelCategory": "category/field of model, here mapped to MODEL_CATEGORY"
 * }
 * // </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Attachment {
    /*
     * 1. Create Attachment Directly
     */
    Future<JsonArray> createAsync(JsonArray data);


    /*
     * 1. Remove Original By condition
     * 2. Create new Attachment
     */
    Future<JsonArray> saveAsync(JsonObject condition, JsonArray data);


    /*
     * 1. Fetch attachments in single field
     */
    Future<JsonArray> fetchAsync(JsonObject condition);
}
