package cn.vertxup.fm.service.end;

import cn.vertxup.fm.domain.tables.daos.FDebtDao;
import cn.vertxup.fm.domain.tables.daos.FPaymentDao;
import cn.vertxup.fm.domain.tables.daos.FPaymentItemDao;
import cn.vertxup.fm.domain.tables.daos.FSettlementDao;
import cn.vertxup.fm.domain.tables.pojos.FDebt;
import cn.vertxup.fm.domain.tables.pojos.FPaymentItem;
import cn.vertxup.fm.domain.tables.pojos.FSettlement;
import cn.vertxup.fm.service.business.FillStub;
import cn.vertxup.fm.service.business.IndentStub;
import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.FmCv;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
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
    public Future<JsonObject> createAsync(final JsonObject data) {
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
                return this.savePayment(endKeys, payments);
            })
            .compose(payments -> this.forwardDebt(payments, Ut.toSet(endKeys)));
    }

    private Future<List<FPaymentItem>> savePayment(final JsonArray endKeys, final List<FPaymentItem> payments) {
        final UxJooq jq = Ux.Jooq.on(FPaymentItemDao.class);
        return jq.<FPaymentItem>fetchInAsync(FmCv.ID.SETTLEMENT_ID, endKeys).compose(original -> {
            final ConcurrentMap<ChangeFlag, List<FPaymentItem>> compared =
                Ux.compare(original, payments, FPaymentItem::getSerial);
            /*return jq.insertAsync(compared.get(ChangeFlag.ADD)).compose(inserted -> {
                // 返回合并值
                final List<FPaymentItem> ignored = compared.get(ChangeFlag.UPDATE);
                ignored.addAll(inserted);
                return Ux.future(ignored);
            });*/
            /*
            * Fix：https://e.gitee.com/wei-code/issues/table?issue=I60392
            * 不进行更新数据的操作
            * */
            return jq.insertAsync(compared.get(ChangeFlag.ADD));
        });
    }

    private Future<JsonObject> forwardDebt(final List<FPaymentItem> payments, final Set<String> endKeys) {
        return this.fetchDebt(payments).compose(debts -> {
            final List<FDebt> qUpdate = new ArrayList<>();
            /*
             * 针对 PaymentItem 按结算总金额进行分组
             * settlementId = List<FPaymentItem>
             * 1）Debt 中的 debtId 和 settlementId 的对比关系是1:1
             * 2）根据 Debt 金额执行计算
             * -- 金额为负：退款
             * -- 金额为正：应收
             * 3）所有 FPaymentItem 中的金额之和 >= 退款/应收金额绝对值：finished = true，反之 false
             */
            final ConcurrentMap<String, BigDecimal> payedMap = new ConcurrentHashMap<>();
            payments.forEach(payment -> {
                final BigDecimal amount;
                if (payedMap.containsKey(payment.getSettlementId())) {
                    amount = payedMap.get(payment.getSettlementId());
                } else {
                    amount = new BigDecimal(0);
                }
                // 计算和
                BigDecimal sum = Optional.ofNullable(payment.getAmount()).orElse(new BigDecimal(0));
                sum = sum.add(amount);
                payedMap.put(payment.getSettlementId(), sum);
            });
            final List<FDebt> processed = debts.stream()
                .filter(debt -> endKeys.contains(debt.getSettlementId()))
                .toList();
            processed.forEach(debt -> {
                final BigDecimal amount = Optional.ofNullable(debt.getAmount()).orElse(new BigDecimal(0));
                final BigDecimal payed = payedMap.getOrDefault(debt.getSettlementId(), new BigDecimal(0));
                if (Math.abs(amount.doubleValue()) <= payed.doubleValue()) {
                    debt.setFinished(Boolean.TRUE);
                    debt.setFinishedAt(LocalDateTime.now());
                    qUpdate.add(debt);
                }
            });
            return Ux.Jooq.on(FDebtDao.class).updateAsync(qUpdate).compose(this::syncSettlement).compose(nil -> {
                final JsonObject response = new JsonObject();
                processed.forEach(debt -> {
                    final JsonObject debtJ = Ux.toJson(debt);
                    debtJ.put(FmCv.ID.PAYMENT, Ux.toJson(payments));
                    response.put(debt.getKey(), debtJ);
                });
                return Ux.future(response);
            });
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
            return Fn.combineT(futures)
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

    // 结算记录中需查看应收/退款，应收退款完成时，结算也完成，反之结算也未完成
    private Future<List<FDebt>> syncSettlement(final List<FDebt> debts) {
        final Set<String> keys = debts.stream().map(FDebt::getSettlementId).collect(Collectors.toSet());
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        final UxJooq jq = Ux.Jooq.on(FSettlementDao.class);
        return jq.<FSettlement>fetchAsync(condition).compose(settlements -> {
            final ConcurrentMap<String, FDebt> debtMap = Ut.elementMap(debts, FDebt::getSettlementId);
            settlements.forEach(settlement -> {
                final FDebt debt = debtMap.get(settlement.getKey());
                if (debt.getFinished()) {
                    settlement.setFinished(Boolean.TRUE);
                    settlement.setFinishedAt(LocalDateTime.now());
                } else {
                    settlement.setFinished(Boolean.FALSE);
                    settlement.setFinishedAt(null);
                }
            });
            return jq.updateAsync(settlements).compose(nil -> Ux.future(debts));
        });
    }

    private Future<Boolean> revertDebt(final List<FPaymentItem> items) {
        return this.fetchDebt(items).compose(debts -> {
            debts.forEach(debt -> {
                debt.setFinished(Boolean.FALSE);
                debt.setFinishedAt(null);
            });
            return Ux.Jooq.on(FDebtDao.class).updateAsync(debts)
                .compose(this::syncSettlement)
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
        return Ux.Jooq.on(FPaymentItemDao.class).deleteByAsync(condition)
            // Delete the major payment ticket
            .compose(nil -> Ux.Jooq.on(FPaymentDao.class).deleteByIdAsync(paymentId));
    }
}
