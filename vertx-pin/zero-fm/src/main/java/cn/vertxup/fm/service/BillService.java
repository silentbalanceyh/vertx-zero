package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.daos.FBillDao;
import cn.vertxup.fm.domain.tables.daos.FBillItemDao;
import cn.vertxup.fm.domain.tables.daos.FSettlementDao;
import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FSettlement;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BillService implements BillStub {
    @Inject
    private transient BookStub bookStub;

    @Override
    public Future<List<FBill>> fetchByOrder(final String orderId) {
        /*
         * Fetch Bill List
         */
        final JsonObject condBill = Ux.whereAnd();
        condBill.put("orderId", orderId);
        return Ux.Jooq.on(FBillDao.class).fetchAsync(condBill);
    }

    @Override
    public Future<List<FBillItem>> fetchByBills(final List<FBill> bills) {
        final JsonObject condition = Ux.whereAnd();
        condition.put("billId,i", Ut.toJArray(bills.stream().map(FBill::getKey)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet())));
        return Ux.Jooq.on(FBillItemDao.class).fetchAsync(condition);
    }

    @Override
    public Future<List<FSettlement>> fetchSettlements(final List<FBillItem> items) {
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.KEY, Ut.toJArray(items.stream()
            .map(FBillItem::getSettlementId)
            .filter(Ut::notNil)
            .collect(Collectors.toSet())
        ));
        return Ux.Jooq.on(FSettlementDao.class).fetchAsync(condition);
    }
}
