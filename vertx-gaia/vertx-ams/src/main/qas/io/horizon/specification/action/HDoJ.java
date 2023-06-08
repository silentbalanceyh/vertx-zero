package io.horizon.specification.action;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;

/**
 * @author lang : 2023-06-03
 */
public interface HDoJ<O> {
    /**
     * （JsonObject）专用检查方法，检查数据结果是否符合预期
     *
     * @param data   数据
     * @param config 配置
     *
     * @return {@link O}
     */
    O executeJ(final ClusterSerializable data, final JsonObject config);

    /**
     * 「异步版本」（JsonObject）专用检查方法，检查数据结果是否符合预期
     *
     * @param data   数据
     * @param config 配置
     *
     * @return {@link Future}
     */
    default Future<O> executeJAsync(final ClusterSerializable data, final JsonObject config) {
        return Future.succeededFuture(this.executeJ(data, config));
    }
}
