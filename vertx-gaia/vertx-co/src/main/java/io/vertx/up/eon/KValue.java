package io.vertx.up.eon;

public interface KValue {

    int RC_SUCCESS = Values.ZERO;

    int RC_FAILURE = Values.RANGE;

    /*
     * Bool Result
     */
    enum Bool {
        SUCCESS,
        FAILURE
    }

    interface View {
        /* Default View */
        String VIEW_DEFAULT = "DEFAULT";
        /* Default View Value:  Position + View */
        String VIEW_DEFAULT_VALUE = "[\"DEFAULT\",\"DEFAULT\"]";
        /* Default Position */
        String POSITION_DEFAULT = "DEFAULT";
    }
}
