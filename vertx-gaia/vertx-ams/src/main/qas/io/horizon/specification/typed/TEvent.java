package io.horizon.specification.typed;

import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * 「异步指令」
 * <hr/>
 * 系统指令的异步版本，该指令主要用于异步执行，异步执行的指令会返回 {@link Future} 类型的结果。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface TEvent<I, R> extends TCommand<I, Future<R>> {

    /**
     * 绑定 Vertx 实例
     *
     * @param vertx {@link Vertx} 实例
     * @param <T>   泛型
     *
     * @return {@link T} 绑定后的实例
     */
    @SuppressWarnings("unchecked")
    default <T> T bind(final Vertx vertx) {
        return (T) this;
    }

    /**
     * 步骤1：初始化/配置
     *
     * @param input 输入参数
     *
     * @return 返回值
     */
    @Override
    default Future<R> configure(final I input) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    /**
     * 步骤2：一致性保持最新，同步
     *
     * @param input   输入参数
     * @param request 原始请求：原始请求用于中间 Monad 捕捉最初输入专用
     *
     * @return 返回值
     */
    @Override
    default Future<R> synchro(final I input, final JsonObject request) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    /**
     * 步骤3：验证后期处理环节
     *
     * @param input   输入参数
     * @param request 原始请求：原始请求用于中间 Monad 捕捉最初输入专用
     *
     * @return 返回值
     */
    @Override
    default Future<R> compile(final I input, final JsonObject request) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
