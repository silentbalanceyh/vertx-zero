package io.vertx.tp.rbac.ruler;

import io.vertx.aeon.atom.secure.HCatena;
import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.atom.secure.HSemi;
import io.vertx.aeon.specification.secure.HValve;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HSUiArea extends HSUiNorm {
    /*
     * HSUiArea为分区域配置，内置可调用 HSUiNorm 实现分步骤处理，其结构图：
     * 正常模式
     * Valve  ->  HAdmit ( UiNorm )  ->  JsonObject
     * 区域模式
     * Valve  ->  HAdmit ( UiArea )  ->  HAdmit ( UiNorm )  ->  JsonObject  -> Mapped ( key = JsonObject )
     *                               ->  HAdmit ( UiNorm )  ->  JsonObject
     *                               ->  HAdmit ( UiNorm )  ->  JsonObject
     * 顶层DM依旧按原始读取，子模式的读取会有些许区别
     * 针对 children 节点执行
     */
    @Override
    public Future<JsonObject> compile(final HPermit input, final JsonObject request) {
        // 直接连接 HSDimNorm 做三选一处理
        final ConcurrentMap<String, Future<JsonObject>> futureMap = new ConcurrentHashMap<>();
        input.children().forEach(permit -> {
            // permit -> code ( code 就是 key 相关信息 ）
            final String code = permit.code();
            final JsonObject childJ = this.valueChild(code, request);
            /*
             * Call HAdmit
             * 1) HCatena构造
             * 2) HSemi构造
             * 3）只执行 ui 部分且只执行 compile 方法
             */
            final HCatena catena = HCatena.instance(childJ);
            final HSemi semi = HSemi.create(permit);
            futureMap.put(code, semi.uiCompile(catena)
                .compose(item -> Ux.future(HValve.output(item))));
        });
        return Fn.combineM(futureMap)
            /*
             * child1 = {},
             * child2 = {}
             */
            .compose(normalized -> Ux.future(Ut.toJObject(normalized)));
    }

    private JsonObject valueChild(final String code, final JsonObject requestJ) {
        final JsonArray childrenJ = Ut.valueJArray(requestJ, KName.CHILDREN);
        return Ut.itJArray(childrenJ)
            .filter(childJ -> code.equals(Ut.valueString(childJ, KName.CODE)))
            .findFirst().orElse(new JsonObject());
    }
}
