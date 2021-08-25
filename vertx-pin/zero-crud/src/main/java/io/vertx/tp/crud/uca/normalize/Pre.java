package io.vertx.tp.crud.uca.normalize;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Pre {
    static Pre head() {
        return Fn.poolThread(Pool.PRE_MAP, HeadPre::new, HeadPre.class.getName());
    }

    static Pre Apeak() {
        return Fn.poolThread(Pool.PRE_MAP, ApeakPre::new, ApeakPre.class.getName());
    }

    Future<JsonObject> setUp(JsonObject data, IxIn in);
}

interface Pool {

    ConcurrentMap<String, Pre> PRE_MAP =
            new ConcurrentHashMap<>();
}
