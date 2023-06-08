package io.mature.extension.migration;

import io.horizon.eon.em.Environment;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/*
 * 升级专用接口，一次性升级
 */
public interface Migrate {

    Migrate bind(Environment environment);

    Migrate bind(HArk ark);

    /*
     * 升级专用
     * 1. 删除原始库
     * 2. 矫正Number数据
     * 3. 处理配置项数据（删除异常进入历史库）
     * 4. 修正状态
     * 5. 删除待确认
     */
    Future<JsonObject> restoreAsync(JsonObject config);

    /*
     * 1. 输出报表
     * 2. 备份数据
     */
    Future<JsonObject> backupAsync(JsonObject config);
}
