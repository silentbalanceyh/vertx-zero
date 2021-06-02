package io.vertx.up.atom.record;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;

import java.util.concurrent.ConcurrentMap;

/*
 * Package scope
 */
interface AptOp<T> {
    /* Original data here */
    T dataO();

    /* Current data here */
    T dataN();

    /* Current data ( Maybe update ) */
    T data();

    /* Return current type of Change */
    ChangeFlag type();

    T set(T dataArray);

    /* Update data based on `current`. */
    AptOp<T> update(JsonObject data);

    /* Get compared data here */
    ConcurrentMap<ChangeFlag, T> compared();
}
