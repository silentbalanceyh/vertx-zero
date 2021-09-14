package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.output.Post;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicByID implements Agonic {
    @Override
    public Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        final UxJooq jooq = IxPin.jooq(in);
        return jooq.fetchOneAsync(input).compose(entity -> {
            if (Objects.isNull(entity)) {
                return Post.success204Pre();
            } else {
                final KModule module = in.module();
                final JsonObject json = Ux.toJson(entity, module.getPojo());
                if (in.canJoin()) {
                    return Ix.<JsonObject>seekFn(in, json)
                        .apply(JsonObject::new, UxJooq::fetchJOneAsync)
                        .compose(category -> Ux.future(category.copy().mergeIn(json)));
                } else {
                    return Ux.future(json);
                }
            }
        });
    }
}