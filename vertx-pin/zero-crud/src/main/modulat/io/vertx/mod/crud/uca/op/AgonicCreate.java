package io.vertx.mod.crud.uca.op;

import io.aeon.experiment.specification.KModule;
import io.horizon.uca.aop.Aspect;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.em.QrType;
import io.vertx.mod.crud.init.IxPin;
import io.vertx.mod.crud.refine.Ix;
import io.vertx.mod.crud.uca.desk.IxKit;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.mod.crud.uca.input.Pre;
import io.vertx.mod.crud.uca.trans.Tran;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicCreate implements Agonic {
    @Override
    public Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        final KModule module = in.module();
        final UxJooq jooq = IxPin.jooq(in);
        return Pre.qr(QrType.BY_UK).inJAsync(input, in).compose(condition ->
            /*
             * Here must use jooq directly instead of join because
             * The creation step split to
             * 1) Major table inserted
             * 2) Secondary table inserted
             */
            jooq.countAsync(condition).compose(counter -> 0 < counter ?
                    // Unique Existing
                    IxKit.success201Pre(input, module)
                    // Primary Key ( Not Existing )
                    : Ix.passion(input, in,
                        Pre.key(true)::inJAsync,                // UUID Generated
                        Pre.serial()::inJAsync,                         // Serial/Number
                        Pre.audit(true)::inJAsync,              // createdAt, createdBy
                        Pre.audit(false)::inJAsync,             // updatedAt, updatedBy
                        Pre.fileIn(true)::inJAsync              // File: Attachment creating
                    )


                    // 「AOP」Wrap JsonObject create
                    .compose(Ix.wrap(module, Aspect::wrapJCreate, wrapData -> Ux.future(wrapData)
                        .compose(processed -> Ix.deserializeT(processed, module))
                        .compose(jooq::insertAsync)
                        .compose(entity -> IxKit.successJ(entity, module))
                    ))
            ));
    }

    @Override
    public Future<JsonArray> runAAsync(final JsonArray input, final IxMod in) {
        final KModule module = in.module();
        final UxJooq jooq = IxPin.jooq(in);
        return Ix.passion(input, in,
                Pre.key(true)::inAAsync,                        // UUID Generated
                Tran.tree(true)::inAAsync,                      // After GUID
                Pre.serial()::inAAsync,                         // Serial/Number
                Pre.audit(true)::inAAsync,                      // createdAt, createdBy
                Pre.audit(false)::inAAsync                      // updatedAt, updatedBy
            )


            // 「AOP」Wrap JsonArray create
            .compose(Ix.wrap(module, Aspect::wrapACreate, wrapData -> Ux.future(wrapData)
                .compose(processed -> Ix.deserializeT(processed, module))
                .compose(jooq::insertAsync)
                .compose(inserted -> IxKit.successA(inserted, module))
            ));
    }
}
