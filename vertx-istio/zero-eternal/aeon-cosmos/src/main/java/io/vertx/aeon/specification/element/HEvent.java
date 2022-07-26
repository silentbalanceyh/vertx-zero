package io.vertx.aeon.specification.element;

import io.vertx.core.Future;
import io.vertx.up.exception.web._501NotSupportException;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HEvent<I, R> extends HCommand<I, Future<R>> {
    // 合法性检查（异步）

    @Override
    default Future<R> configure(final I input) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
