package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HSUiNorm extends AbstractAdmit {

    @Override
    public Future<JsonObject> configure(final HPermit permit) {
        /*
         * Ui 的配置流程，配置完成后会输入
         * {
         *     "surface": uiSurface,
         *     "qr":      uiQr
         * }
         */
        final JsonObject request = permit.uiJ();
        final JsonObject qrJ = Ut.valueJObject(request, KName.Rbac.QR);
        request.put(KName.Rbac.QR, this.valueQr(qrJ, null));
        return Future.succeededFuture(request);
    }

    @Override
    public Future<JsonObject> compile(final HPermit input, final JsonObject request) {
        return super.compile(input, request);
    }
}
