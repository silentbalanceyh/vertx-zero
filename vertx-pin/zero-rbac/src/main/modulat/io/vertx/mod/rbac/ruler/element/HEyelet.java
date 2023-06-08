package io.vertx.mod.rbac.ruler.element;

import cn.vertxup.rbac.domain.tables.pojos.SPacket;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.core.shareddata.ClusterSerializable;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HEyelet {

    Cc<String, HEyelet> CCT_HEYELET = Cc.openThread();

    @SuppressWarnings("all")
    static HEyelet instance(Enum type) {
        final Supplier<HEyelet> supplier = __H1H.EYELET.get(type);
        if (Objects.isNull(supplier)) {
            return null;
        }
        return CCT_HEYELET.pick(supplier, type.name());
    }

    Future<ClusterSerializable> ingest(SPacket packet, SView view);
}
