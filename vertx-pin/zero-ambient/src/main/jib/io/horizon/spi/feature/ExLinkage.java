package io.horizon.spi.feature;

import cn.vertxup.ambient.domain.tables.daos.XLinkageDao;
import cn.vertxup.ambient.service.linkage.LinkService;
import cn.vertxup.ambient.service.linkage.LinkStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExLinkage implements Linkage {
    @Override
    public Future<JsonArray> link(final JsonArray linkage, final boolean vector) {
        final LinkStub linkStub = Ut.singleton(LinkService.class);
        return linkStub.saving(linkage, vector);
    }

    @Override
    public Future<Boolean> unlink(final JsonObject criteria) {
        return Ux.Jooq.on(XLinkageDao.class).deleteByAsync(criteria);
    }

    @Override
    public Future<JsonArray> fetch(final JsonObject criteria) {
        return Ux.Jooq.on(XLinkageDao.class).fetchJAsync(criteria).compose(Fn.ofJArray(
            KName.SOURCE_DATA,
            KName.TARGET_DATA
        ));
    }
}
