package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.daos.*;
import cn.vertxup.fm.domain.tables.pojos.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

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

    @Override
    public Future<JsonObject> fetchDebt(final String key) {
        final JsonObject response = new JsonObject();
        return Ux.Jooq.on(FDebtDao.class)
            .<FDebt>fetchByIdAsync(key).compose(debt -> {
                // FSettlement Details Here
                Objects.requireNonNull(debt.getSettlementId());
                response.mergeIn(Ux.toJson(debt));
                return Ux.Jooq.on(FSettlementItemDao.class)
                    .<FSettlementItem>fetchAsync("settlementId", debt.getSettlementId()).compose(items -> {
                        // FSettlement Items
                        response.put(KName.ITEMS, Ux.toJson(items));
                        return Ux.future(debt);
                    });
            }).compose(debt -> this.fetchPayment(debt.getSettlementId()))
            .compose(payments -> {
                response.put("payment", payments);
                return Ux.future(response);
            });
    }

    @Override
    public Future<JsonArray> fetchPayment(final String settlementId) {
        return Ux.Jooq.on(FPaymentItemDao.class)
            .<FPaymentItem>fetchAsync("settlementId", settlementId).compose(items -> {
                // Payment Information
                final Set<String> paymentIds = items.stream()
                    .map(FPaymentItem::getPaymentId)
                    .filter(Ut::notNil)
                    .collect(Collectors.toSet());
                // List<Payment>
                return Ux.Jooq.on(FPaymentDao.class)
                    .<FPayment>fetchInAsync(KName.KEY, Ut.toJArray(paymentIds)).compose(payment -> {
                        final ConcurrentMap<String, List<FPaymentItem>> grouped =
                            Ut.elementGroup(items, FPaymentItem::getPaymentId, item -> item);
                        final JsonArray data = Ux.toJson(payment);
                        Ut.itJArray(data).forEach(item -> {
                            final String payKey = item.getString(KName.KEY);
                            if (grouped.containsKey(payKey)) {
                                final List<FPaymentItem> refs = grouped.getOrDefault(payKey, new ArrayList<>());
                                item.put(KName.ITEMS, Ux.toJson(refs));
                            } else {
                                item.put(KName.ITEMS, new JsonArray());
                            }
                        });
                        return Ux.future(data);
                    });
            });
    }
}
