package io.vertx.up.atom;

import io.vertx.core.Future;
import io.vertx.up.fn.Fn;

/*
 * [Data Structure]
 * Single Rxjava container for null pointer that is not used in
 * Rxjava2.
 * For usage such as:
 *
 * compose(Refer::future)  // stored
 * compose(xxx)
 * compose(x -> Refere.get()) // pick up
 *
 * When some steps skipped, this object is usage for reference stored
 */
@SuppressWarnings("all")
public final class Refer {

    private Object reference;

    public <T> Refer add(final T reference) {
        this.reference = reference;
        return this;
    }

    /*
     * For vert.x compose method only.
     */
    public <T> Future<T> future(final T reference) {
        this.reference = reference;
        return Future.succeededFuture(reference);
    }

    public <T> Future<T> future() {
        return Future.succeededFuture((T) this.reference);
    }

    public boolean successed() {
        return null != this.reference;
    }

    @SuppressWarnings("unchecked")
    public <T> T get() {
        return Fn.getNull(() -> (T) this.reference, this.reference);
    }
}
