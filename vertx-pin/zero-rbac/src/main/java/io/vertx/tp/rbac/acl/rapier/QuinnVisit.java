package io.vertx.tp.rbac.acl.rapier;

import cn.vertxup.rbac.domain.tables.pojos.SResource;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScOwner;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QuinnVisit implements Quinn {

    @Override
    @SuppressWarnings("unchecked")
    public <T> Future<T> saveAsync(final SResource resource, final ScOwner owner, final JsonObject data) {
        final JsonArray visitantData = Ut.valueJArray(data, KName.Rbac.VISITANT);
        final JsonObject viewData = data.copy();
        // Set visitant = true when Data Not Empty
        viewData.put(KName.Rbac.VISITANT, Ut.notNil(visitantData));
        return Quinn.view().<SView>saveAsync(resource.getKey(), owner, viewData).compose(view -> {
            final Boolean virtual = Objects.isNull(resource.getVirtual()) ? Boolean.FALSE : resource.getVirtual();
            if (!virtual) {
                return Ux.futureJ(view);
            }
            // 资源访问者保存流程
            return Ux.futureJ();
        }).compose(json -> Ux.future((T) json));
    }
}
