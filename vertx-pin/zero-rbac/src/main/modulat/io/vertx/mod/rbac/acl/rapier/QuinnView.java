package io.vertx.mod.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.atom.ScOwner;
import io.vertx.mod.rbac.cv.AuthMsg;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.log.DevEnv;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import static io.vertx.mod.rbac.refine.Sc.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QuinnView implements Quinn {
    @Override
    @SuppressWarnings("unchecked")
    public <T> Future<T> saveAsync(final String resourceId, final ScOwner owner, final JsonObject viewData) {
        // 1. 读取该用户视图
        return this.<SView>fetchAsync(resourceId, owner).compose(queried -> {
            final SView myView = this.initialize(queried, resourceId, owner, viewData);
            this.updateData(myView, viewData);
            if (Objects.isNull(queried)) {
                return Ux.Jooq.on(SViewDao.class).insertAsync(myView);
            } else {
                return Ux.Jooq.on(SViewDao.class).updateAsync(myView);
            }
        }).compose(view -> Ux.future((T) view));
    }

    // ----------------------------- 私有方法 「写」---------------------------
    private SView initialize(final SView found, final String resourceId, final ScOwner owner, final JsonObject viewData) {
        if (Objects.isNull(found)) {
            // 新创建一个视图
            final JsonObject qrData = Quinn.viewQr(resourceId, owner);
            qrData.mergeIn(viewData);
            final SView inserted = Ut.deserialize(qrData, SView.class);
            inserted.setKey(UUID.randomUUID().toString());
            inserted.setActive(Boolean.TRUE);

            // 此处没有 owner / ownerType（新建时需跟上）
            inserted.setOwner(owner.owner());
            inserted.setOwnerType(owner.type().name());

            // 创建专用 auditor
            inserted.setCreatedAt(LocalDateTime.now());
            inserted.setCreatedBy(Ut.valueString(viewData, KName.UPDATED_BY));
            return inserted;
        } else {
            // 更新已有的视图
            return found;
        }
    }

    private void updateData(final SView view, final JsonObject viewData) {
        // projection
        if (viewData.containsKey(Ir.KEY_PROJECTION)) {
            view.setProjection(Ut.valueJArray(viewData, Ir.KEY_PROJECTION).encode());
        }
        // rows
        if (viewData.containsKey(KName.Rbac.ROWS)) {
            view.setRows(Ut.valueJObject(viewData, KName.Rbac.ROWS).encode());
        }
        // criteria
        if (viewData.containsKey(Ir.KEY_CRITERIA)) {
            view.setCriteria(Ut.valueJObject(viewData, Ir.KEY_CRITERIA).encode());
        } else {
            // 只有查询条件存在清空
            view.setCriteria(new JsonObject().encode());
        }
        /* Auditor Information */
        view.setUpdatedAt(LocalDateTime.now());
        view.setUpdatedBy(Ut.valueString(viewData, KName.UPDATED_BY));
    }

    /*
     * 提取角色视图 / 用户视图
     * 用户级：
     * 1）owner:             user id
     * 2）resourceId
     * 3）view:              DEFAULT
     * 4）position:          DEFAULT
     * 角色级（支持多）：
     * 1）owner:             role id
     * 2）resourceId
     * 3）view:              DEFAULT
     * 4）position:          DEFAULT
     */
    @Override
    public <T> Future<T> fetchAsync(final String resourceId, final ScOwner owner) {
        final JsonObject condition = Quinn.viewQr(resourceId, owner);
        // OWNER = ?, OWNER_TYPE = ? --- ownerType 从 ScOwner 中提取
        condition.put(KName.OWNER, owner.owner());
        condition.put(KName.OWNER_TYPE, owner.type().name());
        if (DevEnv.devAuthorized()) {
            LOG.Resource.info(this.getClass(), AuthMsg.VIEW_PROCESS, "fetchAsync", condition.encode());
        }
        return Ux.Jooq.on(SViewDao.class).fetchOneAsync(condition);
    }
}
