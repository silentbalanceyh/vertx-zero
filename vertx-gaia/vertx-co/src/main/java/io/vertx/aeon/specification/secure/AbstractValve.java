package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.eon.HCache;
import io.vertx.aeon.eon.em.ScDim;
import io.vertx.aeon.eon.em.ScIn;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.web._409SecureAdminException;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractValve implements HValve {
    /*
     * 输入：
     * {
     *     "name": "xxx",
     *     "code": "xxx",
     *     "mapping": {},
     *
     *     "dmType": "Dm Type",
     *     "dmComponent": "HDm Interface"
     *     "dmCondition": {},
     *     "dmConfig": {},
     *
     *     "uiType": "Ui Type",
     *     "uiComponent": "HUi Interface",
     *     "uiCondition": {},
     *     "uiConfig": {},
     *     "uiSurface": {}
     * }
     */
    private static final Cc<String, HPermit> CC_PERMIT = Cc.open();

    @Override
    public Future<JsonObject> configure(final JsonObject input) {
        // HPermit Build
        final HPermit permit = this.build(input);
        /*
         * Here are two workflow for different part
         * 1) Dim = None / Ui must not be None
         * 2) Dim <> None /
         */
        // HSDim Processing
        if (ScDim.NONE == permit.shape()) {
            // Ui must not be none
            Fn.out(ScIn.NONE == permit.source(), _409SecureAdminException.class, this.getClass(), permit.code());

            // Ui Processing Only
            final Class<?> uiCls = Ut.valueCI(input, KName.Component.UI_COMPONENT, HSUi.class);
            final HSUi ui = (HSUi) HCache.CCT_EVENT.pick(() -> Ut.instance(uiCls), uiCls.getName());

            return ui.configure(permit)
                .compose(uiJ -> this.response(input, new JsonObject(), uiJ));
        } else {
            // When Ui is None
            final Class<?> dmCls = Ut.valueCI(input, KName.Component.DM_COMPONENT, HSDim.class);
            final HSDim dm = (HSDim) HCache.CCT_EVENT.pick(() -> Ut.instance(dmCls), dmCls.getName());
            if (ScIn.NONE == permit.source()) {


                return dm.configure(permit)
                    .compose(dmJ -> this.response(input, dmJ, new JsonObject()));
            } else {
                final Class<?> uiCls = Ut.valueCI(input, KName.Component.UI_COMPONENT, HSUi.class);
                final HSUi ui = (HSUi) HCache.CCT_EVENT.pick(() -> Ut.instance(uiCls), uiCls.getName());
                return dm.configure(permit).compose(dmJ -> ui.configure(permit)


                    .compose(uiJ -> this.response(input, dmJ, uiJ))
                );
            }
        }
    }

    protected abstract Future<JsonObject> response(final JsonObject input, final JsonObject dimJ, final JsonObject uiJ);

    private HPermit build(final JsonObject input) {
        // Build Cache Key Based on `sigma / code`
        final String sigma = Ut.valueString(input, KName.SIGMA);
        final String code = Ut.valueString(input, KName.CODE);
        Ut.notNull(sigma, code);
        return CC_PERMIT.pick(() -> {
            final HPermit permit = HPermit.create(code, Ut.valueString(input, KName.NAME));
            // Dim Processing
            final ScDim dmType = Ut.toEnum(() -> Ut.valueString(input, KName.DM_TYPE), ScDim.class, ScDim.NONE);
            permit.shape(dmType).shape(
                Ut.valueJObject(input, KName.DM_CONDITION),
                Ut.valueJObject(input, KName.DM_CONFIG)
            );
            // Ui Processing
            final ScIn uiType = Ut.toEnum(() -> Ut.valueString(input, KName.UI_TYPE), ScIn.class, ScIn.NONE);
            permit.ui(uiType).ui(Ut.valueJObject(input, KName.UI_SURFACE)).ui(
                Ut.valueJObject(input, KName.UI_CONDITION),
                Ut.valueJObject(input, KName.UI_CONFIG)
            );
            return permit;
        }, sigma + Strings.SLASH + code);
    }
}
