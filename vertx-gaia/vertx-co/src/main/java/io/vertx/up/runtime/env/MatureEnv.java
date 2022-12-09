package io.vertx.up.runtime.env;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.record.AttrSet;

/**
 * 成熟度环境模型，负责各种环境变量的读取和执行
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class MatureEnv implements Mature {
    /*
     * vector 数据结构：
     *   field1 = ENV_NAME1
     *   field2 = ENV_NAME2
     */
    @Override
    public JsonObject configure(final JsonObject configJ, final AttrSet set) {
        return null;
    }
}
