package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.specification.action.HEvent;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.web._501NotSupportException;

/**
 * 「阀」
 * 同时兼容单点系统和分布式的标准权限接口
 * 1. 和 Extension 中的 S_PATH 执行桥接和映射
 * 2. 管理界面：不同管理界面读取不同配置
 * 3. 最终影响：根据配置修改视图
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HValve extends HEvent<JsonObject, HPermit> {
    /*
     * 根据输入的 JsonObject 提取 HPermit 辅助管理
     */
    @Override
    default Future<HPermit> configure(final JsonObject input) {
        return HEvent.super.configure(input);
    }

    /*
     * 数据区域二次提取规则，配合静态和动态
     */
    default Future<JsonObject> configure(final JsonObject input, final HPermit permit) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
