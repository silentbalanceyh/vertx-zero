package io.aeon.experiment.reference;

import io.horizon.atom.Kv;
import io.horizon.eon.VString;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RQuery implements Serializable {
    /**
     * Attribute Name
     */
    private final String name;
    /**
     * Source Field
     */
    private final String sourceField;

    private final List<Kv<String, String>> joined = new ArrayList<>();
    /**
     * RDao reference
     */
    private RDao daoRef;

    public RQuery(final String name, final String sourceField) {
        this.name = name;
        this.sourceField = sourceField;
    }

    public RQuery bind(final RDao dao) {
        this.daoRef = dao;
        return this;
    }

    public RQuery bind(final List<Kv<String, String>> result) {
        this.joined.addAll(result);
        return this;
    }

    public JsonArray fetchBy(final String op, final Object value) {
        final JsonObject condition = new JsonObject();
        if (value instanceof JsonArray) {
            condition.put(this.sourceField + ",i", value);
        } else {
            final String operator = Ut.isNil(op) ? Qr.Op.EQ : op;
            condition.put(this.sourceField + "," + operator, value);
        }
        condition.put(VString.EMPTY, Boolean.TRUE);
        return this.daoRef.fetchBy(condition);
    }

    public ConcurrentMap<String, JsonArray> fetchQuery(final JsonArray data) {
        final ConcurrentMap<String, JsonArray> query = new ConcurrentHashMap<>();
        this.joined.forEach(kv -> {
            final String name = kv.value();
            final String extract = kv.key();

            final JsonArray compress = Ut.toJArray(Ut.valueSetString(data, extract));
            query.put(name, compress);
        });
        return query;
    }
}
