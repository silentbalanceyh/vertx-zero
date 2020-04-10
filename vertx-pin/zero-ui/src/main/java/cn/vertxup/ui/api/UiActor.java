package cn.vertxup.ui.api;

import cn.vertxup.ui.service.ControlStub;
import cn.vertxup.ui.service.FormStub;
import cn.vertxup.ui.service.ListStub;
import cn.vertxup.ui.service.PageStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ui.cv.Addr;
import io.vertx.tp.ui.cv.em.ControlType;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;

@Queue
public class UiActor {

    @Inject
    private transient PageStub stub;

    @Inject
    private transient ListStub listStub;

    @Inject
    private transient FormStub formStub;

    @Inject
    private transient ControlStub controlStub;

    @Address(Addr.Page.FETCH_AMP)
    public Future<JsonObject> fetchAmp(final String sigma, final JsonObject body) {
        return this.stub.fetchAmp(sigma, body);
    }


    @Address(Addr.Control.FETCH_BY_ID)
    public Future<JsonObject> fetchControl(final JsonObject body) {
        final String control = body.getString("control");
        final ControlType type = Ut.toEnum(() -> body.getString("type"), ControlType.class, ControlType.NONE);
        if (Ut.notNil(control)) {
            if (ControlType.LIST == type) {
                return this.listStub.fetchById(control);
            } else {
                return this.formStub.fetchById(control);
            }
        } else {
            return Ux.fnJObject(new JsonObject());
        }
    }

    @Address(Addr.Control.FETCH_OP)
    public Future<JsonArray> fetchOps(final JsonObject body) {
        final String control = body.getString("control");
        return this.controlStub.fetchOps(control);
    }

    @Address(Addr.Control.FETCH_FORM_BY_CODE)
    public Future<JsonObject> fetchForm(final String sigma, final String code) {
        return this.formStub.fetchByCode(code, sigma);
    }
}
