package io.vertx.tp.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScOwner;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.cv.em.OwnerType;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.secure.DataBound;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Debugger;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QuinnView implements Quinn {
    @Override
    public Future<JsonObject> saveAsync(final String resourceId, final ScOwner owner, final JsonObject viewData) {
        // 1. 读取该用户视图
        return this.fetchView(resourceId, owner).compose(queried -> {
            final SView myView = this.initialize(queried, resourceId, owner, viewData);
            this.updateData(myView, viewData);
            if (Objects.isNull(queried)) {
                return Ux.Jooq.on(SViewDao.class).insertAsync(myView);
            } else {
                return Ux.Jooq.on(SViewDao.class).updateAsync(myView);
            }
        }).compose(upsert -> {
            /* Response Building */
            final JsonObject cached = new JsonObject();
            cached.put(Qr.KEY_PROJECTION, Ut.toJArray(upsert.getProjection()));
            cached.put(Qr.KEY_CRITERIA, Ut.toJObject(upsert.getCriteria()));
            cached.put(KName.Rbac.ROWS, Ut.toJObject(upsert.getRows()));
            return Ux.future(cached);
        });
    }

    // ----------------------------- 私有方法 「写」---------------------------
    private SView initialize(final SView found, final String resourceId, final ScOwner owner, final JsonObject viewData) {
        if (Objects.isNull(found)) {
            // 新创建一个视图
            final JsonObject qrData = this.qrCond(resourceId, owner);
            qrData.mergeIn(viewData);
            final SView inserted = Ut.deserialize(qrData, SView.class);
            inserted.setKey(UUID.randomUUID().toString());
            inserted.setActive(Boolean.TRUE);

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
        if (viewData.containsKey(Qr.KEY_PROJECTION)) {
            view.setProjection(Ut.valueJArray(viewData, Qr.KEY_PROJECTION).encode());
        }
        // rows
        if (viewData.containsKey(KName.Rbac.ROWS)) {
            view.setRows(Ut.valueJObject(viewData, KName.Rbac.ROWS).encode());
        }
        // criteria
        if (viewData.containsKey(Qr.KEY_CRITERIA)) {
            view.setCriteria(Ut.valueJObject(viewData, Qr.KEY_CRITERIA).encode());
        } else {
            // 只有查询条件存在清空
            view.setCriteria(new JsonObject().encode());
        }
        /* Auditor Information */
        view.setUpdatedAt(LocalDateTime.now());
        view.setUpdatedBy(Ut.valueString(viewData, KName.UPDATED_BY));
    }

    /*
     * 返回DataBound，统一原始的调用
     * 1）MatrixService   调用（先用户、后角色）
     * 2）ExColumnApeakMy 调用（只查询用户级）
     *
     * 如果 owner 中绑定了 roles 自动转换成 null 提取模式，此处切换的前提在于是否提取了角色，执行了
     * ScOwner 中的 roles bind 方法，该方法目前只有在原始 MatrixService 替换时会被调用，通常所有为止
     * 的 ScOwner 不分都是平行调用
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Future<T> fetchAsync(final String resourceId, final ScOwner owner) {
        // 1. 直接提取角色视图 / 用户视图
        return this.fetchView(resourceId, owner).compose(view -> {
            if (Objects.isNull(view) && OwnerType.USER == owner.type()) {
                // 2. 二次读取，提取绑定的角色视图
                return this.fetchViews(resourceId, owner);
            } else {
                return Ux.futureL(view);
            }
        }).compose(this::regionBind).compose(bound -> Ux.future((T) bound));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Future<T> fetchAsync(final SResource resource, final ScOwner owner) {
        return this.<DataBound>fetchAsync(resource.getKey(), owner).compose(bound -> {
            final Boolean virtual = resource.getVirtual();
            /*
             * Check whether current resource is virtual resource
             * 1) If true, the resource is virtual resource, there need additional steps
             * to calculated view in future instead of current view stored.
             * 2) If false, the old workflow
             */
            if (Objects.nonNull(virtual) && virtual) {
                final JsonObject seeker = new JsonObject();
                seeker.put(KName.CONFIG, Ut.toJObject(resource.getSeekConfig()));
                seeker.put(KName.SYNTAX, Ut.toJObject(resource.getSeekSyntax()));
                seeker.put(KName.COMPONENT, resource.getSeekComponent());
                /* Store view object into json for future condition building */
                bound.addSeeker(seeker);
            }
            return Ux.future((T) bound);
        });
    }

    // ----------------------------- 私有方法 「读」---------------------------
    /*
     * Data Bound building
     * 1) projection append
     * 2) rows append
     * 3) criteria append
     * 4) attach `visitant` process here
     */
    private Future<DataBound> regionBind(final List<SView> views) {
        final DataBound bound = new DataBound();
        views.forEach(viewRef -> {
            final JsonObject viewData = Ut.serializeJson(viewRef);
            /*
             * Basic view configuration reading
             */
            bound.addProjection(viewRef.getProjection())
                .addRows(viewRef.getRows())
                .addCriteria(viewRef.getCriteria())
                .addView(viewData);
        });
        return Ux.future(bound);
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
    private Future<SView> fetchView(final String resourceId, final ScOwner owner) {
        final JsonObject condition = this.qrCond(resourceId, owner);
        // OWNER = ?, OWNER_TYPE = ? --- ownerType 从 ScOwner 中提取
        condition.put(KName.OWNER, owner.owner());
        condition.put(KName.OWNER_TYPE, owner.type().name());
        if (Debugger.onAuthorizedCache()) {
            Sc.infoResource(this.getClass(), AuthMsg.VIEW_PROCESS, "fetchView", condition.encode());
        }
        return Ux.Jooq.on(SViewDao.class).fetchOneAsync(condition);
    }

    private Future<List<SView>> fetchViews(final String resourceId, final ScOwner owner) {
        final Set<String> roles = owner.roles();
        if (roles.isEmpty()) {
            return Ux.futureL();
        }
        final JsonObject condition = this.qrCond(resourceId, owner);
        // OWNER = ?, OWNER_TYPE = ? --- ownerType 固定
        condition.put(KName.OWNER + ",i", Ut.toJArray(roles));
        condition.put(KName.OWNER_TYPE, OwnerType.ROLE.name());
        return Ux.Jooq.on(SViewDao.class).fetchAsync(condition);
    }

    // RESOURCE_ID = ? AND NAME = ? AND POSITION = ? AND OWNER = ? AND OWNER_TYPE = ?
    private JsonObject qrCond(final String resourceId, final ScOwner owner) {
        final JsonObject condition = Ux.whereAnd();
        // RESOURCE_ID = ?
        condition.put(KName.RESOURCE_ID, resourceId);
        // NAME = ?, POSITION = ?
        condition.put(KName.NAME, owner.view());
        condition.put(KName.POSITION, owner.position());
        return condition;
    }
}
