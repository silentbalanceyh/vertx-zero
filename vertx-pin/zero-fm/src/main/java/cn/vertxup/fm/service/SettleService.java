package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.daos.FSettlementDao;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FSettlement;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class SettleService implements SettleStub {
    @Override
    public Future<List<FSettlement>> fetchByItems(final List<FBillItem> items) {
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.KEY, Ut.toJArray(items.stream()
            .map(FBillItem::getSettlementId)
            .filter(Ut::notNil)
            .collect(Collectors.toSet())
        ));
        return Ux.Jooq.on(FSettlementDao.class).fetchAsync(condition);
    }
}
