package io.vertx.tp.plugin.excel.atom;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.up.atom.pojo.Mirror;
import io.vertx.up.atom.pojo.Mojo;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Connect configuration data to
 * Dao / Pojo class
 */
public class ExConnect implements Serializable {

    private transient String table;
    private transient Class<?> pojo;
    private transient Class<?> dao;
    private transient String pojoFile;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray unique;
    private transient String key;

    public String getTable() {
        return this.table;
    }

    public void setTable(final String table) {
        this.table = table;
    }

    public Class<?> getPojo() {
        return this.pojo;
    }

    public void setPojo(final Class<?> pojo) {
        this.pojo = pojo;
    }

    public Class<?> getDao() {
        return this.dao;
    }

    public void setDao(final Class<?> dao) {
        this.dao = dao;
    }

    public String getPojoFile() {
        return this.pojoFile;
    }

    public void setPojoFile(final String pojoFile) {
        this.pojoFile = pojoFile;
    }

    public JsonArray getUnique() {
        return this.unique;
    }

    public void setUnique(final JsonArray unique) {
        this.unique = unique;
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public Set<String> ukOut() {
        if (Objects.isNull(this.pojoFile) || Ut.isNil(this.unique)) {
            return Ut.toSet(this.unique);
        } else {
            final Set<String> result = new HashSet<>();
            final Mojo mojo = Mirror.create(this.getClass())
                .mount(this.pojoFile)
                .type(this.getDao()).mojo();
            Ut.itJArray(this.unique, String.class, (field, index) -> {
                final String converted = mojo.getOut(field);
                if (Objects.isNull(converted)) {
                    result.add(field);
                } else {
                    result.add(converted);
                }
            });
            return result;
        }
    }

    public Set<String> ukIn() {
        return Ut.toSet(this.unique);
    }

    public String pkIn() {
        return this.key;
    }

    public String pkOut() {
        /*
         * Calculated for Pojo Part
         * 1. When pojo file is null, return key directly.
         * 2. Second situation, when this table is RELATION table, this.key is null.
         */
        if (Objects.isNull(this.pojoFile) || Objects.isNull(this.key)) {
            return this.key;
        } else {
            final Mojo mojo = Mirror.create(this.getClass())
                .mount(this.pojoFile)
                .type(this.getDao()).mojo();
            return mojo.getOut(this.key);
        }
    }

    @Override
    public String toString() {
        return "ExConnect{" +
            "table='" + this.table + '\'' +
            ", pojo=" + this.pojo +
            ", dao=" + this.dao +
            ", pojoFile='" + this.pojoFile + '\'' +
            ", unique=" + this.unique +
            ", key='" + this.key + '\'' +
            '}';
    }
}
