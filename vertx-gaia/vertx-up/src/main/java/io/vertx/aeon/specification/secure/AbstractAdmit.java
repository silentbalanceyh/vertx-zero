package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.Hoi;
import io.vertx.aeon.specification.app.HES;
import io.vertx.core.json.JsonObject;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AbstractAdmit implements HAdmit {
    protected transient HAtom atom;
    protected transient Hoi owner;

    @Override
    public HAdmit bind(final HAtom atom) {
        this.atom = atom;
        this.owner = HES.caller(atom.sigma());
        return this;
    }

    /*
     * Dm / Ui Qr data json normalizing
     */
    public JsonObject valueQr(final JsonObject qrJ, final JsonObject request) {
        if (Ut.isNil(qrJ)) {
            return new JsonObject();
        }
        final JsonObject parameters = this.valueParameter(request);
        // 维度模型使用统一的Qr构造器，这部分变数比较大，从 Hoi 直接绑定执行器来处理
        return Ut.fromExpression(qrJ, parameters);
    }

    private JsonObject valueParameter(final JsonObject request) {
        final JsonObject parameters = Ut.valueJObject(request, true);
        /*
         * {
         *     "sigma": "xxx",
         *     "appId": "xxx",
         *     "tenantId": "xxx",
         *     "language": "xxx"
         * }
         */
        if (Objects.nonNull(this.owner)) {
            this.owner.inputQr(parameters, false);
        }
        return parameters;
    }
}
