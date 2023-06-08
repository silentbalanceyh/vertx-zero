package cn.vertxup.fm.service.end;

import cn.vertxup.fm.domain.tables.daos.*;
import cn.vertxup.fm.domain.tables.pojos.*;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.fm.cv.FmCv;
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
public class QrService implements QrStub {
    @Override
    public Future<JsonObject> fetchSettlement(final String key) {
        final JsonObject response = new JsonObject();
        return Ux.Jooq.on(FSettlementDao.class)
            .<FSettlement>fetchByIdAsync(key).compose(settlement -> {
                // FSettlement Details Here
                response.mergeIn(Ux.toJson(settlement));
                return this.fetchItems(key, response)
                    .compose(nil -> Ux.future(settlement));
            })
            // Payment fetching for all settlement
            .compose(settlement -> Ux.Jooq.on(FPaymentItemDao.class)
                .fetchJAsync(FmCv.ID.SETTLEMENT_ID, settlement.getKey())
                .compose(payment -> {
                    response.put(FmCv.ID.PAYMENT, payment);
                    return Ux.future(settlement);
                })
            )
            // Debt fetching for all Refund/Debt
            .compose(settlement -> Ux.Jooq.on(FDebtDao.class)
                .<FDebt>fetchOneAsync(FmCv.ID.SETTLEMENT_ID, key).compose(debt -> {
                    response.put(KName.RECORD, Ux.toJson(debt));
                    return Ux.future(response);
                }));
    }

    @Override
    public Future<JsonObject> fetchDebt(final String key) {
        final JsonObject response = new JsonObject();
        return Ux.Jooq.on(FDebtDao.class)
            .<FDebt>fetchByIdAsync(key).compose(debt -> {
                // FSettlement Details Here
                Objects.requireNonNull(debt.getSettlementId());
                response.mergeIn(Ux.toJson(debt));
                return this.fetchItems(debt.getSettlementId(), response)
                    .compose(nil -> Ux.future(debt));
            }).compose(debt -> this.fetchPayment(debt.getSettlementId(), false))
            .compose(payments -> {
                response.put(FmCv.ID.PAYMENT, payments);
                return Ux.future(response);
            });
    }

    @Override
    public Future<ConcurrentMap<String, FDebt>> fetchDebtMap(final Set<String> settlementId) {
        return Ux.Jooq.on(FDebtDao.class).<FDebt>fetchInAsync(FmCv.ID.SETTLEMENT_ID, Ut.toJArray(settlementId))
            .compose(dataA -> Ux.future(Ut.elementMap(dataA, FDebt::getSettlementId)));
    }

    private Future<Boolean> fetchItems(final String settlementId, final JsonObject response) {
        return Ux.Jooq.on(FSettlementItemDao.class)
            .<FSettlementItem>fetchAsync(FmCv.ID.SETTLEMENT_ID, settlementId).compose(items -> {
                // FSettlement Items
                response.put(KName.ITEMS, Ux.toJson(items));
                return Ux.futureT();
            });
    }

    @Override
    public Future<JsonArray> fetchPayment(final String settlementId, final boolean tree) {
        return Ux.Jooq.on(FPaymentItemDao.class)
            .<FPaymentItem>fetchAsync(FmCv.ID.SETTLEMENT_ID, settlementId).compose(items -> {
                if (tree) {
                    // Payment Information
                    final Set<String> paymentIds = items.stream()
                        .map(FPaymentItem::getPaymentId)
                        .filter(Ut::isNotNil)
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
                } else {
                    return Ux.futureA(items);
                }
            });
    }
}
