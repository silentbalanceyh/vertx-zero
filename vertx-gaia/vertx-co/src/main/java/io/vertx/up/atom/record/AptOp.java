package io.vertx.up.atom.record;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;

/*
 * Package scope
 */
interface AptOp<T> {
    /* Old */
    T dataO();

    /* New */
    T dataN();

    /* Replace */
    T dataR();

    /* Append */
    T dataA();

    /* Current data ( Maybe update ) */
    T dataDft();

    /* Return current type of Change */
    ChangeFlag type();

    T set(T dataArray);

    /* Update data based on `current`. */
    AptOp<T> update(JsonObject data);
}
