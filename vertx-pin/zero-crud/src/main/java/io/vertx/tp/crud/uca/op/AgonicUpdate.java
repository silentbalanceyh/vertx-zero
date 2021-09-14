package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.output.Post;
import io.vertx.tp.ke.atom.KField;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicUpdate implements Agonic {
    @Override
    public Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        final KModule module = in.module();
        Ix.Log.filters(this.getClass(), "( Update ) Identifier: {1}, Condition: {0}",
            module.getIdentifier(), input);
        final UxJooq jooq = IxPin.jooq(in);
        // Query by `key` first
        return Pre.qPk().inJAsync(input, in)
            .compose(jooq::fetchJOneAsync)
            .compose(queryJ -> Ut.isNil(queryJ) ?
                // Query by `unique key` then
                Pre.qUk().inJAsync(input, in)
                    .compose(jooq::fetchJOneAsync)
                    .compose(querySubJ -> Ut.isNil(querySubJ) ?
                        Post.success404Pre()
                        : Ux.future(querySubJ)
                    )
                : Ux.future(queryJ)
            )
            /*
             * Here the queryJ is the record that has been stored
             * in database, this api should re-write and merge the input
             * data into `queryJ`
             * 1. If the `field` exist, the field will be overwritten
             * 2. If the `field` does not exist, the field will be ignored
             * */
            .compose(queryJ -> Ux.future(queryJ.copy().mergeIn(input)))
            .compose(json -> Ix.passion(json, in,
                        Pre.auditor(false)::inJAsync         // updatedAt, updatedBy
                    )
                    .compose(processed -> Ix.deserializeT(processed, module))
                    .compose(jooq::updateAsync)
                    .compose(updated -> Post.successJ(updated, module))
            );
    }

    @Override
    public Future<JsonArray> runJAAsync(final JsonObject input, final IxMod in) {
        final JsonObject query = input.getJsonObject(Qr.KEY_CRITERIA);
        Ix.Log.filters(this.getClass(), "( Mass Update ) Condition: {0}", query);
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
                Pre.auditor(false)::inAAsync         // updatedAt, updatedBy
            )
            .compose(processed -> Ix.deserializeT(processed, module))
            .compose(jooq::updateAsync)
            .compose(updated -> Post.successA(updated, module));
    }
}
