package io.vertx.tp.crud.connect;


import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.actor.IxActor;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.atom.connect.KJoin;
import io.vertx.tp.ke.atom.connect.KPoint;
import io.vertx.tp.ke.cv.em.JoinMode;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.Apeak;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Constants;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Values;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class ViewLinker implements IxLinker {
    @Override
    public Future<Envelop> joinAAsync(final Envelop request, final JsonArray columns, final KModule module) {
        /* Processing for Join Parameters */
        final JsonObject params = new JsonObject();
        params.put(KName.IDENTIFIER, Ux.getString1(request));

        final KJoin connect = module.getConnect();
        if (Objects.isNull(connect)) {
            return this.singleAsync(columns);
        } else {
            final KPoint target = connect.procTarget(params);
            if (Objects.isNull(target) || JoinMode.CRUD != target.modeTarget()) {
                return this.singleAsync(columns);
            } else {
                final KModule joinedModule = IxPin.getActor(target.getCrud());
                final UxJooq dao = IxPin.getDao(joinedModule, request.headers());
                return Ke.channel(Apeak.class, JsonArray::new, stub -> IxActor.start()
                                .compose(original -> IxActor.apeak().bind(request).procAsync(original, joinedModule))
                                .compose(stub.on(dao)::fetchFull)
                        )
                        .compose(linked -> this.joinAsync(columns, linked));
            }
        }
    }

    // Single Table
    private Future<Envelop> singleAsync(final JsonArray columns) {
        final JsonArray filtered = new JsonArray();
        final Set<String> fieldSet = new HashSet<>();
        columns.forEach(item -> this.addColumn(filtered, item, fieldSet));
        return Ux.future(Envelop.success(filtered));
    }

    // Join Table
    private Future<Envelop> joinAsync(final JsonArray columns, final JsonArray linked) {
        // Major Field
        final Set<String> majorSet = this.fieldName(columns);

        final JsonArray filtered = new JsonArray();
        // Duplicated Column Parsing
        final Set<String> fieldSet = new HashSet<>();
        for (int idx = Values.IDX; idx < columns.size(); idx++) {
            final Object value = columns.getValue(idx);
            if (Objects.isNull(value)) {
                // Continue for current loop
                continue;
            }
            if (Constants.DEFAULT_HOLDER.equals(value)) {
                // HOLDER Collection
                linked.forEach(item -> this.addColumn(filtered, item, majorSet));
            } else {
                // Add column here
                this.addColumn(filtered, value, fieldSet);
            }
        }
        return Ux.future(Envelop.success(filtered));
    }

    private String fieldName(final Object value) {
        final String field;
        if (value instanceof String) {
            // metadata
            field = value.toString().split(",")[0];
        } else {
            final JsonObject column = (JsonObject) value;
            if (column.containsKey(KName.METADATA)) {
                // metadata
                final String metadata = column.getString(KName.METADATA);
                if (Ut.notNil(metadata)) {
                    field = metadata.split(",")[0];
                } else {
                    field = null;
                }
            } else {
                // dataIndex
                field = column.getString("dataIndex");
            }
        }
        return field;
    }

    private Set<String> fieldName(final JsonArray columns) {
        final Set<String> fieldSet = new HashSet<>();
        columns.stream().map(this::fieldName).forEach(fieldSet::add);
        return fieldSet;
    }

    private void addColumn(final JsonArray columns, final Object value, final Set<String> fieldSet) {
        final String field = this.fieldName(value);
        if (Objects.nonNull(field)) {
            if (!fieldSet.contains(field)) {
                columns.add(value);
                fieldSet.add(field);
            }
        }
    }
}
