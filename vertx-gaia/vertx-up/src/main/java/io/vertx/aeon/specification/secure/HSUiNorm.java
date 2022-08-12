package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HSUiNorm extends AbstractAdmit {

    /*
     * 1. Ui 的配置流程，配置完成后会输入
     * {
     *     "surface": uiSurface,
     *     "qr":      uiQr
     * }
     */
    @Override
    public Future<JsonObject> configure(final HPermit input) {
        /*
         * 此处需做一个逻辑处理，根据输入参数配置当前调用的
         */
        return Future.succeededFuture(input.uiJ());
    }

    @Override
    public Future<JsonObject> compile(final HPermit input, final JsonObject request) {
        return super.compile(input, request);
    }
}
