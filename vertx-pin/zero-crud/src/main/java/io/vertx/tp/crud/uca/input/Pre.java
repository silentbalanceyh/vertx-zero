package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Pre {
    static Pre head() {
        return Fn.poolThread(Pooled.PRE_MAP, HeadPre::new, HeadPre.class.getName());
    }

    static Pre apeak() {
        return Fn.poolThread(Pooled.PRE_MAP, ApeakPre::new, ApeakPre.class.getName());
    }

    Future<JsonObject> inAsync(JsonObject data, IxIn in);
}
