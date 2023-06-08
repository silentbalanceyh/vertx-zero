package io.aeon.experiment.specification.secure;

import io.aeon.atom.secure.KPermit;
import io.horizon.eon.em.EmApp;
import io.horizon.uca.boot.KPivot;
import io.horizon.util.HUt;
import io.macrocosm.specification.app.HAmbient;
import io.macrocosm.specification.program.HArk;
import io.modello.specification.atom.HAtom;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.specification.secure.HAdmit;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractAdmit implements HAdmit {
    protected transient HAtom atom;
    protected transient HArk ark;
    protected transient EmApp.Mode mode;

    @Override
    public HAdmit bind(final HAtom atom) {
        this.atom = atom;
        return this.bind(atom.ark().sigma());
    }

    @Override
    public HAdmit bind(final String sigma) {
        final HAmbient ambient = KPivot.running();
        this.ark = ambient.running(sigma);
        this.mode = ambient.mode();
        return this;
    }

    /*
     * 该方法止于此处，不可以继承，且内置调用
     * configure(KPermit, JsonObject)
     * 优先级切换，只要不直接实现 HAdmit 则不会牵涉到方法的重写部分
     * 若要自定义方法，则此处需调整。
     */
    @Override
    public final Future<JsonObject> configure(final KPermit permit) {
        return this.configure(permit, null);
    }

    protected Future<JsonObject> configure(final KPermit permit, final JsonObject requestJ, final Function<KPermit, JsonObject> supplierJ) {
        // 父结构
        /*
         * Dm / Ui配置，子类使用，目前版本流程
         * 1）调用 valueQr 执行查询条件处理
         *
         * - 该方法可在主记录中调用，也可以在子记录中调用
         * - 如果出现多级，则构造如下结构
         * {
         *     "items": ...,
         *     "....",
         *     "children": {
         *         "code": {
         *             "DM递归结构（不包含children和webChildren），只支持一级递归，不支持二级递归"
         *         }
         *     }
         * }
         * 当前版本中 DM 和 UI 未分离开，DM 和 UI 都会调用该方法，只是 supplierJ 会有区别
         */
        // 复制原始记录（创建副本以防止混乱）
        final JsonObject normalizedJ = this.valueConfig(permit, requestJ, supplierJ);
        // 子结构
        /*
         * configured 中已经包含了 qr 节点（主节点）
         * 此处构造子节点专用 qr
         * {
         *     "children": {
         *         "code1": children1,
         *         "code2": children2
         *     }
         * }
         * 最终构造的结构如：
         * {
         *    ...Dm,
         *    children: {
         *        code1: child1Dm,
         *        code2: child2Dm
         *    }
         * }
         * children 为前后端统一的接口
         */
        final JsonArray childrenA = Ut.valueJArray(requestJ, KName.CHILDREN);
        if (Ut.isNotNil(childrenA)) {
            final JsonObject childrenJ = this.valueChildren(permit, childrenA, supplierJ);
            normalizedJ.put(KName.CHILDREN, childrenJ);
        }
        return Ux.future(normalizedJ);
    }

    /*
     * Dm / Ui Qr data json normalizing
     */
    protected JsonObject configureQr(final JsonObject qrJ, final JsonObject request) {
        if (Ut.isNil(qrJ)) {
            return new JsonObject();
        }
        final JsonObject parameters = Ut.valueJObject(request, true);
        /*
         * {
         *     "sigma": "xxx",
         *     "appId": "xxx",
         *     "tenantId": "xxx",
         *     "language": "xxx"
         * }
         */
        Optional.ofNullable(this.ark).ifPresent(item -> {
            final JsonObject qr = HUt.qrApp(item, this.mode);
            parameters.mergeIn(qr, true);
        });
        // 维度模型使用统一的Qr构造器，这部分变数比较大，从 Hoi 直接绑定执行器来处理
        return Ut.fromExpression(qrJ, parameters);
    }

    // ----------------------------- 私有方法内部调用 -------------------------
    /*
     * Dm / Ui配置，子类使用，目前版本流程
     * 1）调用 valueQr 执行查询条件处理
     *
     * - 该方法可在主记录中调用，也可以在子记录中调用
     * - 如果出现多级，则构造如下结构
     * {
     *     "items": ...,
     *     "....",
     *     "children": {
     *         "code": {
     *             "DM递归结构（不包含children和webChildren），只支持一级递归，不支持二级递归"
     *         }
     *     }
     * }
     * 当前版本中 DM 和 UI 未分离开，DM 和 UI 都会调用该方法，只是 supplierJ 会有区别
     */
    private JsonObject valueConfig(final KPermit permit, final JsonObject requestJ, final Function<KPermit, JsonObject> supplierJ) {
        /*
         * 字段集
         * - items
         * - qr
         * - mapping
         */
        final JsonObject inputJ = supplierJ.apply(permit);
        final JsonObject qrJ = Ut.valueJObject(inputJ, KName.Rbac.QR);
        // 复制原始记录（创建副本以防止混乱）
        final JsonObject normalizedJ = inputJ.copy();
        // 维度计算只会存在于 compile 中，此处只做 qr 的处理
        normalizedJ.put(KName.Rbac.QR, this.configureQr(qrJ, requestJ));
        return normalizedJ;
    }


    /*
     * configured 中已经包含了 qr 节点（主节点）
     * 此处构造子节点专用 qr
     * {
     *     "children": {
     *         "code1": children1,
     *         "code2": children2
     *     }
     * }
     * 最终构造的结构如：
     * {
     *    ...Dm,
     *    children: {
     *        code1: child1Dm,
     *        code2: child2Dm
     *    }
     * }
     * children 为前后端统一的接口
     */
    private JsonObject valueChildren(final KPermit input, final JsonArray children, final Function<KPermit, JsonObject> supplierJ) {
        final JsonArray childrenA = Ut.valueJArray(children);
        final JsonObject childrenJ = new JsonObject();
        Ut.itJArray(childrenA).forEach(childJ -> {
            // code -> permit ( Child )
            final String code = Ut.valueString(childJ, KName.CODE);
            if (Ut.isNotNil(code)) {
                final KPermit child = input.child(code);
                /*
                 * 数据结构重新处理
                 * {
                 *     "dm": {
                 *         "qr": {},
                 *         "surface": {}
                 *     },
                 *     "qr": {},
                 *     "surface": {}
                 * }
                 */
                final JsonObject childUi = this.valueConfig(child, childJ, supplierJ);
                final JsonObject childDm = this.valueConfig(child, childJ, KPermit::dmJ);
                childUi.put(KName.Rbac.DM, childDm);
                childrenJ.put(code, childUi);
            }
        });
        return childrenJ;
    }
}
