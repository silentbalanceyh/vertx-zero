package io.vertx.up.eon;

public interface KResult {

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
