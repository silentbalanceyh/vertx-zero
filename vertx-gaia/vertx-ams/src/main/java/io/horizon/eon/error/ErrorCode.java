package io.horizon.eon.error;

/**
 * @author lang : 2023/4/27
 */
public enum ErrorCode {
    _11000(-11000,
        "Cannot find META-INF/services/ {} on classpath")                                  // SPINullException
    , _11001(-11001,
        "( Depend ) ArgumentWrongException")                                               // ArgumentWrongException
    , _11002(-11002,
        "The `filename` of io stream is empty, filename = `{}`")                           // EmptyIoException
    , _11003(-11003,
        "The error of `{}` has not been defined and could not be found")                   // ErrorMissingException
    , _11004(-11004,
        "The system met json decoding/encoding exception: {}")                             // JsonFormatException
    , _11005(-11005,
        "This operation is not supported! ( method = {}, class = {} )")                    // OperationException
    , _11006(-11006,
        "This method is forbidden and not supported when you called")                      // NotImplementException
    , _11007(-11007,
        "Input `pool` argument is null, may cause NullPointerException / Terminal")        // PoolNullException
    , _11008(-11008,
        "The input key of `Pool` is null, it''s conflict in current environment")          // PoolKeyNullException
    ;
    private final String message;
    private final int code;

    ErrorCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public String M() {
        return this.message;
    }

    public int V() {
        return this.code;
    }
}
