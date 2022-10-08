package cn.vertxup.ui.api;

import cn.vertxup.ui.service.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ui.cv.Addr;
import io.vertx.tp.ui.cv.em.ControlType;
import io.vertx.tp.ui.cv.em.OpType;
import io.vertx.tp.ui.refine.Ui;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KValue;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.ViewType;
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
    private transient DoStub doStub;

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
            /*
             * 新逻辑处理，参数种类
             * control -> controlId
             * type    -> null / ATOM,   动态表单
             *         -> FLOW,          流程表单,   提取 workflow,event
             *         -> WEB,           静态表单,   提取 identifier
             * 隐含流程：当 control = null 时，检索 identifier 执行 WEB 类型读取
             */
            final String type = Ut.valueString(body, KName.TYPE);
            final OpType opType = Ut.toEnum(() -> type, OpType.class, null);
            if (Objects.isNull(opType)) {
                // ATOM / WEB
                return this.doStub.fetchSmart(body);
            } else {
                // 标准流程
                return switch (opType) {
                    // ATOM
                    case ATOM -> this.doStub.fetchAtom(body);
                    // WEB
                    case WEB -> this.doStub.fetchWeb(body);
                    // FLOW
                    case FLOW -> this.doStub.fetchFlow(body);
                };
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

    @Address(Addr.Control.FETCH_LIST_QR_BY_CODE)
    public Future<JsonArray> fetchListQr(
        final String id, final String position,
        final String typeStr, final XHeader header
    ) {
        final JsonObject condition = Ux.whereAnd()
            .put(KName.SIGMA, header.getSigma())
            .put(KName.POSITION, position);
        final ViewType type = Ut.toEnum(ViewType.class, typeStr);
        if (ViewType.Model == type) {
            // POSITION = ? AND SIGMA = ? AND IDENTIFIER = ? ORDER BY SORT ASC
            condition.put(KName.IDENTIFIER, id);
        } else {
            // POSITION = ? AND SIGMA = ? AND WORKFLOW = ? ORDER BY SORT ASC
            condition.put(KName.Flow.WORKFLOW, id);
        }
        return this.listStub.fetchQr(condition);
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
