package io.horizon.spi.plugin;

import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.up.atom.exchange.DFabric;

/*
 * 用于处理 AOP 横切层的专用插件接口，目前主要使用在
 * Activity 和 WTodo 中
 * 1）beforeAsync：在逻辑之前执行
 * 2）afterAsync：在逻辑之后执行
 */
public interface AspectPlugin extends DataPlugin<AspectPlugin> {

    @Override
    default AspectPlugin bind(final DataAtom atom) {
        return this;
    }

    @Override
    default AspectPlugin bind(final DFabric fabric) {
        return this;
    }

    /*
     * 前置函数
     */
    default Future<JsonObject> beforeAsync(final JsonObject record, final JsonObject config) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    default Future<JsonArray> beforeAsync(final JsonArray records, final JsonObject config) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    /*
     * 后置函数
     */
    default Future<JsonObject> afterAsync(final JsonObject record, final JsonObject config) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    default Future<JsonArray> afterAsync(final JsonArray records, final JsonObject config) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
