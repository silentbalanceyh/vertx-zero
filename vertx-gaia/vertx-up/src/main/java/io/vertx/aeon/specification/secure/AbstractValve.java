package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.eon.em.ScDim;
import io.vertx.aeon.eon.em.ScIn;
import io.vertx.aeon.runtime.HCache;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.em.run.ActPhase;
import io.vertx.up.exception.web._409UiPhaseEagerException;
import io.vertx.up.exception.web._409UiSourceNoneException;
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

        // Error-80223: uiType不能定义为NONE
        Fn.out(ScIn.NONE == permit.source(), _409UiSourceNoneException.class, this.getClass(), permit.code());
        /*
         * 管理界面分为两个区域：
         * DM - 维度管理区域，UI - 数据管理区域
         * - permit.shape():    指定维度管理区域的数据源
         * - permit.source():   指定数据管理区域的数据源（该数据源必须定义，不能为 NONE，否则无法管理）
         * 另外的规范
         *
         * 1）如果维度区域为 NONE, 数据区域的触发阶段必须是 EAGER，否则无数据输出
         * 2）如果维度区域存在，则分为
         *   - 前端计算：phase = EAGER，在配置时调用执行方法，数据读取到前端计算
         *   - 延迟计算：phase = DELAY，等维度区域读取过后，根据维度区域的选择执行计算
         */
        final ActPhase phase = permit.phase();
        // HSDim Processing
        if (ScDim.NONE == permit.shape()) {

            // Ui Processing Only
            final Class<?> uiCls = Ut.valueCI(input, KName.Component.UI_COMPONENT, HAdmit.class);
            final HAdmit ui = (HAdmit) HCache.CCT_EVENT.pick(() -> Ut.instance(uiCls), uiCls.getName());

            // Error-80224，这种模式下，uiType只能定义成 EAGER
            Fn.out(ActPhase.EAGER != phase, _409UiPhaseEagerException.class, this.getClass(), phase);

            return ui.configure(permit)
                .compose(uiJ -> this.response(input, new JsonObject(), uiJ));
        } else {
            // When Ui is None
            final Class<?> dmCls = Ut.valueCI(input, KName.Component.DM_COMPONENT, HAdmit.class);
            final HAdmit dm = (HAdmit) HCache.CCT_EVENT.pick(() -> Ut.instance(dmCls), dmCls.getName());
            final Class<?> uiCls = Ut.valueCI(input, KName.Component.UI_COMPONENT, HAdmit.class);
            final HAdmit ui = (HAdmit) HCache.CCT_EVENT.pick(() -> Ut.instance(uiCls), uiCls.getName());
            return dm.configure(permit).compose(dmJ -> ui.configure(permit)


                .compose(uiJ -> this.response(input, dmJ, uiJ))
            );
        }
    }

    protected abstract Future<JsonObject> response(final JsonObject input, final JsonObject dimJ, final JsonObject uiJ);

    private HPermit build(final JsonObject input) {
        // Build Cache Key Based on `sigma / code`
        final String sigma = Ut.valueString(input, KName.SIGMA);
        final String code = Ut.valueString(input, KName.CODE);
        Ut.notNull(sigma, code);
        // Store KApp information
        return CC_PERMIT.pick(() -> {

            // 绑定基础信息：code, sigma, name
            final HPermit permit = HPermit.create(code, sigma, Ut.valueString(input, KName.NAME));


            // 维度信息绑定
            final ScDim dmType = Ut.toEnum(() -> Ut.valueString(input, KName.DM_TYPE), ScDim.class, ScDim.NONE);
            permit.shape(dmType).shape(Ut.valueJObject(input, KName.MAPPING)).shape(
                Ut.valueJObject(input, KName.DM_CONDITION),
                Ut.valueJObject(input, KName.DM_CONFIG)
            );


            // 呈现信息绑定
            final ScIn uiType = Ut.toEnum(
                () -> Ut.valueString(input, KName.UI_TYPE), ScIn.class, ScIn.NONE);
            final ActPhase uiPhase = Ut.toEnum(
                () -> Ut.valueString(input, KName.PHASE), ActPhase.class, ActPhase.ERROR);

            permit.ui(uiType, uiPhase).ui(Ut.valueJObject(input, KName.UI_SURFACE)).ui(
                Ut.valueJObject(input, KName.UI_CONDITION),
                Ut.valueJObject(input, KName.UI_CONFIG)
            );
            return permit;

            // 缓存键值：<sigma> / <code>, code 是唯一的
        }, sigma + Strings.SLASH + code);
    }
}
