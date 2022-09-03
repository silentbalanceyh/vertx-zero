package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HCatena;
import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.atom.secure.HSemi;
import io.vertx.aeon.eon.em.ScDim;
import io.vertx.aeon.eon.em.ScIn;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.run.ActPhase;
import io.vertx.up.exception.web._409UiPhaseEagerException;
import io.vertx.up.exception.web._409UiSourceNoneException;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

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
    public Future<JsonObject> configure(final JsonObject input) {
        // Normalize Request Data
        final HCatena catena = HCatena.instance(input);
        // HPermit New
        final HPermit permit = catena.permit();

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
         *
         *
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
        final HSemi semi = HSemi.create(permit);
        if (ScDim.NONE == permit.shape()) {
            final ActPhase phase = permit.phase();
            // Error-80224，这种模式下，uiPhase 只能定义成 EAGER（由于没有维度）
            Fn.out(ActPhase.EAGER != phase, _409UiPhaseEagerException.class, this.getClass(), phase);
            // 只处理 Ui 部分，不处理其他部分
            return Ux.future(catena)
                .compose(semi::uiConfigure)
                .compose(semi::uiCompile)
                .compose(item -> this.response(permit, item));
        } else {
            // 同时处理 Dm 不分和 Ui 部分

            return Ux.future(catena)
                .compose(semi::dmConfigure)
                .compose(semi::dmCompile)
                .compose(item -> this.uiSmart(permit, item))
                .compose(item -> this.response(permit, item));
        }
    }

    // Ui Smart ( Uniform Call )
    protected Future<HCatena> uiSmart(final HPermit permit, final HCatena catena) {
        final ActPhase phase = permit.phase();
        final HSemi semi = HSemi.create(permit);
        if (ActPhase.EAGER == phase) {
            // configure + compile
            return Ux.future(catena)
                .compose(semi::uiConfigure)
                .compose(semi::uiCompile);
        } else {
            // configure
            return semi.uiConfigure(catena);
        }
    }

    /*
     * 最终返回数据格式
     * {
     *    "in": "xxx",
     *    "dm": "catena -> dataDm，维度",
     *    "ui": "catena -> dataUi，数据"
     * }
     */
    protected abstract Future<JsonObject> response(HPermit permit, HCatena catena);
}
