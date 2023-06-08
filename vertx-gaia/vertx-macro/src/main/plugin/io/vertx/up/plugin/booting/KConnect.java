package io.vertx.up.plugin.booting;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.up.commune.pojo.Mirror;
import io.vertx.up.commune.pojo.Mojo;
import io.vertx.up.plugin.jooq.JooqPin;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Connect configuration data to
 * Dao / Pojo class
 */
public class KConnect implements Serializable {

    private transient Class<?> dao;
    private transient String pojoFile;

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray unique;

    private transient String key;

    public String getTable() {
        Objects.requireNonNull(this.dao);
        return JooqPin.initTable(this.dao);
    }

    public Class<?> getPojo() {
        Objects.requireNonNull(this.dao);
        return JooqPin.initPojo(this.dao);
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

    public Set<String> ukIn() {
        if (Objects.isNull(this.pojoFile)) {
            return Ut.toSet(this.unique);
        } else {
            if (Ut.isNil(this.unique)) {
                return new HashSet<>();
            }
            final Set<String> result = new HashSet<>();
            final Mojo mojo = Mirror.create(this.getClass())
                .mount(this.pojoFile)
                .type(this.getDao()).mojo();
            Ut.itJArray(this.unique, String.class, (field, index) -> {
                final String converted = mojo.getIn(field);
                if (Objects.isNull(converted)) {
                    result.add(field);
                } else {
                    result.add(converted);
                }
            });
            return result;
        }
    }

    public String pkIn() {
        /*
         * Calculated for Pojo Part
         * 1. When pojo file is null, return key directly.
         * 2. Second situation, when this table is RELATION table, this.key is null.
         */
        if (Objects.isNull(this.pojoFile)) {
            return this.key;
        } else {
            if (Objects.isNull(this.key)) {
                return null;
            }
            final Mojo mojo = Mirror.create(this.getClass())
                .mount(this.pojoFile)
                .type(this.getDao()).mojo();
            return mojo.getIn(this.key);
        }
    }

    @Override
    public String toString() {
        return "ExConnect{" +
            ", dao=" + this.dao +
            ", pojoFile='" + this.pojoFile + '\'' +
            ", unique=" + this.unique +
            ", key='" + this.key + '\'' +
            '}';
    }
}
