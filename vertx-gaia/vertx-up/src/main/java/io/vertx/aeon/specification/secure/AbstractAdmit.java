package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.atom.secure.Hoi;
import io.vertx.aeon.specification.app.HES;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractAdmit implements HAdmit {
    protected transient HAtom atom;
    protected transient Hoi owner;

    @Override
    public HAdmit bind(final HAtom atom) {
        this.atom = atom;
        return this.bind(atom.sigma());
    }

    @Override
    public HAdmit bind(final String sigma) {
        this.owner = HES.caller(sigma);
        return this;
    }

    @Override
    public Future<JsonObject> configure(final HPermit permit, final JsonObject requestJ) {
        /*
         * 优先级
         * 1. configure(HPermit, JsonObject)   -> 内置调用 configure(HPermit)
         * 2. configure(HPermit)
         */
        return this.configure(permit);
    }

    /*
     * Dm / Ui Qr data json normalizing
     */
    protected JsonObject valueQr(final JsonObject qrJ, final JsonObject request) {
        if (Ut.isNil(qrJ)) {
            return new JsonObject();
        }
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
        // 维度模型使用统一的Qr构造器，这部分变数比较大，从 Hoi 直接绑定执行器来处理
        return Ut.fromExpression(qrJ, parameters);
    }
}
