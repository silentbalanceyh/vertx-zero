package io.vertx.mod.workflow.atom.configuration;

import com.fasterxml.jackson.databind.ClassDeserializer;
import com.fasterxml.jackson.databind.ClassSerializer;
import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ConfigChild implements Serializable {
    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    private Class<?> dao;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray fields = new JsonArray();

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray auditor = new JsonArray();

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private JsonArray complex = new JsonArray();

    public Class<?> getDao() {
        return this.dao;
    }

    public void setDao(final Class<?> dao) {
        this.dao = dao;
    }

    public JsonArray getFields() {
        return this.fields;
    }

    public void setFields(final JsonArray fields) {
        this.fields = fields;
    }

    public JsonArray getAuditor() {
        return this.auditor;
    }

    public void setAuditor(final JsonArray auditor) {
        this.auditor = auditor;
    }

    public JsonArray getComplex() {
        return this.complex;
    }

    public void setComplex(final JsonArray complex) {
        this.complex = complex;
    }
}
