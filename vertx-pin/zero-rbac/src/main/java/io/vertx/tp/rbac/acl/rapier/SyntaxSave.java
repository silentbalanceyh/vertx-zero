package io.vertx.tp.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScOwner;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SyntaxSave {
    /* 根据资源和拥有者执行视图数据的同步 */
    Future<JsonObject> syncAsync(final SResource resource, final ScOwner owner, final JsonObject viewData) {
        final Boolean virtual = Objects.nonNull(resource.getVirtual()) ? resource.getVirtual() : Boolean.FALSE;
        if (virtual) {
            // 更新资源访问者
            return this.syncVisitant(resource, owner, viewData);
        } else {
            // 更新视图
            return this.syncView(resource, owner, viewData);
        }
    }

    private Future<JsonObject> syncVisitant(final SResource resource, final ScOwner owner, final JsonObject viewData) {

        return Ux.futureJ();
    }

    private Future<JsonObject> syncView(final SResource resource, final ScOwner owner, final JsonObject viewData) {

        return Ux.futureJ();
    }
}
