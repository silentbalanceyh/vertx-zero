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
import io.vertx.tp.ui.refine.Ui;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KValue;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;

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
        return Ui.cacheControl(body, () -> {
            final String control = body.getString("control");
            final ControlType type = Ut.toEnum(() -> body.getString("type"), ControlType.class, ControlType.NONE);
            if (Ut.notNil(control)) {
                if (ControlType.LIST == type) {
                    return this.listStub.fetchById(control);
                } else {
                    return this.formStub.fetchById(control);
                }
            } else {
                return Ux.futureJ(new JsonObject());
            }
        });
    }

    @Address(Addr.Control.FETCH_OP)
    public Future<JsonArray> fetchOps(final JsonObject body) {
        return Ui.cacheOps(body, () -> {
            final String control = body.getString("control");
            if (Ut.notNil(control)) {
                /*
                 * UI_OP stored
                 */
                return this.listStub.fetchOpDynamic(control);
            } else {
                /*
                 * fetch data plugin/ui/ops.json
                 */
                final String identifier = body.getString(KName.IDENTIFIER);
                return this.listStub.fetchOpFixed(identifier);
            }
        });

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

    @Address(Addr.Control.FETCH_BY_VISITOR)
    @Me
    public Future<JsonObject> fetchByVisitor(final String page, final String identifier, final JsonObject params) {
        /*
         * The input parameters
         * {
         *      "page": xxx,
         *      "identifier": xxx,
         *      "path": based on view/position,
         *      "type": calculate the parameter from params,
         *      "sigma": extract from params,
         *      "alias": The name that you can define here.
         * }
         */
        final ControlType controlType = Ut.toEnum(ControlType.class, params.getString(KName.TYPE));

        if (Objects.isNull(controlType)) {
            return Ux.futureJ();
        } else {
            /*
             * calculate the `path` based on `view` and `position`
             */
            final String view = params.getString(KName.VIEW, KValue.View.VIEW_DEFAULT);
            final String position = params.getString(KName.POSITION, KValue.View.POSITION_DEFAULT);
            final String alias = params.getString(KName.ALIAS, KValue.View.VIEW_DEFAULT);

            /*
             * Build Parameters
             * Here must contain following two node for dynamic seeking.
             * 1. `data`
             * 2. `config`
             */
            final JsonObject request = new JsonObject();
            request.put(KName.SIGMA, params.getString(KName.SIGMA));
            request.put(KName.IDENTIFIER, identifier);
            request.put(KName.Ui.PAGE, page);
            final String path = view + Strings.SLASH + position + Strings.SLASH + alias;
            request.put(KName.App.PATH, path);
            /*
             * data
             * config
             */
            Ut.elementCopy(request, params, KName.DATA, KName.CONFIG);
            return this.controlStub.fetchControl(controlType, request);
        }
    }
}
