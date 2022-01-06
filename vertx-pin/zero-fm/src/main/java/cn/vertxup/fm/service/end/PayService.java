package cn.vertxup.fm.service.end;

import cn.vertxup.fm.domain.tables.daos.FDebtDao;
import cn.vertxup.fm.domain.tables.daos.FPaymentDao;
import cn.vertxup.fm.domain.tables.daos.FPaymentItemDao;
import cn.vertxup.fm.domain.tables.pojos.FDebt;
import cn.vertxup.fm.domain.tables.pojos.FPaymentItem;
import cn.vertxup.fm.service.business.FillStub;
import cn.vertxup.fm.service.business.IndentStub;
import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.FmCv;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PayService implements PayStub {

    @Inject
    private IndentStub indentStub;

    @Inject
    private FillStub fillStub;

    @Override
    public Future<JsonArray> createAsync(final JsonObject data) {
        /*
         * payment data structure
         * [
         *      settlementId: required,
         *      amount: required
         * ]
         */
        final JsonArray endKeys = data.getJsonArray("finished");

        return this.indentStub.payAsync(data)
            .compose(Ux.Jooq.on(FPaymentDao.class)::insertAsync)
            .compose(payment -> {
                final JsonArray paymentArr = data.getJsonArray(FmCv.ID.PAYMENT, new JsonArray());
                final List<FPaymentItem> payments = Ux.fromJson(paymentArr, FPaymentItem.class);
                this.fillStub.payment(payment, payments);
                return Ux.Jooq.on(FPaymentItemDao.class).insertAsync(payments);
            })
            .compose(payments -> this.forwardDebt(payments, Ut.toSet(endKeys)));
    }

    private Future<JsonArray> forwardDebt(final List<FPaymentItem> payments, final Set<String> endKeys) {
        return this.fetchDebt(payments).compose(debts -> {
            final List<FDebt> qUpdate = new ArrayList<>();
            debts.forEach(debt -> {
                if (endKeys.contains(debt.getSettlementId())) {
                    debt.setFinished(Boolean.TRUE);
                    debt.setFinishedAt(LocalDateTime.now());
                    qUpdate.add(debt);
                }
            });
            return Ux.Jooq.on(FDebtDao.class).updateAsync(qUpdate)
                .compose(nil -> Ux.futureA(payments));
        });
    }

    @Override
    public Future<Boolean> deleteByItem(final String itemKey) {
        // 1. Fetch payment items first
        return this.fetchAllItems(itemKey).compose(items -> {
            /*
             * Step:
             * 2. updateDebt
             *    deleteCascade
             */
            final List<Future<Boolean>> futures = new ArrayList<>();
            futures.add(this.revertDebt(items));
            futures.add(this.deleteCascade(items));
            return Ux.thenCombineT(futures)
                .compose(nil -> Ux.futureT());
        });
    }

    private Future<List<FPaymentItem>> fetchAllItems(final String itemKey) {
        final UxJooq jq = Ux.Jooq.on(FPaymentItemDao.class);
        return jq.<FPaymentItem>fetchByIdAsync(itemKey).compose(item -> {
            if (Objects.isNull(item) || Objects.isNull(item.getPaymentId())) {
                /*
                 * 1. item is not exist
                 * 2. item -> paymentId = null
                 */
                return Ux.futureL();
            }
            return jq.fetchAsync("paymentId", item.getPaymentId());
        });
    }

    private Future<Boolean> revertDebt(final List<FPaymentItem> items) {
        return this.fetchDebt(items).compose(debts -> {
            debts.forEach(debt -> {
                debt.setFinished(Boolean.FALSE);
                debt.setFinishedAt(null);
            });
            return Ux.Jooq.on(FDebtDao.class).updateAsync(debts)
                .compose(nil -> Ux.futureT());
        });
    }

    private Future<List<FDebt>> fetchDebt(final List<FPaymentItem> items) {
        final Set<String> settlementIds = items.stream()
            .map(FPaymentItem::getSettlementId)
            .filter(Ut::notNil)
            .collect(Collectors.toSet());
        final UxJooq jq = Ux.Jooq.on(FDebtDao.class);
        return jq.fetchInAsync("settlementId", Ut.toJArray(settlementIds));
    }

    private Future<Boolean> deleteCascade(final List<FPaymentItem> items) {
        final String paymentId = items
            .stream()
            .map(FPaymentItem::getPaymentId)
            .findFirst().orElse(null);
        Objects.requireNonNull(paymentId);
        final JsonObject condition = new JsonObject();
        condition.put("paymentId", paymentId);
        // Delete all items
        return Ux.Jooq.on(FPaymentItemDao.class).deleteAsync(condition)
            // Delete the major payment ticket
            .compose(nil -> Ux.Jooq.on(FPaymentDao.class).deleteByIdAsync(paymentId));
    }
}
