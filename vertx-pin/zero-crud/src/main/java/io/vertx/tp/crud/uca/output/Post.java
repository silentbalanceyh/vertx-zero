package io.vertx.tp.crud.uca.output;

import io.vertx.core.Future;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface Post<T> {

    static Post apeak() {
        return Fn.poolThread(Pooled.POST_MAP, ApeakPost::new, ApeakPost.class.getName());
    }

    static Future<Envelop> success201(final JsonObject input, final KModule module) {
        return Ux.future(Envelop.success(Ix.serializeJ(input, module), HttpStatusCode.CREATED));
    }

    static <T> Future<Envelop> success200(final T input, final KModule module) {
        final JsonObject serializedJson = Ux.toJson(input, module.getPojo());
        Ix.serializeJ(serializedJson, module);
        return Ux.future(Envelop.success(serializedJson));
    }

    Future<T> outAsync(T active, T standBy);
}
