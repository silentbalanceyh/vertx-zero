package io.vertx.tp.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScOwner;
import io.vertx.up.unity.Ux;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QuinnVisit implements Quinn {
    @Override
    public <T> Future<T> saveAsync(final SResource resource, final ScOwner owner, final JsonObject data) {
        return Quinn.view().<SView>saveAsync(resource.getKey(), owner, data).compose(view -> {
            final Boolean virtual = Objects.isNull(resource.getVirtual()) ? Boolean.FALSE : resource.getVirtual();
            if (virtual) {
                // 资源访问者保存流程
            }
            return Ux.future();
        });
    }
}
