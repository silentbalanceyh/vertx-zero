package io.vertx.tp.ke.cv;

import io.vertx.up.eon.Values;

public interface KeResult {

    int RC_SUCCESS = Values.ZERO;

    int RC_FAILURE = Values.RANGE;

    /*
     * Bool Result
     */
    enum Bool {
        SUCCESS,
        FAILURE
    }
}
