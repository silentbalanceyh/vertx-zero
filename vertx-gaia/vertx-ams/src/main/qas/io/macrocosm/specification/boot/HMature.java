package io.macrocosm.specification.boot;

import io.horizon.atom.common.AttrSet;
import io.vertx.core.json.JsonObject;

/**
 * 成熟度，处理环境变量专用的处理模型
 * - 主要用于环境变量的执行
 * - 以及处理环境的所有相关信息的执行
 * 替换原始的 ENV 部分的内容
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HMature {
    /* 环境变量加载，根据环境变量修订核心配置点，实现配置本身的处理 */
    JsonObject configure(JsonObject configJ, AttrSet set);
}
