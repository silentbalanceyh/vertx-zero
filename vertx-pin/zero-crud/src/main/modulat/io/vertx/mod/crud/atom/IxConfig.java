package io.vertx.mod.crud.atom;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;

import java.io.Serializable;

public class IxConfig implements Serializable {
    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray patterns;

    private transient String columnKeyField = "dataIndex";  // Render for column
    private transient String columnLabelField = "title";

    public JsonArray getPatterns() {
        return this.patterns;
    }

    public void setPatterns(final JsonArray patterns) {
        this.patterns = patterns;
    }

    public String getColumnKeyField() {
        return this.columnKeyField;
    }

    public void setColumnKeyField(final String columnKeyField) {
        this.columnKeyField = columnKeyField;
    }

    public String getColumnLabelField() {
        return this.columnLabelField;
    }

    public void setColumnLabelField(final String columnLabelField) {
        this.columnLabelField = columnLabelField;
    }

    @Override
    public String toString() {
        return "IxConfig{" +
            "patterns=" + this.patterns +
            ", columnKeyField='" + this.columnKeyField + '\'' +
            ", columnLabelField='" + this.columnLabelField + '\'' +
            '}';
    }
}
