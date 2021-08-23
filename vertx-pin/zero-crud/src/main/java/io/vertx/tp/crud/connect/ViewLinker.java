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
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.Objects;

class ViewLinker implements IxLinker {
    @Override
    public Future<Envelop> joinAAsync(final Envelop request, final JsonArray columns, final KModule module) {
        /* Processing for Join Parameters */
        final JsonObject params = new JsonObject();
        params.put(KName.IDENTIFIER, Ux.getString1(request));

        final KJoin connect = module.getConnect();
        final Envelop response = Envelop.success(columns);
        if (Objects.isNull(connect)) {
            return Ux.future(response);
        } else {
            final KPoint target = connect.procTarget(params);
            if (Objects.isNull(target) || JoinMode.CRUD != target.modeTarget()) {
                return Ux.future(response);
            } else {
                final KModule joinedModule = IxPin.getActor(target.getCrud());
                final UxJooq dao = IxPin.getDao(joinedModule, request.headers());
                return Ke.channel(Apeak.class, JsonArray::new, stub -> IxActor.start()
                                .compose(original -> IxActor.apeak().bind(request).procAsync(original, joinedModule))
                                .compose(stub.on(dao)::fetchFull)
                        )
                        .compose(columnsLinked -> Ux.future(columns.copy().addAll(columnsLinked)))
                        .compose(combine -> Ux.future(Envelop.success(combine)));
            }
        }
    }
}
