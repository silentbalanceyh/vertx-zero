package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxKit;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.up.experiment.specification.KModule;
import io.vertx.up.experiment.specification.KPoint;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.uca.sectio.Aspect;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AgonicYouSave extends AgonicUnique {
    private final transient IxMod module;

    AgonicYouSave(final IxMod module) {
        this.module = module;
    }

    @Override
    public Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        final JsonObject condition = this.module.dataCond(input);
        final KModule standBy = in.module();
        final UxJooq jooq = IxPin.jooq(in);
        return this.runUnique(condition, in,
            this::fetchBy
        ).compose(json -> {
            // Avoid overwrite primary key here
            final JsonObject inputJson = input.copy();
            inputJson.remove(standBy.getField().getKey());
            if (Ut.isNil(json)) {
                // Not Found ( Insert )
                return Ix.passion(inputJson, in,
                        Pre.key(true)::inJAsync,                // UUID Generated
                        Pre.serial()::inJAsync,                 // Serial/Number
                        Pre.audit(true)::inJAsync,              // createdAt, createdBy
                        Pre.audit(false)::inJAsync              // updatedAt, updatedBy
                    )


                    // 「AOP」Wrap JsonObject create
                    .compose(Ix.wrap(standBy, Aspect::wrapJCreate, wrapData -> Ux.future(wrapData)
                        .compose(processed -> Ix.deserializeT(processed, standBy))
                        .compose(jooq::insertAsync)
                        .compose(updated -> IxKit.successJ(updated, standBy))
                    ));
            } else {
                // Found ( Update )
                final JsonObject merged = json.copy().mergeIn(inputJson, true);
                return Ix.passion(merged, in,
                        Pre.audit(false)::inJAsync         // updatedAt, updatedBy
                    )

                    // 「AOP」Wrap JsonArray update
                    .compose(Ix.wrap(standBy, Aspect::wrapJUpdate, wrapData -> Ux.future(wrapData)
                        .compose(processed -> Ix.deserializeT(processed, standBy))
                        .compose(jooq::updateAsync)
                        .compose(updated -> IxKit.successJ(updated, standBy))
                    ));
            }
        });
    }

    /*
     * Fix:
     * https://github.com/silentbalanceyh/hotel/issues/323
     * https://github.com/silentbalanceyh/hotel/issues/324
     */
    @Override
    public Future<JsonArray> runAAsync(final JsonArray input, final IxMod in) {
        final JsonObject condition = this.module.dataCond(input);
        Ix.Log.filters(this.getClass(), "( Batch ) By Joined: identifier: {0}, condition: {1}", in.module().identifier(), condition);
        final KModule standBy = in.module();
        final UxJooq jooq = IxPin.jooq(in);
        return jooq.fetchJAsync(condition).compose(queried -> {

            // KPoint to extract joinKey here
            final KPoint point = this.module.pointJoin();
            if (Objects.isNull(point)) {
                return Ux.future(input);
            }
            final String joinedKey = point.getKeyJoin();
            final JsonArray combined = Ux.updateJ(queried, input, joinedKey);

            // Update Combine Json Data
            return Ix.passion(combined, in,
                    Pre.audit(false)::inAAsync                  // updatedAt, updatedBy
                )


                // 「AOP」Wrap JsonArray update
                .compose(Ix.wrap(standBy, Aspect::wrapAUpdate, wrapData -> Ux.future(wrapData)
                    .compose(processed -> Ix.deserializeT(processed, standBy))
                    .compose(jooq::updateAsync)
                    .compose(updated -> IxKit.successA(updated, standBy))
                ));
        });
    }
}
