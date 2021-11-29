package io.vertx.up.uca.jooq;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JoinWriter {
    private final transient JoinStore store;

    JoinWriter(final JoinStore store) {
        this.store = store;
    }
}
