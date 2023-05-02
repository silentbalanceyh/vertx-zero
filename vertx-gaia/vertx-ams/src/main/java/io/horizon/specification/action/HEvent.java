package io.horizon.specification.action;

import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * 「异步指令」
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HEvent<I, R> extends HCommand<I, Future<R>> {

    // 合法性检查（异步）
    @SuppressWarnings("unchecked")
    default <T> T bind(final Vertx vertx) {
        return (T) this;
    }

    @Override
    default Future<R> configure(final I input) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    @Override
    default Future<R> synchro(final I input, final JsonObject request) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    @Override
    default Future<R> compile(final I input, final JsonObject request) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
