package io.vertx.tp.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.pojos.SGroup;
import cn.vertxup.rbac.domain.tables.pojos.SRole;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.horizon.eon.VString;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.up.uca.cache.Cc;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface IdcBinder<T> {

    Cc<String, IdcBinder> CC_BINDER = Cc.openThread();

    public static IdcBinder<SRole> role(final String sigma) {
        return CC_BINDER.pick(() -> new BinderRole(sigma), sigma + VString.SLASH + BinderRole.class.getName());
    }

    public static IdcBinder<SGroup> group(final String sigma) {
        return CC_BINDER.pick(() -> new BinderGroup(sigma), sigma + VString.SLASH + BinderGroup.class.getName());
    }

    Future<JsonArray> bindAsync(List<SUser> users, JsonArray inputData);
}
