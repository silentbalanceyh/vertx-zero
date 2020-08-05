package cn.vertxup.ui.api;

import cn.vertxup.ui.service.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.cv.KeField;
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

    @Inject
    private transient OpStub opStub;

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
        if (Ut.notNil(control)) {
            /*
             * UI_OP stored
             */
            return this.opStub.fetchDynamic(control);
        } else {
            /*
             * fetch data plugin/ui/ops.json
             */
            final String identifier = body.getString(KeField.IDENTIFIER);
            return this.opStub.fetchFixed(identifier);
        }
    }

    @Address(Addr.Control.FETCH_FORM_BY_CODE)
    public Future<JsonObject> fetchForm(final String sigma, final String code) {
        return this.formStub.fetchByCode(code, sigma);
    }

    @Address(Addr.Control.FETCH_FORM_BY_IDENTIFIER)
    public Future<JsonArray> fetchForms(final String sigma, final String identifier) {
        return this.formStub.fetchByIdentifier(identifier, sigma);
    }


    @Address(Addr.Control.FETCH_LIST_BY_IDENTIFIER)
    public Future<JsonArray> fetchLists(final String sigma, final String identifier) {
        return this.listStub.fetchByIdentifier(identifier, sigma);
    }
}
