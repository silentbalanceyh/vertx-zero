package io.vertx.up.commune.record;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.json.JsonObject;

/*
 * Package scope
 */
interface AptOp<T> {
    /* Old */
    T dataO();

    /* New */
    T dataN();

    /* Replace, Save */
    T dataS();

    /* Append */
    T dataA();

    /* Current data ( Maybe update ), Capture default value */
    T dataI();

    /* Return current type of Change */
    ChangeFlag type();

    T set(T dataArray);

    /* Update data based on `current`. */
    AptOp<T> update(JsonObject data);
}
