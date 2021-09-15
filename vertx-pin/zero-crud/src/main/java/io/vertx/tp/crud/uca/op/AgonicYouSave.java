package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxKit;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.util.Ut;

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
        final JsonObject params = this.module.dataCond(input);
        final KModule module = in.module();
        final UxJooq jooq = IxPin.jooq(in);
        return this.runUnique(params, in,
            this::fetchBy
        ).compose(json -> {
            // Avoid overwrite primary key here
            final JsonObject inputJson = input.copy();
            inputJson.remove(module.getField().getKey());
            if (Ut.isNil(json)) {
                // Not Found ( Insert )
                return Ix.passion(inputJson, in,
                        Pre.key(true)::inJAsync,             // UUID Generated
                        Pre.serial()::inJAsync,              // Serial/Number
                        Pre.auditor(true)::inJAsync,         // createdAt, createdBy
                        Pre.auditor(false)::inJAsync         // updatedAt, updatedBy
                    )
                    .compose(processed -> Ix.deserializeT(processed, module))
                    .compose(jooq::insertAsync)
                    .compose(updated -> IxKit.successJ(updated, module));
            } else {
                // Found ( Update )
                final JsonObject merged = json.copy().mergeIn(inputJson, true);
                return Ix.passion(merged, in,
                        Pre.auditor(false)::inJAsync         // updatedAt, updatedBy
                    )
                    .compose(processed -> Ix.deserializeT(processed, module))
                    .compose(jooq::updateAsync)
                    .compose(updated -> IxKit.successJ(updated, module));
            }
        });
    }
}
