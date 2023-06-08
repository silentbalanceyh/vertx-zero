package io.vertx.mod.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.atom.ScOwner;
import io.vertx.mod.rbac.cv.em.OwnerType;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QuinnViews implements Quinn {
    @Override
    public <T> Future<T> saveAsync(final String resourceId, final ScOwner owner, final JsonObject data) {
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Future<T> fetchAsync(final String resourceId, final ScOwner owner) {
        final Set<String> roles = owner.roles();
        if (roles.isEmpty()) {
            return Ux.future((T) new ArrayList<>());
        }
        final JsonObject condition = Quinn.viewQr(resourceId, owner);
        // OWNER = ?, OWNER_TYPE = ? --- ownerType 固定
        condition.put(KName.OWNER + ",i", Ut.toJArray(roles));
        condition.put(KName.OWNER_TYPE, OwnerType.ROLE.name());
        return Ux.Jooq.on(SViewDao.class).fetchAsync(condition)
            .compose(list -> Ux.future((T) list));
    }
}
