package cn.vertxup.ui.api;

import cn.vertxup.ui.service.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ui.cv.Addr;
import io.vertx.tp.ui.cv.em.ControlType;
import io.vertx.tp.ui.init.UiPin;
import io.vertx.tp.ui.refine.Ui;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.cache.Rapid;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.function.Supplier;

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
        return UiCache.getControl(body, () -> {
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
        return UiCache.getOps(body, () -> {
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
                final String identifier = body.getString(KName.IDENTIFIER);
                return this.opStub.fetchFixed(identifier);
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
}

class UiCache {

    private static final Annal LOGGER = Annal.get(UiCache.class);

    public static Future<JsonObject> getControl(final JsonObject body,
                                                final Supplier<Future<JsonObject>> executor) {
        return getCache(UiPin::keyControl, body, executor);
    }

    public static Future<JsonArray> getOps(final JsonObject body,
                                           final Supplier<Future<JsonArray>> executor) {
        return getCache(UiPin::keyOps, body, executor);
    }

    private static <T> Future<T> getCache(
        final Supplier<String> poolFn,
        final JsonObject body,
        final Supplier<Future<T>> executor) {
        final String keyPool = poolFn.get();
        if (Ut.notNil(keyPool)) {
            final String uiKey = String.valueOf(body.hashCode());
            return Rapid.<String, T>t(keyPool).cached(uiKey, executor);
        } else {
            Ui.infoUi(LOGGER, "Ui Cached has been disabled!");
            return executor.get();
        }
    }
}
