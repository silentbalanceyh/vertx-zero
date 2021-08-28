package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class QUkPre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxIn in) {
        /* Unique Keys */
        final JsonArray unique = in.module().getField().getUnique();

        /* Each Unique */
        final JsonObject filters = this.condition(data, unique);
        Ix.Log.filters(this.getClass(), "{0}", filters.encode());
        return Ux.future(filters);
    }

    private JsonObject condition(final JsonObject data, final JsonArray unique) {
        final JsonObject filters = new JsonObject();
        if (Values.ONE == unique.size()) {
            final JsonArray fields = unique.getJsonArray(Values.IDX);
            final Set<String> fieldSet = Ut.toSet(fields);
            filters.mergeIn(this.condition(data, fieldSet));
        } else {
            filters.put(Strings.EMPTY, Boolean.FALSE);
            Ut.itJArray(unique, JsonArray.class,
                (each, index) -> filters.put("$" + index, this.condition(data, Ut.toSet(each))));
        }
        return filters;
    }

    private JsonObject condition(final JsonObject data, final Set<String> fieldSet) {
        final JsonObject filters = new JsonObject();
        filters.put(Strings.EMPTY, Boolean.TRUE);
        fieldSet.forEach(field -> {
            final Object value = data.getValue(field);
            filters.put(field, value);
        });
        return filters;
    }

    @Override
    public Future<JsonObject> inAJAsync(final JsonArray array, final IxIn in) {
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
