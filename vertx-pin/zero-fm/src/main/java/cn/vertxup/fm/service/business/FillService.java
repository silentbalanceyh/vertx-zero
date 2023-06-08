package cn.vertxup.fm.service.business;

import cn.vertxup.fm.domain.tables.pojos.*;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.fm.cv.FmCv;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FillService implements FillStub {
    /*
     * Bill -> BillItem
     */
    @Override
    public void income(final FBill bill, final List<FBillItem> items) {
        for (int idx = 0; idx < items.size(); idx++) {
            final FBillItem item = items.get(idx);
            final int number = (idx + 1);
            item.setBillId(bill.getKey());
            item.setSerial(bill.getSerial() + "-" + Ut.fromAdjust(number, 2));
            item.setCode(bill.getCode() + "-" + Ut.fromAdjust(number, 2));
            item.setAmountTotal(item.getAmount());
            item.setStatus(FmCv.Status.PENDING);
            item.setIncome(bill.getIncome());
            // auditor
            Ke.umCreated(item, bill);
        }
    }

    @Override
    public void cancel(final FBillItem item, final JsonObject params) {
        item.setActive(Boolean.FALSE);
        item.setStatus(FmCv.Status.INVALID);
        item.setType(FmCv.Type.CANCEL);
        item.setUpdatedAt(LocalDateTime.now());
        item.setUpdatedBy(params.getString(KName.UPDATED_BY));
    }

    /*
     * Bill -> BillItem
     */
    @Override
    public void income(final FBill bill, final FBillItem item) {
        item.setBillId(bill.getKey());
        item.setSerial(bill.getSerial() + "-01");
        item.setCode(bill.getCode() + "-01");
        item.setStatus(FmCv.Status.PENDING);
        item.setIncome(bill.getIncome());
        // price, quanlity, total
        item.setPrice(item.getAmount());
        item.setQuantity(1);
        item.setAmountTotal(item.getAmount());
        Ke.umCreated(item, bill);
    }

    @Override
    public void income(final FBill bill, final FPreAuthorize authorize) {
        if (Objects.nonNull(authorize)) {
            authorize.setBillId(bill.getKey());
            authorize.setSerial(bill.getSerial() + "-A");
            authorize.setCode(bill.getCode() + "-A");
            authorize.setStatus(FmCv.Status.PENDING);
            // active, sigma
            Ke.umCreated(authorize, bill);
        }
    }

    @Override
    public void split(final FBillItem item, final List<FBillItem> items) {
        Objects.requireNonNull(item);
        if (Objects.nonNull(items) && !items.isEmpty()) {
            final int size = items.size();
            item.setActive(Boolean.FALSE);          // Old Disabled
            item.setStatus(FmCv.Status.INVALID);
            for (int idx = 0; idx < size; idx++) {
                final FBillItem split = items.get(idx);
                split.setKey(null);
                split.setBillId(item.getBillId());
                split.setSerial(item.getSerial() + FmCv.SEQ[idx]);
                split.setCode(item.getCode() + FmCv.SEQ[idx]);
                split.setStatus(FmCv.Status.PENDING);
                split.setRelatedId(item.getKey());
                split.setIncome(item.getIncome());
                // active, sigma
                Ke.umCreated(split, item);
                split.setActive(Boolean.TRUE);      // New Enabled
            }
        }
    }

    @Override
    public void revert(final FBillItem item, final FBillItem to) {
        Objects.requireNonNull(item);
        item.setActive(Boolean.FALSE);
        item.setStatus(FmCv.Status.INVALID);
        // To
        to.setKey(null);
        to.setBillId(item.getBillId());
        to.setSerial(item.getSerial() + "R");
        to.setCode(item.getCode() + "R");
        to.setStatus(FmCv.Status.INVALID);
        to.setRelatedId(item.getKey());
        to.setIncome(item.getIncome());
        Ke.umCreated(to, item);
    }

    @Override
    public void transfer(final List<FBillItem> from, final List<FBillItem> to) {
        from.forEach(fromItem -> {
            fromItem.setActive(Boolean.FALSE);
            fromItem.setStatus(FmCv.Status.INVALID);
            fromItem.setType(FmCv.Type.TRANSFER_FROM);
            fromItem.setSerial(fromItem.getSerial() + "F");
            fromItem.setCode(fromItem.getCode() + "F");
        });
        to.forEach(toItem -> {
            toItem.setKey(null);
            toItem.setBillId(null);
            toItem.setSerial(toItem.getSerial() + "T");
            toItem.setCode(toItem.getCode() + "T");
            toItem.setStatus(FmCv.Status.PENDING);
            toItem.setActive(Boolean.TRUE);
        });
    }

    @Override
    public void transfer(final FBook book, final FBill bill) {
        bill.setKey(null);
        bill.setBookId(book.getKey());
        bill.setOrderId(book.getOrderId());
        bill.setModelId(book.getModelId());
        bill.setModelKey(book.getModelKey());
        // Created
        bill.setCreatedAt(bill.getUpdatedAt());
        bill.setCreatedBy(bill.getUpdatedBy());
    }

    @Override
    public void settle(final FSettlement settlement, final List<FBillItem> items) {
        items.forEach(item -> {
            item.setSettlementId(settlement.getKey());
            item.setUpdatedAt(LocalDateTime.now());
            item.setUpdatedBy(settlement.getUpdatedBy());
        });
    }

    @Override
    public void settle(final FSettlement settlement, final FDebt debt) {
        debt.setSettlementId(settlement.getKey());
        if (0 > settlement.getAmount().doubleValue()) {
            // Refund
            debt.setSerial("R" + settlement.getSerial().substring(1));
            debt.setCode("R" + settlement.getCode().substring(1));
        } else {
            // DEBT Serial
            debt.setSerial("D" + settlement.getSerial().substring(1));
            debt.setCode("D" + settlement.getCode().substring(1));
        }
    }

    @Override
    public void payment(final FSettlement settlement, final List<FPaymentItem> payments) {
        for (int idx = 0; idx < payments.size(); idx++) {
            final FPaymentItem item = payments.get(idx);
            item.setSettlementId(settlement.getKey());
            item.setSerial(settlement.getSerial() + "-" + Ut.fromAdjust(idx + 1, 2));
            item.setCode(settlement.getCode() + "-" + Ut.fromAdjust(idx + 1, 2));
            // Fix: Field 'AMOUNT_PRE' doesn't have a default value
            if (Objects.isNull(item.getAmountPre())) {
                item.setAmountPre(BigDecimal.ZERO);
            }

            Ke.umCreated(item, settlement);
        }
    }

    @Override
    public void payment(final FPayment payment, final List<FPaymentItem> payments) {
        for (int idx = 0; idx < payments.size(); idx++) {
            final FPaymentItem item = payments.get(idx);
            item.setPaymentId(payment.getKey());
            if (Objects.isNull(item.getCode()) || Objects.isNull(item.getSerial())) {
                item.setSerial(payment.getSerial() + "-" + Ut.fromAdjust(idx + 1, 2));
                item.setCode(payment.getCode() + "-" + Ut.fromAdjust(idx + 1, 2));
            }
            Ke.umCreated(item, payment);
        }
    }
}
