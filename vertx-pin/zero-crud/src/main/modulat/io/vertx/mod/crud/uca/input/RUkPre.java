package io.vertx.mod.crud.uca.input;

import io.horizon.eon.VString;
import io.horizon.eon.VValue;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Set;

import static io.vertx.mod.crud.refine.Ix.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RUkPre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        /* Unique Keys */
        final JsonArray unique = in.module().getField().getUnique();

        /* Each Unique */
        final JsonObject filters = this.condition(data, unique);
        LOG.Filter.info(this.getClass(), "{0}", filters.encode());
        return Ux.future(filters);
    }

    private JsonObject condition(final JsonObject data, final JsonArray unique) {
        final JsonObject filters = new JsonObject();
        if (VValue.ONE == unique.size()) {
            final JsonArray fields = unique.getJsonArray(VValue.IDX);
            final Set<String> fieldSet = Ut.toSet(fields);
            filters.mergeIn(this.condition(data, fieldSet));
        } else {
            filters.put(VString.EMPTY, Boolean.FALSE);
            Ut.itJArray(unique, JsonArray.class,
                (each, index) -> filters.put("$" + index, this.condition(data, Ut.toSet(each))));
        }
        return filters;
    }

    private JsonObject condition(final JsonObject data, final Set<String> fieldSet) {
        final JsonObject filters = new JsonObject();
        filters.put(VString.EMPTY, Boolean.TRUE);
        fieldSet.forEach(field -> {
            final Object value = data.getValue(field);
            filters.put(field, value);
        });
        return filters;
    }

    @Override
    public Future<JsonObject> inAJAsync(final JsonArray array, final IxMod in) {
        /* Unique Keys */
        final JsonArray unique = in.module().getField().getUnique();
        final JsonObject filters = new JsonObject();
        Ut.itJArray(array, JsonObject.class, (data, index) -> {
            /* Each condition */
            final JsonObject cond = this.condition(data, unique);
            filters.put("$" + index, cond);
        });
        return Ux.future(filters);
    }
}
