package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxField;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.util.Ut;

class UniqueActor extends AbstractActor {

    @Override
    public JsonObject proc(final JsonObject data, final IxModule config) {
        /* Unique Keys */
        final IxField field = config.getField();
        final JsonArray unique = field.getUnique();
        /* Each Unique */
        final JsonObject filters = new JsonObject();
        if (Values.ONE == unique.size()) {
            final JsonArray fields = unique.getJsonArray(Values.IDX);
            filters.mergeIn(this.getFilters(fields, data));
        } else {
            filters.put(Strings.EMPTY, Boolean.FALSE);
            Ut.itJArray(unique, JsonArray.class,
                    (each, index) -> filters.put("$" + index, this.getFilters(each, data)));
        }
        Ix.infoFilters(this.getLogger(), "\n{0}", filters.encodePrettily());
        return filters;
    }

    private JsonObject getFilters(final JsonArray fields, final JsonObject data) {
        final JsonObject filters = new JsonObject();
        filters.put(Strings.EMPTY, Boolean.TRUE);
        Ut.itJArray(fields, String.class, (field, index) -> {
            final Object value = data.getValue(field);
            filters.put(field, value);
        });
        return filters;
    }
}
