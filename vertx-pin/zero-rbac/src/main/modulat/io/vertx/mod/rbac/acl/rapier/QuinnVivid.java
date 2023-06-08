package io.vertx.mod.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.atom.ScOwner;
import io.vertx.mod.rbac.cv.em.OwnerType;
import io.vertx.up.commune.secure.DataBound;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QuinnVivid implements Quinn {
    @Override
    @SuppressWarnings("unchecked")
    public <T> Future<T> saveAsync(final String resourceId, final ScOwner owner, final JsonObject viewData) {
        // 1. 读取该用户视图
        return Quinn.view().<SView>saveAsync(resourceId, owner, viewData).compose(upsert -> {
            /* Response Building */
            final JsonObject cached = new JsonObject();
            cached.put(Ir.KEY_PROJECTION, Ut.toJArray(upsert.getProjection()));
            cached.put(Ir.KEY_CRITERIA, Ut.toJObject(upsert.getCriteria()));
            cached.put(KName.Rbac.ROWS, Ut.toJObject(upsert.getRows()));
            return Ux.future((T) cached);
        });
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
        return Quinn.view().<SView>fetchAsync(resourceId, owner).compose(view -> {
            if (Objects.isNull(view) && OwnerType.USER == owner.type()) {
                // 2. 二次读取，提取绑定的角色视图
                return Quinn.views().fetchAsync(resourceId, owner);
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
}
