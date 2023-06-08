package io.vertx.up.eon.em;

/**
 * @author lang : 2023/4/27
 */
public enum ErrorZero {

    _15000(-15000,
        "The arguments could not be both null to build `Apt` component")                                    // AptParameterException
    , _15001(-15001,
        "The input `{}` could not be parsed to valid date")                                                 // DateFormatException
    , _15002(-15002,
        "The expression \"{0}\" could not be parsed, details = {1}")                                        // JexlExpressionException
    , _15003(-15003,
        "Lime node configured key = \"{0}\" is missing in yml file")                                        // LimeMissingException
    ;
    private final String message;
    private final int code;

    ErrorZero(final int code, final String message) {
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
