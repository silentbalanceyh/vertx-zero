package io.vertx.aeon.atom.secure;

import io.vertx.aeon.runtime.H3H;
import io.vertx.aeon.specification.secure.HAdmit;
import io.vertx.core.Future;
import io.vertx.up.eon.em.run.ActPhase;
import io.vertx.up.exception.web._409DmComponentException;
import io.vertx.up.fn.Fn;

import java.util.Objects;

/*
 * Semi - 半
 * 1）维度
 * 2）数据
 * 运行模式根据选择对维度执行可选项（半运行状态）
 */
public class HSemi {

    private final HPermit permit;

    private HSemi(final HPermit permit) {
        this.permit = permit;
    }

    public static HSemi create(final HPermit permit) {
        Objects.requireNonNull(permit);
        // 缓存组件，和 HPermit 一致
        return H3H.CC_SEMI.pick(() -> new HSemi(permit), permit.keyCache());
    }

    // DM -> configure
    public Future<HCatena> dmConfigure(final HCatena catena) {
        final HAdmit dm = catena.admit(true);
        if (Objects.isNull(dm)) {
            // 60058, `runComponent` 配置错误，而规则中又配置了维度管理信息
            return Fn.error(_409DmComponentException.class, this.getClass(), this.permit.shape());
        } else {
            return dm.configure(this.permit)
                .compose(dmJ -> catena.config(dmJ, true));
        }
    }

    // DM -> compile
    public Future<HCatena> dmCompile(final HCatena catena) {
        final HAdmit dm = catena.admit(true);
        return dm.compile(this.permit, catena.config(true))
            .compose(dmJ -> catena.data(dmJ, true));
    }

    // UI -> configure, 该过程不理睬 phase
    public Future<HCatena> uiConfigure(final HCatena catena) {
        final HAdmit ui = catena.admit(false);
        // 不论 phase 为何，配置UI都会执行
        return ui.configure(this.permit, catena.request())
            .compose(uiJ -> catena.config(uiJ, false));
    }

    // UI -> compile, 计算相对复杂的流程
    public Future<HCatena> uiCompile(final HCatena catena) {
        final ActPhase phase = this.permit.phase();
        if (ActPhase.EAGER == phase) {
            /*
             * dm = catena -> dataDm    UI编译时，消费的DM维度数据为处理后的维度数据
             *                          所以
             *                          1）configDm 永远不会被 UI 消费
             *                          2）「前端模式」下，configDm 会作为dm输出
             * ui = catena -> configUi  UI编译的目的是生成UI数据，所以此处的UI数据为原生配置数据
             *                          所以
             *                          1）configUi 在生成 UI 时使用
             *                          2）「前端模式」下，configUi 会作为ui输出
             * in = catena -> request   此处的请求数据只有一份
             */
            final HAdmit ui = catena.admit(false);
            return ui.compile(this.permit, catena.medium())
                /*
                 * 标准化数据格式，无数据也执行标准格式化输出主要，保持 uiCompile 只追加
                 * {
                 *     "data": {
                 *     }
                 * }
                 * 其余内容全部使用另外的方式标准化，该标准化存在于本类内部
                 */
                .compose(uiJ -> catena.data(uiJ, false));
        } else {
            // Nothing Processing
            return Future.succeededFuture(catena);
        }
    }
}
