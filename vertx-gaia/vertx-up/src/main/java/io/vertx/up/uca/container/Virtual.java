package io.vertx.up.uca.container;

public class Virtual {

    private static Virtual INSTANCE = null;

    private Virtual() {
    }

    public static Virtual create() {
        if (null == INSTANCE) {
            synchronized (Virtual.class) {
                if (null == INSTANCE) {
                    INSTANCE = new Virtual();
                }
            }
        }
        return INSTANCE;
    }

    public static boolean is(final Object input) {
        boolean virtual = false;
        if (null != input && Virtual.class == input.getClass()) {
            virtual = true;
        }
        return virtual;
    }
}
