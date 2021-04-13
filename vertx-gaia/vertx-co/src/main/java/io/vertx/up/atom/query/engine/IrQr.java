package io.vertx.up.atom.query.engine;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.atom.query.Pager;
import io.vertx.up.atom.query.Sorter;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Set;

class IrQr implements Qr {

    private transient Pager pager;
    private transient Sorter sorter;
    private transient Set<String> projection;
    private transient Criteria criteria;

    IrQr(final JsonObject input) {
        this.ensure(input);
        // Building
        this.init(input);
    }

    @SuppressWarnings("unchecked")
    private void init(final JsonObject input) {
        Fn.safeSemi(input.containsKey(KEY_PAGER), null,
                () -> this.pager = Pager.create(input.getJsonObject(KEY_PAGER)));
        Fn.safeSemi(input.containsKey(KEY_SORTER), null,
                () -> this.sorter = Sorter.create(input.getJsonArray(KEY_SORTER)));
        Fn.safeSemi(input.containsKey(KEY_PROJECTION), null,
                () -> this.projection = new HashSet<String>(input.getJsonArray(KEY_PROJECTION).getList()));
        Fn.safeSemi(input.containsKey(KEY_CRITERIA), null,
                () -> this.criteria = Criteria.create(input.getJsonObject(KEY_CRITERIA)));
    }

    private void ensure(final JsonObject input) {
        // Sorter checking
        Qr.ensureType(input, KEY_SORTER, JsonArray.class, Ut::isJArray, this.getClass());
        // Projection checking
        Qr.ensureType(input, KEY_PROJECTION, JsonArray.class, Ut::isJArray, this.getClass());
        // Pager checking
        Qr.ensureType(input, KEY_PAGER, JsonObject.class, Ut::isJObject, this.getClass());
        // Criteria
        Qr.ensureType(input, KEY_CRITERIA, JsonObject.class, Ut::isJObject, this.getClass());
    }

    @Override
    public Set<String> getProjection() {
        return this.projection;
    }

    @Override
    public Pager getPager() {
        return this.pager;
    }

    @Override
    public Sorter getSorter() {
        return this.sorter;
    }

    @Override
    public Criteria getCriteria() {
        return this.criteria;
    }

    @Override
    public void setQr(final String field, final Object value) {
        if (null == this.criteria) {
            this.criteria = Criteria.create(new JsonObject());
        }
        this.criteria.add(field, value);
    }

    @Override
    public JsonObject toJson() {
        final JsonObject result = new JsonObject();
        Fn.safeNull(() -> result.put(KEY_PAGER, this.pager.toJson()), this.pager);
        Fn.safeNull(() -> {
            final JsonObject sorters = this.sorter.toJson();
            final JsonArray array = new JsonArray();
            Ut.<Boolean>itJObject(sorters, (value, key) -> array.add(key + "," + (value ? "ASC" : "DESC")));
            result.put(KEY_SORTER, array);
        }, this.sorter);
        Fn.safeNull(() -> result.put(KEY_PROJECTION, Ut.toJArray(this.projection)), this.projection);
        Fn.safeNull(() -> result.put(KEY_CRITERIA, this.criteria.toJson()), this.criteria);
        return result;
    }
}
