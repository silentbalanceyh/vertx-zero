package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.daos.FDebtDao;
import cn.vertxup.fm.domain.tables.daos.FSettlementDao;
import cn.vertxup.fm.domain.tables.daos.FSettlementItemDao;
import cn.vertxup.fm.domain.tables.pojos.FDebt;
import cn.vertxup.fm.domain.tables.pojos.FSettlement;
import cn.vertxup.fm.domain.tables.pojos.FSettlementItem;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class EndService implements EndStub {
    @Override
    public Future<JsonObject> fetchSettlement(final String key) {
        final JsonObject response = new JsonObject();
        return Ux.Jooq.on(FSettlementDao.class)
            .<FSettlement>fetchByIdAsync(key).compose(settlement -> {
                // FSettlement Details Here
                response.mergeIn(Ux.toJson(settlement));
                return Ux.Jooq.on(FSettlementItemDao.class)
                    .<FSettlementItem>fetchAsync("settlementId", key).compose(items -> {
                        // FSettlement Items
                        response.put(KName.ITEMS, Ux.toJson(items));
                        return Ux.future(settlement);
                    });
            }).compose(settlement -> {
                // Fetch Debt
                return Ux.Jooq.on(FDebtDao.class)
                    .<FDebt>fetchOneAsync("settlementId", key).compose(debt -> {
                        response.put(KName.RECORD, Ux.toJson(debt));
                        return Ux.future(response);
                    });
            });
    }
}
