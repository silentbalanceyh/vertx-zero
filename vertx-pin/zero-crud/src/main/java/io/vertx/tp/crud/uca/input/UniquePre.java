package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.KField;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class UniquePre implements Pre {
    @Override
    public Future<JsonObject> inAsync(final JsonObject data, final IxIn in) {
        /* Unique Keys */
        final KModule module = in.module();
        final KField field = module.getField();
        final JsonArray unique = field.getUnique();
        /* Each Unique */
        final JsonObject filters = new JsonObject();
        if (Values.ONE == unique.size()) {
            final JsonArray fields = unique.getJsonArray(Values.IDX);
            filters.mergeIn(this.condition(fields, data));
        } else {
            filters.put(Strings.EMPTY, Boolean.FALSE);
            Ut.itJArray(unique, JsonArray.class,
                    (each, index) -> filters.put("$" + index, this.condition(each, data)));
        }
        Ix.Log.filters(this.getClass(), "\n{0}", filters.encodePrettily());
        return Ux.future(filters);
    }

    private JsonObject condition(final JsonArray fields, final JsonObject data) {
        final JsonObject filters = new JsonObject();
        filters.put(Strings.EMPTY, Boolean.TRUE);
        Ut.itJArray(fields, String.class, (field, index) -> {
            final Object value = data.getValue(field);
            filters.put(field, value);
        });
        return filters;
    }
}
