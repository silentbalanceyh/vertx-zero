package io.aeon.atom.secure;

import io.aeon.runtime.CRunning;
import io.vertx.core.Future;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.exception.web._409DmComponentException;
import io.vertx.up.fn.Fn;
import io.vertx.up.specification.secure.HAdmit;

import java.util.Objects;

/*
 * Semi - 半
 * 1）维度
 * 2）数据
 * 运行模式根据选择对维度执行可选项（半运行状态）
 */
public class KSemi {

    private final KPermit permit;

    private KSemi(final KPermit permit) {
        this.permit = permit;
    }

    public static KSemi create(final KPermit permit) {
        Objects.requireNonNull(permit);
        // 缓存组件，和 KPermit 一致
        return CRunning.CC_SEMI.pick(() -> new KSemi(permit), permit.keyCache());
    }

    // DM -> configure
    public Future<KCatena> dmConfigure(final KCatena catena) {
        final HAdmit dm = catena.admit(true);
        if (Objects.isNull(dm)) {
            // 60058, `runComponent` 配置错误，而规则中又配置了维度管理信息
            return Fn.outWeb(_409DmComponentException.class, this.getClass(), this.permit.shape());
        } else {
            return dm.configure(this.permit)
                .compose(dmJ -> catena.config(dmJ, true));
        }
    }

    // DM -> compile
    public Future<KCatena> dmCompile(final KCatena catena) {
        final HAdmit dm = catena.admit(true);
        return dm.compile(this.permit, catena.config(true))
            .compose(dmJ -> catena.data(dmJ, true));
    }

    // UI -> configure, 该过程不理睬 phase
    public Future<KCatena> uiConfigure(final KCatena catena) {
        final HAdmit ui = catena.admit(false);
        // 不论 phase 为何，配置UI都会执行
        return ui.configure(this.permit, catena.request())
            .compose(uiJ -> catena.config(uiJ, false));
    }

    // UI -> compile, 计算相对复杂的流程
    public Future<KCatena> uiCompile(final KCatena catena) {
        final EmSecure.ActPhase phase = this.permit.phase();
        if (EmSecure.ActPhase.EAGER == phase) {
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
