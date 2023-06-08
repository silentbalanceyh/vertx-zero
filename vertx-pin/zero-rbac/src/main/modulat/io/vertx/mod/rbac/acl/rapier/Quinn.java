package io.vertx.mod.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.horizon.exception.web._501NotSupportException;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.atom.ScOwner;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Quinn {

    Cc<String, Quinn> CC_QUINN = Cc.openThread();

    static Quinn vivid() {
        return CC_QUINN.pick(QuinnVivid::new, QuinnVivid.class.getName());
    }

    static Quinn view() {
        return CC_QUINN.pick(QuinnView::new, QuinnView.class.getName());
    }

    static Quinn views() {
        return CC_QUINN.pick(QuinnViews::new, QuinnViews.class.getName());
    }

    static Quinn visit() {
        return CC_QUINN.pick(QuinnVisit::new, QuinnVisit.class.getName());
    }

    /*
     * 通用方法，QuinnView / QuinnViews 中都会使用的统一方法，直接根据
     * resourceId, owner ( owner, ownerType, view, position ) 构造最终数据结构
     * {
     *     "resourceId":        "resource id",
     *     "name":              "view name",
     *     "position":          "view position"
     * }
     *
     * RESOURCE_ID = ? AND NAME = ? AND POSITION = ? AND OWNER = ? AND OWNER_TYPE = ?
     */
    static JsonObject viewQr(final String resourceId, final ScOwner owner) {
        final JsonObject condition = Ux.whereAnd();
        // RESOURCE_ID = ?
        condition.put(KName.RESOURCE_ID, resourceId);
        // NAME = ?, POSITION = ?
        condition.put(KName.NAME, owner.view());
        condition.put(KName.POSITION, owner.position());
        return condition;
    }

    // ----------------------- SResource 方法 ---------------------------
    /*
     * 直接使用 SResource 方法的核心逻辑和 resource key 的逻辑区别在于，直接使用 SResource
     * 方法才会在执行过程中判断 resource ( virtual = true ) 执行后续 syntax 资源访问逻辑
     * 而 resource key 系列的方法几乎不会触碰到 SResource 对应的表结构，只是单纯使用 SView
     * 的简单逻辑，这种场景中不会触碰到资源访问者这一层
     */
    default <T> Future<T> fetchAsync(final SResource resource, final ScOwner owner) {
        Objects.requireNonNull(resource);
        return this.fetchAsync(resource.getKey(), owner);
    }

    default <T> Future<T> saveAsync(final SResource resource, final ScOwner owner, final JsonObject data) {
        Objects.requireNonNull(resource);
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }


    // ----------------------- resource Key 方法 ---------------------------
    /*
     * 读取和保存的新统一接口
     * 1）针对用户/角色读取唯一视图
     * 2）带有资源访问者的保存流程
     * 新接口中直接替换原始流程生成完整的 DataBound，移除掉原始的 ViewStub / VisitStub 以及 MatrixStub 部分逻辑
     */
    default <T> Future<T> saveAsync(final String resourceId, final ScOwner owner, final JsonObject data) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

    default <T> Future<T> fetchAsync(final String resourceId, final ScOwner owner) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }
}
