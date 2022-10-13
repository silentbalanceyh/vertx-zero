package io.vertx.tp.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.daos.SVisitantDao;
import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.feature.Confine;
import io.vertx.tp.optic.secure.ConfineBuiltIn;
import io.vertx.tp.rbac.atom.acl.AclData;
import io.vertx.up.commune.secure.Acl;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KValue;
import io.vertx.up.eon.em.run.ActTime;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SyntaxAop {
    static final Cc<String, Confine> CC_FINITY = Cc.openThread();

    private static Future<JsonObject> normInput(final JsonObject bodyData, final JsonObject matrixJ, final JsonObject headerJ) {
        /*
         * Read configuration of `seeker` here
         * 1) Read syntax `BEFORE/AFTER` to match
         * 2）To avoid missing acl configuration information, the default phase is `BEFORE`
         * 3）Input and Define must be matched
         *
         * BEFORE / AFTER 已经在上层判断过了，所以此处直接调用，不执行第二次资源访问者的判断
         **/
        final JsonObject seeker = Ut.valueJObject(matrixJ, KName.SEEKER);
        final JsonObject syntaxJ = Ut.valueJObject(seeker, KName.SYNTAX);
        /*
         * syntax 语法部分的基础解析，抛开 phase 不处理，只执行剩余部分的处理
         * data 是参数模板，最终处理结果根据 data 执行筛选一层，不筛选就整体构造
         * request = {
         *
         * }
         * syntax = {
         *
         * }
         */
        final Class<?> confineCls = Ut.valueCI(syntaxJ, KName.SELECTOR, Confine.class, ConfineBuiltIn.class);


        /*
         * 请求参数构造
         * 1）第一部分直接来自请求的数据
         * 2）从 matrix 中提取 view 相关数据（可找到视图的视图数据）
         *    resourceId = view -> resourceId
         *    sigma      = view -> sigma
         *    language   = view -> language
         *    viewId     = view -> key
         * 3）从 header 中提取 header 相关数据
         *    appId      = X-App-Id
         *    appKey     = X-App-Key
         *    sigma      = X-Sigma
         *    language   = X-Lang
         *    tenantId   = X-Tenant-Id
         */
        final JsonObject requestJ = new JsonObject();
        requestJ.mergeIn(bodyData, false);
        requestJ.mergeIn(headerJ, false);

        final JsonObject viewData = Ut.valueJObject(matrixJ, KName.VIEW);
        requestJ.put(KName.RESOURCE_ID, viewData.getString(KName.RESOURCE_ID));
        requestJ.put(KName.VIEW_ID, viewData.getString(KName.KEY));
        requestJ.put(KName.VIEW, viewData.getString(KName.NAME, KValue.View.VIEW_DEFAULT));
        requestJ.put(KName.POSITION, viewData.getString(KName.POSITION, KValue.View.POSITION_DEFAULT));

        final Confine confine = CC_FINITY.pick(() -> Ut.instance(confineCls), confineCls.getName());
        return confine.restrict(requestJ, syntaxJ);
    }

    /*
     * 计算 `syntax` 用于生成条件，然后系统会根据条件读取 visitant 对象
     * matrix 数据结构
     * {
     *     "seeker": 访问者配置{
     *         "syntax": {
     *             Syntax 结构，对应资源字段 SEEK_SYNTAX
     *         }
     *     },
     *     "view":   视图数据 {
     *         "resourceId": "访问资源ID",
     *         "sigma":      "统一标识符",
     *         "language":   "语言信息",
     *         "key":        "视图ID",
     *         "position":   "位置数据（新版加入）"
     *     }
     * }
     *
     * syntax 数据结构
     * {
     *     "phase": "ActTime, BEFORE / AFTER",
     *     "data": {
     *         可执行表达式解析专用的数据输入部分
     *     },
     *     "metadata": {
     *         "wheel": "资源访问者筛选组件，根据输入组件筛选相关信息"
     *     }
     * }
     */
    Future<Acl> aclBefore(final JsonObject bodyData, final JsonObject matrixJ, final JsonObject headerJ) {
        return normInput(bodyData, matrixJ, headerJ)
            .compose(qr -> Ux.Jooq.on(SVisitantDao.class).<SVisitant>fetchOneAsync(qr))
            .compose(visitant -> this.normOutput(visitant, matrixJ, ActTime.BEFORE));
    }

    Future<Acl> aclAfter(final JsonObject bodyData, final JsonObject matrixJ, final JsonObject headerJ) {
        return normInput(bodyData, matrixJ, headerJ)
            .compose(qr -> Ux.Jooq.on(SVisitantDao.class).<SVisitant>fetchOneAsync(qr))
            .compose(visitant -> this.normOutput(visitant, matrixJ, ActTime.AFTER));
    }

    private void normMatrix(final SVisitant visitant, final JsonObject matrixJ) {

    }

    private Future<Acl> normOutput(final SVisitant visitant, final JsonObject matrixJ, final ActTime phase) {
        if (Objects.isNull(visitant)) {
            return Ux.future();
        }
        final ActTime configured = Ut.toEnum(visitant::getPhase, ActTime.class, null);
        if (configured != phase) {
            return Ux.future();
        }
        this.normMatrix(visitant, matrixJ);
        /*
         * vQr
         * {
         *     "rows": {
         *         "field1": []
         *     },
         *     "criteria": {
         *         Qr Engine 语法
         *     },
         *     "projection": [
         *         选择可用列处理
         *     ]
         * }
         */
        final JsonObject vQr = new JsonObject();
        vQr.put(KName.Rbac.ROWS, Ut.toJObject(visitant.getDmRow()));
        vQr.put(KName.Rbac.CRITERIA, Ut.toJObject(visitant.getDmQr()));
        vQr.put(KName.Rbac.PROJECTION, Ut.toJArray(visitant.getDmColumn()));
        return Ux.future(new AclData(visitant).config(vQr));
    }
}
