package io.vertx.tp.rbac.ruler;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.specification.secure.AbstractAdmit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;

public class HSUiArea extends AbstractAdmit {
    /*
     * HSUiArea为分区域配置，内置可调用 HSUiNorm 实现分步骤处理，其结构图：
     * 正常模式
     * Valve  ->  HAdmit ( UiNorm )  ->  JsonObject
     * 区域模式
     * Valve  ->  HAdmit ( UiArea )  ->  HAdmit ( UiNorm )  ->  JsonObject  -> Mapped ( key = JsonObject )
     *                               ->  HAdmit ( UiNorm )  ->  JsonObject
     *                               ->  HAdmit ( UiNorm )  ->  JsonObject
     * 顶层DM依旧按原始读取，子模式的读取会有些许区别
     */
    @Override
    public Future<JsonObject> configure(final HPermit permit, final JsonObject requestJ) {
        return super.configure(permit, requestJ, HPermit::uiJ);
    }

    @Override
    public Future<JsonObject> compile(final HPermit input, final JsonObject request) {
        // 直接连接 HSDimNorm 做三选一处理
        return Ux.future();
    }
}
