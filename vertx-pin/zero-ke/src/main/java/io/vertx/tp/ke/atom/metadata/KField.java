package io.vertx.tp.ke.atom.metadata;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/**
 * 「Pojo」Critical Fields
 *
 * ## 1. Intro
 *
 * Here this class defined critical data structure to store `field` specification.
 *
 * ## 2. Data Format
 *
 * ```json
 * // <pre><code class="json">
 *     {
 *         "key": "",
 *         "numbers": {
 *              "fields": "numberCode1"
 *         },
 *         "unique": [
 *              [
 *                  "field1",
 *                  "field2"
 *              ]
 *         ],
 *         "created": {
 *             "by": "",
 *             "at": ""
 *         },
 *         "updated": {
 *             "by": "",
 *             "at": ""
 *         }
 *     }
 * // </code></pre>
 * ```
 *
 * ## 3. Comments
 *
 * > All the `fields` have been stored in `field` of configuration json.
 *
 * |Json|Comment|
 * |:---|:---|
 * |key|The primary key field name（One field support only）.|
 * |unique|The business unique rule of multi fields（Business Unique).|
 * |numbers|The numbers field that is related to `X_NUMBER`.|
 * |created.by|The created user id of auditor.|
 * |created.at|The created timestamp of audition.|
 * |updated.by|The updated user id of auditor.|
 * |updated.at|The updated timestamp of audition.|
 *
 * ## 4. Design
 *
 * 1. The design of zero extension module support single field primary key only, it could be defined `key`, it's proxy primary key instead of complex mode ( More than one primary key field ).
 * 2. You can provide more than one UNIQUE rule, it means that if you have primary key of multi fields defined, you can provide business rule instead of it.
 * 3. For auditor feature, here are standard four fields: `createdAt, createdBy, updatedAt, updatedBy`.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KField implements Serializable {
    /**
     * `key` field
     */
    private String key;
    /**
     * `unique` field matrix
     *
     * The data format is as `[[]]` to support more than one unique rule.
     */
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray unique;

    /**
     * `created` information of auditor.
     */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject created;
    /**
     * `updated` information of auditor.
     */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject updated;

    /**
     * `numbers` information of auditor.
     */
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject numbers;

    public String getKey() {
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public JsonArray getUnique() {
        return this.unique;
    }

    public void setUnique(final JsonArray unique) {
        this.unique = unique;
    }

    public JsonObject getCreated() {
        return this.created;
    }

    public void setCreated(final JsonObject created) {
        this.created = created;
    }

    public JsonObject getUpdated() {
        return this.updated;
    }

    public void setUpdated(final JsonObject updated) {
        this.updated = updated;
    }

    public JsonObject getNumbers() {
        return this.numbers;
    }

    public void setNumbers(final JsonObject numbers) {
        this.numbers = numbers;
    }

    @Override
    public String toString() {
        return "KField{" +
                "key='" + this.key + '\'' +
                ", unique=" + this.unique +
                ", created=" + this.created +
                ", updated=" + this.updated +
                ", numbers=" + this.numbers +
                '}';
    }
}
