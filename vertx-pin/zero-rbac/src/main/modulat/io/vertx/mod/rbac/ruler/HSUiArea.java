package io.vertx.mod.rbac.ruler;

import io.aeon.atom.secure.KCatena;
import io.aeon.atom.secure.KPermit;
import io.aeon.atom.secure.KSemi;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.specification.secure.HValve;
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
    public Future<JsonObject> compile(final KPermit input, final JsonObject request) {
        // 直接连接 HSDimNorm 做三选一处理
        final ConcurrentMap<String, Future<JsonObject>> futureMap = new ConcurrentHashMap<>();
        input.children().forEach(permit -> {
            // permit -> code ( code 就是 key 相关信息 ）
            final String code = permit.code();
            /*
             * 此处的 in 提取是由于输入本身执行了封装，在 uiComponent 调用时，请求被封装成了如下数据结构
             * {
             *     "ui": "uiX部分的配置标准化",
             *     "dm": "dmX部分的配置标准化",
             *     "in": "Json格式的输入"
             * }
             */
            final JsonObject inputJ = Ut.valueJObject(request, KName.Rbac.IN);
            final JsonObject childJ = this.valueChild(code, inputJ);
            /*
             * Call HAdmit
             * 1) HCatena构造
             * 2) HSemi构造
             */
            final KCatena catena = KCatena.instance(childJ);
            final KSemi semi = KSemi.create(permit);
            futureMap.put(code, Ux.future(catena)
                /*
                 * 此处的特殊点在于，必须执行 uiConfigure 方法先对UI部分执行配置，否则
                 * uiConfig 中的数据不会被 KCatena 捕捉导致 configUi 无值，而这个流程和
                 * 步骤在后期会被规范化，简单说 ui 执行和 dm 的执行只会出现以下几种
                 * 1) DM -> configure -> compile
                 * 2) DM -> configure
                 * 3) UI -> configure -> compile
                 * 4) UI -> configure
                 * 不存在单独的 compile 流程
                 */
                .compose(semi::uiConfigure).compose(semi::uiCompile)
                .compose(item -> Ux.future(HValve.output(item))));
        });
        return Fn.combineM(futureMap)
            /* children = {} */
            .compose(normalized -> Fn.ifJObject(KName.CHILDREN, Ut.toJObject(normalized)));
    }

    private JsonObject valueChild(final String code, final JsonObject requestJ) {
        final JsonArray childrenJ = Ut.valueJArray(requestJ, KName.CHILDREN);
        return Ut.itJArray(childrenJ)
            .filter(childJ -> code.equals(Ut.valueString(childJ, KName.CODE)))
            .findFirst().orElse(new JsonObject());
    }
}
