package io.vertx.mod.crud.uca.op;

import io.aeon.experiment.specification.KModule;
import io.horizon.spi.feature.Trash;
import io.horizon.uca.aop.Aspect;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.init.IxPin;
import io.vertx.mod.crud.refine.Ix;
import io.vertx.mod.crud.uca.desk.IxKit;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.mod.crud.uca.input.Pre;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicDelete implements Agonic {
    @Override
    public Future<JsonObject> runJAsync(final JsonObject criteria, final IxMod in) {
        final UxJooq jooq = IxPin.jooq(in);
        return jooq.fetchOneAsync(criteria).compose(entity -> {
            if (Objects.isNull(entity)) {
                /* Could not find the original */
                return IxKit.success204Pre(Boolean.TRUE);
            } else {
                final KModule module = in.module();
                final JsonObject json = Ix.serializeJ(entity, module);

                /* File: Remove Attachment Part */
                return Pre.fileOut().inJAsync(json, in)
                    /* BackUp future */
                    .compose(removed -> Ux.channelA(Trash.class, () -> Ux.future(removed),
                        (stub) -> stub.backupAsync(module.identifier(), removed)))


                    // 「AOP」Wrap JsonObject delete
                    .compose(Ix.wrap(module, Aspect::wrapJDelete, wrapData -> Ux.future(wrapData)
                        /* 200, IxLinker deleted first and then delete related record */
                        .compose(processed -> Ix.<Boolean>seekFn(in, processed)
                            .apply(() -> Boolean.FALSE, UxJooq::deleteByAsync))
                        /* 200, Current Item */
                        .compose(nil -> jooq.deleteByAsync(criteria))
                        .compose(IxKit::success200Pre)
                    ));
            }
        });
    }

    @Override
    public Future<JsonArray> runJAAsync(final JsonObject criteria, final IxMod in) {
        final UxJooq jooq = IxPin.jooq(in);
        return jooq.fetchAsync(criteria).compose(queried -> {
            if (Objects.isNull(queried) || queried.isEmpty()) {
                return Ux.futureA();
            } else {
                final KModule module = in.module();
                final JsonArray array = Ix.serializeA(queried, module);

                /* File: Remove Attachment Part */
                return Pre.fileOut().inAAsync(array, in)
                    /* BackUp future */
                    .compose(removed -> Ux.channelA(Trash.class, () -> Ux.future(array),
                        stub -> stub.backupAsync(module.identifier(), array)))


                    // 「AOP」Wrap JsonArray delete
                    .compose(Ix.wrap(module, Aspect::wrapADelete, wrapData -> Ux.future(wrapData)
                        /* 200, IxLinker deleted first and then delete related records */
                        .compose(processed -> Ix.<Boolean>seekFn(in, processed)
                            .apply(() -> Boolean.FALSE, UxJooq::deleteByAsync))
                        /* 200, Current Item */
                        .compose(nil -> jooq.deleteByAsync(criteria))
                        .compose(nil -> Ux.future(array))
                    ));
            }
        });
    }
}
