package io.vertx.mod.crud.uca.op;

import io.aeon.experiment.specification.KModule;
import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.uca.aop.Aspect;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.init.IxPin;
import io.vertx.mod.crud.refine.Ix;
import io.vertx.mod.crud.uca.desk.IxKit;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.mod.crud.uca.input.Pre;
import io.vertx.up.atom.shape.KField;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import static io.vertx.mod.crud.refine.Ix.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicUpdate extends AgonicUnique {

    @Override
    public Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        final KModule module = in.module();
        final UxJooq jooq = IxPin.jooq(in);
        /*
         * Here the queryJ is the record that has been stored
         * in database, this api should re-write and merge the input
         * data into `queryJ`
         * 1. If the `field` exist, the field will be overwritten
         * 2. If the `field` does not exist, the field will be ignored
         * */
        return this.runUnique(input, in,
            this::fetchByPk,
            this::fetchByUk
        ).compose(json -> {
            if (Ut.isNil(json)) {
                // Not Found
                return IxKit.success204Pre();
            } else {
                // Do Update
                final JsonObject merged = json.copy().mergeIn(input, true);
                return Ix.passion(merged, in,
                        Pre.audit(false)::inJAsync,                 // updatedAt, updatedBy
                        Pre.fileIn(false)::inJAsync                 // File: Attachment creating
                    )


                    // 「AOP」Wrap JsonObject update
                    .compose(Ix.wrap(module, Aspect::wrapJUpdate, wrapData -> Ux.future(wrapData)
                        .compose(processed -> Ix.deserializeT(processed, module))
                        .compose(jooq::updateAsync)
                        .compose(updated -> IxKit.successJ(updated, module))
                    ));
            }
        });
    }

    @Override
    public Future<JsonArray> runJAAsync(final JsonObject input, final IxMod in) {
        final JsonObject query = input.getJsonObject(Ir.KEY_CRITERIA);
        LOG.Filter.info(this.getClass(), "( Mass Update ) Condition: {0}", query);
        final UxJooq jooq = IxPin.jooq(in);
        return jooq.fetchJAsync(query)
            .compose(original -> {
                final KField fieldConfig = in.module().getField();
                final JsonArray matrix = Ix.onMatrix(fieldConfig);
                return Ux.compareJAsync(original, input.getJsonArray(KName.DATA), matrix);
            })
            .compose(compared -> {
                final JsonArray updateQueue = compared.get(ChangeFlag.UPDATE);
                return this.runAAsync(updateQueue, in);
            });
    }

    @Override
    public Future<JsonArray> runAAsync(final JsonArray input, final IxMod in) {
        final KModule module = in.module();
        final UxJooq jooq = IxPin.jooq(in);
        return Ix.passion(input, in,
                Pre.audit(false)::inAAsync                      // updatedAt, updatedBy
            )


            // 「AOP」Wrap JsonArray update
            .compose(Ix.wrap(module, Aspect::wrapAUpdate, wrapData -> Ux.future(wrapData)
                .compose(processed -> Ix.deserializeT(processed, module))
                .compose(jooq::updateAsync)
                .compose(updated -> IxKit.successA(updated, module))
            ));
    }
}
