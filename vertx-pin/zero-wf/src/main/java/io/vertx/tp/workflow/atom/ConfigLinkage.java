package io.vertx.tp.workflow.atom;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ConfigLinkage implements Serializable {

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray fields = new JsonArray();

    private transient ConcurrentMap<String, JsonObject> condition = new ConcurrentHashMap<>();

    public JsonArray getFields() {
        return this.fields;
    }

    public void setFields(final JsonArray fields) {
        this.fields = fields;
    }

    public ConcurrentMap<String, JsonObject> getCondition() {
        return this.condition;
    }

    public void setCondition(final ConcurrentMap<String, JsonObject> condition) {
        this.condition = condition;
    }

    public Set<String> fields() {
        return Ut.toSet(this.fields);
    }

    public JsonObject condition(final String field) {
        final JsonObject conditionJ = this.condition.getOrDefault(field, new JsonObject());
        conditionJ.put(Strings.EMPTY, Boolean.TRUE);
        return conditionJ.copy();
    }
}
