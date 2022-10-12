package io.vertx.tp.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScOwner;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.cache.Cc;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Quinn {

    Cc<String, Quinn> CC_QUINN = Cc.openThread();

    static Quinn view() {
        return CC_QUINN.pick(QuinnView::new, QuinnView.class.getName());
    }

    static Quinn visit() {
        return CC_QUINN.pick(QuinnVisit::new, QuinnVisit.class.getName());
    }

    /*
     * 读取和保存的新统一接口
     * 1）针对用户/角色读取唯一视图
     * 2）带有资源访问者的保存流程
     * 新接口中直接替换原始流程生成完整的 DataBound，移除掉原始的 ViewStub / VisitStub 以及 MatrixStub 部分逻辑
     */
    Future<JsonObject> saveAsync(String resourceId, ScOwner owner, JsonObject data);

    default <T> Future<T> fetchAsync(final SResource resource, final ScOwner owner) {
        Objects.requireNonNull(resource);
        return this.fetchAsync(resource.getKey(), owner);
    }

    default <T> Future<T> fetchAsync(final String resourceId, final ScOwner owner) {
        return Fn.error(_501NotSupportException.class, this.getClass());
    }
}
