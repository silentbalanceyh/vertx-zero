package io.aeon.experiment.brain;

interface Tool {

    static boolean isInteger(final Class<?> type) {
        return Long.class == type || long.class == type ||
            Short.class == type || short.class == type ||
            Integer.class == type || int.class == type;
    }

    static boolean isDecimal(final Class<?> type) {
        return Double.class == type || double.class == type ||
            Float.class == type || float.class == type;
    }
}
