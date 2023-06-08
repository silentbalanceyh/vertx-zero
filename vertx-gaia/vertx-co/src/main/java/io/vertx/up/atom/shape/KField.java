package io.vertx.up.atom.shape;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
 *         },
 *         "object": [],
 *         "array": [],
 *         "attachment": []
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

    /**
     * `object` information
     */
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray object;

    /**
     * `array` information
     */
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray array;

    /**
     * `attachment` information
     */
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray attachment;

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

    public JsonArray getObject() {
        return this.object;
    }

    public void setObject(final JsonArray object) {
        this.object = object;
    }

    public JsonArray getArray() {
        return this.array;
    }

    public void setArray(final JsonArray array) {
        this.array = array;
    }

    public JsonArray getAttachment() {
        return this.attachment;
    }

    public void setAttachment(final JsonArray attachment) {
        this.attachment = attachment;
    }

    public Set<String> fieldArray() {
        return Ut.toSet(this.array);
    }

    public Set<String> fieldObject() {
        return Ut.toSet(this.object);
    }

    public Set<String> fieldAudit() {
        final Set<String> set = new HashSet<>();
        final JsonObject created = Ut.valueJObject(this.created);
        if (Objects.nonNull(created.getValue(KName.BY))) {
            set.add(created.getString(KName.BY));
        }
        final JsonObject updated = Ut.valueJObject(this.updated);
        if (Objects.nonNull(updated.getValue(KName.BY))) {
            set.add(updated.getString(KName.BY));
        }
        return set;
    }

    public ConcurrentMap<String, JsonObject> fieldFile() {
        final JsonArray attachments = Ut.valueJArray(this.attachment);
        final ConcurrentMap<String, JsonObject> fieldMap = new ConcurrentHashMap<>();
        Ut.itJArray(attachments).forEach(attachment -> {
            final String field = attachment.getString(KName.FIELD);
            final Object value = attachment.getValue("condition");
            if (Ut.isNotNil(field) && value instanceof JsonObject) {
                fieldMap.put(field, (JsonObject) value);
            }
        });
        return fieldMap;
    }

    @Override
    public String toString() {
        return "KField{" +
            "key='" + this.key + '\'' +
            ", unique=" + this.unique +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ", numbers=" + this.numbers +
            ", object=" + this.object +
            ", array=" + this.array +
            ", attachment=" + this.attachment +
            '}';
    }
}
