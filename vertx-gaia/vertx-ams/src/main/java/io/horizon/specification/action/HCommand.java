package io.horizon.specification.action;

import io.vertx.core.json.JsonObject;

/**
 * 「指令」指令抽象到底层某个单独行为中（底层封装）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HCommand<I, R> {

    // 步骤1：初始化/配置
    R configure(I input);

    // 步骤2：一致性保持最新，同步
    default R synchro(final I input, final JsonObject request) {
        return null;
    }

    // 步骤3：验证后期处理环节
    default R compile(final I input, final JsonObject request) {
        return null;
    }
}
