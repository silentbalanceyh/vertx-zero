package io.vertx.tp.rbac.ruler;

import io.vertx.aeon.atom.secure.HCatena;
import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.specification.secure.AbstractAdmit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HSUiArea extends AbstractAdmit {
    /*
     * HSUiArea为分区域配置，内置可调用 HSUiNorm 实现分步骤处理，其结构图：
     * 正常模式
     * Valve  ->  HAdmit ( UiNorm )  ->  JsonObject
     * 区域模式
     * Valve  ->  HAdmit ( UiArea )  ->  HAdmit ( UiNorm )  ->  JsonObject  -> Mapped ( key = JsonObject )
     *                               ->  HAdmit ( UiNorm )  ->  JsonObject
     *                               ->  HAdmit ( UiNorm )  ->  JsonObject
     */
    @Override
    public Future<JsonObject> configure(final HPermit input, final JsonObject requestJ) {
        final ConcurrentMap<String, Future<JsonObject>> configureJ = new ConcurrentHashMap<>();
        final JsonArray children = Ut.valueJArray(requestJ, KName.CHILDREN);
        Ut.itJArray(children).forEach(childJ -> {
            // childJ 和 input 的双向检查
            final HCatena catena = HCatena.instance(childJ);
            final HPermit permit = catena.permit();

        });
        return Ux.future();
    }

    @Override
    public Future<JsonObject> compile(final HPermit input, final JsonObject request) {
        return Ux.future();
    }
}
