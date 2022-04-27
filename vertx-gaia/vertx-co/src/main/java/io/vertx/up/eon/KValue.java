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
        /* Default Position */
        String POSITION_DEFAULT = "DEFAULT";
    }

    interface Regex {
        // isPositive
        String POSITIVE = "^\\+{0,1}[0-9]\\d*";
        // isNegative
        String NEGATIVE = "^-[0-9]\\d*";
        // isInteger
        String INTEGER = "[+-]{0,1}0";
        // isDecimal
        String DECIMAL = "[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+";
        // isDecimalPositive
        String DECIMAL_POSITIVE = "\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*";
        // isDecimalNegative
        String DECIMAL_NEGATIVE = "^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*";

        // isFileName
        String FILENAME = "[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$";
    }

    interface NS {
        // Modular Namespace of Default
        String DEFAULT = "cn.originx.{0}";
    }
}
