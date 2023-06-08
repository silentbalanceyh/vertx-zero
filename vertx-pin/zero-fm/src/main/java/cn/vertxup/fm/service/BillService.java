package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.daos.*;
import cn.vertxup.fm.domain.tables.pojos.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BillService implements BillStub {
    @Inject
    private transient BookStub bookStub;

    @Override
    public Future<List<FPaymentItem>> fetchPayments(final List<FSettlement> settlements) {
        final Set<String> settlementIds = Ut.valueSetString(settlements, FSettlement::getKey);
        final JsonObject condition = Ux.whereAnd();
        condition.put("settlementId,i", Ut.toJArray(settlementIds));
        return Ux.Jooq.on(FPaymentItemDao.class).fetchAsync(condition);
    }

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
        if (bills.isEmpty()) {
            return Ux.future(new ArrayList<>());
        } else {
            final JsonObject condition = Ux.whereAnd();
            condition.put("billId,i", Ut.toJArray(bills.stream().map(FBill::getKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet())));
            return Ux.Jooq.on(FBillItemDao.class).fetchAsync(condition);
        }
    }

    @Override
    public Future<List<FSettlement>> fetchSettlements(final List<FBillItem> items) {
        if (items.isEmpty()) {
            return Ux.future(new ArrayList<>());
        } else {
            final JsonObject condition = Ux.whereAnd();
            condition.put(KName.KEY, Ut.toJArray(items.stream()
                .map(FBillItem::getSettlementId)
                .filter(Ut::isNotNil)
                .collect(Collectors.toSet())
            ));
            return Ux.Jooq.on(FSettlementDao.class).fetchAsync(condition);
        }
    }

    @Override
    public Future<List<FSettlementItem>> fetchBySettlements(final List<FSettlement> settlements) {
        final Set<String> settlementIds = Ut.valueSetString(settlements, FSettlement::getKey);
        final JsonObject condition = Ux.whereAnd();
        condition.put("settlementId,i", Ut.toJArray(settlementIds));
        return Ux.Jooq.on(FSettlementItemDao.class).fetchAsync(condition);
    }
}
