package io.horizon.uca.qr.syntax;

import io.horizon.fn.HFn;
import io.horizon.uca.qr.Criteria;
import io.horizon.uca.qr.Pager;
import io.horizon.uca.qr.Sorter;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;

class IrQr implements Ir {

    private Pager pager;
    private Sorter sorter;
    private Set<String> projection;
    private Criteria criteria;

    IrQr(final JsonObject input) {
        this.ensure(input);
        // Building
        this.init(input);
    }

    @SuppressWarnings("unchecked")
    private void init(final JsonObject input) {
        HFn.runAt(input.containsKey(KEY_PAGER),
            () -> this.pager = Pager.create(input.getJsonObject(KEY_PAGER)));
        HFn.runAt(input.containsKey(KEY_SORTER),
            () -> this.sorter = Sorter.create(input.getJsonArray(KEY_SORTER)));
        HFn.runAt(input.containsKey(KEY_PROJECTION),
            () -> this.projection = new HashSet<String>(input.getJsonArray(KEY_PROJECTION).getList()));
        HFn.runAt(input.containsKey(KEY_CRITERIA),
            () -> this.criteria = Criteria.create(input.getJsonObject(KEY_CRITERIA)));
    }

    private void ensure(final JsonObject input) {
        // Sorter checking
        Ir.ensureType(input, KEY_SORTER, JsonArray.class, HUt::isJArray, this.getClass());
        // Projection checking
        Ir.ensureType(input, KEY_PROJECTION, JsonArray.class, HUt::isJArray, this.getClass());
        // Pager checking
        Ir.ensureType(input, KEY_PAGER, JsonObject.class, HUt::isJObject, this.getClass());
        // Criteria
        Ir.ensureType(input, KEY_CRITERIA, JsonObject.class, HUt::isJObject, this.getClass());
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
        this.criteria.save(field, value);
    }

    @Override
    public JsonObject toJson() {
        final JsonObject result = new JsonObject();
        HFn.runAt(() -> result.put(KEY_PAGER, this.pager.toJson()), this.pager);
        HFn.runAt(() -> {
            final JsonObject sorters = this.sorter.toJson();
            final JsonArray array = new JsonArray();
            HUt.itJObject(sorters, Boolean.class).forEach(entry -> {
                final Boolean value = entry.getValue();
                final String key = entry.getKey();
                array.add(key + "," + (value ? "ASC" : "DESC"));
            });
            //            HUt.<Boolean>itJObject(sorters, (value, key) -> array.add(key + "," + (value ? "ASC" : "DESC")));
            result.put(KEY_SORTER, array);
        }, this.sorter);
        HFn.runAt(() -> result.put(KEY_PROJECTION, HUt.toJArray(this.projection)), this.projection);
        HFn.runAt(() -> result.put(KEY_CRITERIA, this.criteria.toJson()), this.criteria);
        return result;
    }
}
