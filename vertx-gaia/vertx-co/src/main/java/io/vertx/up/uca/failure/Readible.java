package io.vertx.up.uca.failure;

import io.vertx.up.exception.WebException;
import io.vertx.up.util.Ut;

/**
 * Fill the field "readible" for input exception
 */
@SuppressWarnings("all")
public interface Readible {
    /**
     * Get code readible
     *
     * @return
     */
    static Readible get() {
        return Ut.singleton(CodeReadible.class);
    }

    /**
     * Fill the field "readible" for web exception
     *
     * @param error
     * @return
     */
    void interpret(WebException error);
}
