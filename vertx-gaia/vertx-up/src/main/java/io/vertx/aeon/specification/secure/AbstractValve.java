package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.eon.em.ScDim;
import io.vertx.aeon.eon.em.ScIn;
import io.vertx.aeon.runtime.HCache;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.run.ActPhase;
import io.vertx.up.exception.web._409UiPhaseEagerException;
import io.vertx.up.exception.web._409UiSourceNoneException;
import io.vertx.up.fn.Fn;
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

    @Override
    public Future<JsonObject> configure(final JsonObject request) {
        // HPermit Build
        final HPermit permit = HAdmit.build(request);

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
            final Class<?> uiCls = Ut.valueCI(request, KName.Component.UI_COMPONENT, HAdmit.class);
            final HAdmit ui = (HAdmit) HCache.CCT_EVENT.pick(() -> Ut.instance(uiCls), uiCls.getName());

            // Error-80224，这种模式下，uiType只能定义成 EAGER
            Fn.out(ActPhase.EAGER != phase, _409UiPhaseEagerException.class, this.getClass(), phase);

            return ui.configure(permit)
                .compose(uiJ -> this.response(request, new JsonObject(), uiJ));
        } else {
            // When Ui is None
            final Class<?> dmCls = Ut.valueCI(request, KName.Component.DM_COMPONENT, HAdmit.class);
            final HAdmit dm = (HAdmit) HCache.CCT_EVENT.pick(() -> Ut.instance(dmCls), dmCls.getName());

            return dm.configure(permit)
                .compose(dmJ -> this.configureUi(permit, dmJ, request)


                    .compose(uiJ -> this.response(request, dmJ, uiJ))
                );
        }
    }

    /*
     * 根据定义的 Phase 决定调用 HAdmit 的哪个方法，根据新规范定义，整个生命周期方法如下：
     *
     * 1. configure(I i)
     * - 该方法无前端传入的输入信息，直接操作接口中实现的实体，如此处为 HPermit
     *
     * 2. synchro(I input, JsonObject request)
     * - 该方法为专用写入方法，该写入方法会在保存或更新配置时执行同步专用，其中主实体为 HPermit
     *   而输入数据为 request，输入格式根据需要而有所区别
     *
     * 3. compile(I input, JsonObject request)
     * - 该方法为专用读取方法，一般用于二次读取，在维度数据提取之后根据维度数据执行 UI 层数据的提取
     *   相关操作
     */
    private Future<JsonObject> configureUi(final HPermit permit, final JsonObject dmJ, final JsonObject requestJ) {
        final Class<?> uiCls = Ut.valueCI(requestJ, KName.Component.UI_COMPONENT, HAdmit.class);
        final HAdmit ui = (HAdmit) HCache.CCT_EVENT.pick(() -> Ut.instance(uiCls), uiCls.getName());
        // phase 提取，如果是 EAGER，则执行 compile 方法，如果是 DELAY 则执行 configure 方法
        final ActPhase phase = permit.phase();
        if (ActPhase.EAGER == phase) {
            // 只处理配置，等待前端合并执行数据读取，执行方法：
            // configure
            return ui.configure(permit);
        } else {
            // 执行数据读取操作，调用：
            // compile
            /*
             * 参数配置
             * {
             *    "in": requestJ,
             *    "dm": dmJ
             * }
             */
            final JsonObject parameters = new JsonObject();
            parameters.put(KName.Rbac.IN, requestJ);
            parameters.put(KName.Rbac.DM, dmJ);
            return ui.compile(permit, parameters);
        }
    }

    /*
     * 最终返回的数据格式：
     *
     * {
     *    "in": "输入",
     *    "out": "输出",
     *    "dm": "维度配置",
     *    "ui": "数据配置"
     * }
     */
    protected abstract Future<JsonObject> response(final JsonObject input, final JsonObject dimJ, final JsonObject uiJ);
}
