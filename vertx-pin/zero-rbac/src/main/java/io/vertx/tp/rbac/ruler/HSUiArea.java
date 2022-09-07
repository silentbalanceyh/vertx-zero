package io.vertx.tp.rbac.ruler;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.specification.secure.AbstractAdmit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

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
    public Future<JsonObject> configure(final HPermit input, final JsonObject requestJ) {
        final JsonObject configured = this.valueConfig(input, requestJ);
        /*
         * configured 中已经包含了 qr 节点（主节点）
         * 此处构造子节点专用 qr
         * {
         *     "children": {
         *         "code1": children1,
         *         "code2": children2
         *     }
         * }
         * 最终构造的结构如：
         * {
         *    ...Dm,
         *    children: {
         *        code1: child1Dm,
         *        code2: child2Dm
         *    }
         * }
         * children 为前后端统一的接口
         */
        final JsonArray childrenA = Ut.valueJArray(requestJ, KName.CHILDREN);
        Ut.itJArray(childrenA).forEach(childJ -> {
            // code -> permit ( Child )
            final String code = Ut.valueString(childJ, KName.CODE);
            if (Ut.notNil(code)) {
                final HPermit child = input.child(code);
                final JsonObject childDm = this.valueConfig(child, childJ);
                configured.put(code, childDm);
            }
        });
        return Ux.future(configured);
    }

    @Override
    public Future<JsonObject> compile(final HPermit input, final JsonObject request) {
        // 直接连接 HSDimNorm 做三选一处理
        return Ux.future();
    }
}
