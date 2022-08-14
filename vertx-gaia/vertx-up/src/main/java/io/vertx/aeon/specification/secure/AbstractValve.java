package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.eon.em.ScDim;
import io.vertx.aeon.eon.em.ScIn;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.em.run.ActPhase;
import io.vertx.up.exception.web._409UiPhaseEagerException;
import io.vertx.up.exception.web._409UiSourceNoneException;
import io.vertx.up.fn.Fn;

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
        /*
         * 维度调用组件和数据调用组件都会构造 HAdmit 接口，其生命周期都是一致的：
         * configure / compile
         * - configure 方法内置会调用 compile
         * - compile 则是纯粹的读取数据的方法
         * 在管理界面常见的三种数据流
         * 1）无维度：直接构造 HAdmit
         *    dm ( None )                       ->  ui ( configure -> compile )
         * 2）有维度：EAGER 提取
         *    dm ( configure -> compile )       ->  ui ( configure -> compile )
         * 3）有维度：DELAY 提取
         *    dm ( configure -> compile )       ->  ui ( configure )
         */
        if (ScDim.NONE == permit.shape()) {

            // Error-80224，这种模式下，uiType只能定义成 EAGER
            Fn.out(ActPhase.EAGER != phase, _409UiPhaseEagerException.class, this.getClass(), phase);

            final HAdmit ui = HValve.instanceUi(request);
            // ui ( configure -> compile )
            return ui.configure(permit)
                .compose(uiJ -> ui.compile(permit, uiJ))
                .compose(uiJ -> this.response(request, new JsonObject(), uiJ));
        } else {
            // When Ui is None
            final Refer dmRef = new Refer();
            return this.configureDm(permit, request)
                .compose(dmRef::future)
                .compose(dmJ -> this.configureUi(permit, dmJ, request))
                .compose(uiJ -> this.response(request, dmRef.get(), uiJ));
        }
    }

    // dm ( configure -> compile )
    private Future<JsonObject> configureDm(final HPermit permit, final JsonObject request) {
        final HAdmit dm = HValve.instanceDm(request);
        return dm.configure(permit)
            .compose(dmJ -> dm.compile(permit, dmJ));
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
        final HAdmit ui = HValve.instanceUi(requestJ);
        // phase 提取，如果是 EAGER，则执行 compile 方法，如果是 DELAY 则执行 configure 方法
        final ActPhase phase = permit.phase();
        if (ActPhase.EAGER == phase) {
            // ui ( configure -> compile )
            return ui.configure(permit).compose(uiJ -> ui.compile(permit, uiJ));
        } else {
            // ui ( configure )
            return ui.configure(permit);
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
