package io.aeon.runtime;

import io.horizon.annotations.Memory;
import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonObject;

/**
 * 「运行时应用集数据缓存」
 * 氘(dāo)（重氢） - 2H（稀有能源元素）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
interface CH2H extends CH1H {
    /*
     * 「验证规则集」
     * 原 ZeroCodex 部分代码
     */
    @Memory(JsonObject.class)
    Cc<String, JsonObject> CC_CODEX = Cc.open();

    /*
     * 「应用配置集」
     * 用于存储 XApp + XSource 等应用程序配置集
     */
    @Memory(JsonObject.class)
    Cc<String, JsonObject> CC_META_APP = Cc.open();
}
