package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.em.QrType;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxKit;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.trans.Tran;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.up.uca.jooq.UxJooq;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicCreate implements Agonic {
    @Override
    public Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        final KModule module = in.module();
        final UxJooq jooq = IxPin.jooq(in);
        return Pre.qr(QrType.BY_UK).inJAsync(input, in)
            /*
             * Here must use jooq directly instead of join because
             * The creation step split to
             * 1) Major table inserted
             * 2) Secondary table inserted
             */
            .compose(condition -> jooq.countAsync(condition).compose(counter -> 0 < counter ?
                // Unique Existing
                IxKit.success201Pre(input, module)
                :
                // Primary Key
                Ix.passion(input, in,
                        Pre.key(true)::inJAsync,             // UUID Generated
                        Pre.serial()::inJAsync,              // Serial/Number
                        Pre.audit(true)::inJAsync,         // createdAt, createdBy
                        Pre.audit(false)::inJAsync,        // updatedAt, updatedBy
                        Pre.fileIn(true)::inJAsync           // File: Attachment creating
                    )
                    .compose(processed -> Ix.deserializeT(processed, module))
                    .compose(jooq::insertAsync)
                    .compose(entity -> IxKit.successJ(entity, module))
            ));
    }

    @Override
    public Future<JsonArray> runAAsync(final JsonArray input, final IxMod in) {
        final KModule module = in.module();
        final UxJooq jooq = IxPin.jooq(in);
        return Ix.passion(input, in,
                Pre.key(true)::inAAsync,             // UUID Generated
                Tran.tree(true)::inAAsync,            // After GUID
                Pre.serial()::inAAsync,              // Serial/Number
                Pre.audit(true)::inAAsync,         // createdAt, createdBy
                Pre.audit(false)::inAAsync         // updatedAt, updatedBy
            )
            .compose(processed -> Ix.deserializeT(processed, module))
            .compose(jooq::insertAsync)
            .compose(inserted -> IxKit.successA(inserted, module));
    }
}
