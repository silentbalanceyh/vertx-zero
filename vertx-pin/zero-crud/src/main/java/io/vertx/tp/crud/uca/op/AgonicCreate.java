package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.crud.uca.output.Post;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.uca.jooq.UxJooq;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AgonicCreate implements Agonic {
    @Override
    public Future<Envelop> runAsync(final JsonObject input, final IxIn in) {
        final KModule module = in.module();
        final UxJooq jooq = IxPin.jooq(in);
        return Pre.unique().inAsync(input, in)
                .compose(condition -> Ix.existing(condition, in.module()).apply(jooq)).compose(existing -> existing ?
                        // Unique Existing
                        Post.success201(input, module)
                        :
                        // Primary Key
                        Ix.passion(input, in,
                                        Pre.key(true)::inAsync,             // UUID Generated
                                        Pre.auditor(true)::inAsync,         // createdAt, createdBy
                                        Pre.auditor(false)::inAsync         // updatedAt, updatedBy
                                )
                                .compose(processed -> Ix.deserializeT(processed, module))
                                .compose(jooq::insertAsync)
                                .compose(entity -> Post.success200(entity, module))
                );
    }

    @Override
    public Future<Envelop> runBAsync(final JsonArray input, final IxIn module) {
        return null;
    }
}
