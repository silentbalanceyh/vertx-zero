package io.vertx.tp.battery.uca.dock;

import cn.vertxup.battery.domain.tables.daos.BBagDao;
import cn.vertxup.battery.domain.tables.pojos.BBag;
import io.horizon.eon.em.cloud.TypeBy;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.tp.ke.cv.em.TypeBag;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import java.util.Set;

import static io.vertx.tp.battery.refine.Bk.LOG;

class ArkBag extends AbstractArk {
    @Override
    public Future<ClusterSerializable> modularize(final String appId, final TypeBy by) {
        final Set<TypeBag> typeSet = Set.of(TypeBag.EXTENSION, TypeBag.COMMERCE, TypeBag.FOUNDATION);
        final JsonObject condition = this.buildQr(appId, typeSet, by);
        condition.put(KName.ENTRY, Boolean.TRUE);
        LOG.Spi.info(this.getClass(), "Modulat condition = {0}", condition.encode());
        return Ux.Jooq.on(BBagDao.class).<BBag>fetchAsync(condition)
            // JsonArray -> ClusterSerializable
            .compose(Ux::futureA)
            .compose(Ux::future);
    }
}
