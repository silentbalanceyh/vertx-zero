package io.vertx.aeon.specification.element;

import io.vertx.core.Future;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HEvent<R> extends HCommand<Future<R>> {
    // 合法性检查（异步）
    @Override
    Future<R> configure();
}
