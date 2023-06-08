package io.aeon.experiment.specification.secure;

import io.aeon.atom.secure.KCatena;
import io.aeon.atom.secure.KPermit;
import io.aeon.atom.secure.KSemi;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.exception.web._409UiPhaseEagerException;
import io.vertx.up.exception.web._409UiSourceNoneException;
import io.vertx.up.fn.Fn;
import io.vertx.up.specification.secure.HValve;
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
     * 管理界面流程详细说明（KCatena / HPermit相互配合，一个为配置，一个为数据）：
     * 0. 前置条件
     * -- uiType不能是NONE，最少需要一个对应数据源
     * 1. 基本流程
     * -- 1.1. DM == NONE（无维度定义）
     *    -- 前置：phase定义必须是EAGER，不可以LAZY，由于无维度定义所以UI数据必须是优先提取，直接在该接口需要返回UI对应数据
     *    -- 1）先执行UI组件 configure
     *    -- 2）再执行UI组件 compile
     * -- 1.2. DM != NONE（带有维度定义：FLAT, TREE, FOREST）
     *    -- 1）先执行DM组件 configure
     *    -- 2）再执行DM组件 compile
     *    -- 3）根据 UI 的 phase
     *    -- 3.1）如果 phase = EAGER
     *            执行UI组件 configure
     *            执行UI组件 compile
     *    -- 3.2）如果 phase = LAZY
     *            执行UI组件 configure
     */
    @Override
    public Future<JsonObject> configure(final JsonObject input) {
        // Normalize Request Data
        final KCatena catena = KCatena.instance(input);
        // KPermit New
        final KPermit permit = catena.permit();

        // Error-80223: uiType不能定义为NONE
        Fn.out(EmSecure.ScIn.NONE == permit.source(), _409UiSourceNoneException.class, this.getClass(), permit.code());
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
        final KSemi semi = KSemi.create(permit);
        if (EmSecure.ScDim.NONE == permit.shape()) {
            final EmSecure.ActPhase phase = permit.phase();
            // Error-80224，这种模式下，uiPhase 只能定义成 EAGER（由于没有维度）
            Fn.out(EmSecure.ActPhase.EAGER != phase, _409UiPhaseEagerException.class, this.getClass(), phase);
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
    protected Future<KCatena> uiSmart(final KPermit permit, final KCatena catena) {
        final EmSecure.ActPhase phase = permit.phase();
        final KSemi semi = KSemi.create(permit);
        if (EmSecure.ActPhase.EAGER == phase) {
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
    protected abstract Future<JsonObject> response(KPermit permit, KCatena catena);
}
