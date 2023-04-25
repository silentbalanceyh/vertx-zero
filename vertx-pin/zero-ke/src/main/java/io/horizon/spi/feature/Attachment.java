package io.horizon.spi.feature;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Set;

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
     * 2. Create Attachment by calculation, here are content of calculation:
     * -  directory -> directoryId
     *    Parsing the expression of directory with `params` instead of uploading
     */
    Future<JsonArray> uploadAsync(JsonArray data);

    Future<JsonArray> uploadAsync(JsonArray data, JsonObject params);

    /*
     * 1. Remove Original By condition
     * 2. Create new Attachment
     */
    Future<JsonArray> saveAsync(JsonObject condition, JsonArray data);

    Future<JsonArray> saveAsync(JsonObject condition, JsonArray data, JsonObject params);

    /*
     * 1. Remove Original Only
     */
    Future<JsonArray> removeAsync(JsonObject condition);

    Future<JsonArray> purgeAsync(JsonArray attachment);

    Future<JsonArray> updateAsync(JsonArray attachment, boolean active);

    /*
     * 1. Fetch attachments in single field
     * 2. Here deeply fetch will put `visit` information into attachment
     *    to inherit from `directory`
     */
    Future<JsonArray> fetchAsync(JsonObject condition);

    // ----------------- File Interface ----------------------

    Future<Buffer> downloadAsync(Set<String> keys);

    Future<Buffer> downloadAsync(String key);

    // ----------------- Remove Condition ----------------------
}
