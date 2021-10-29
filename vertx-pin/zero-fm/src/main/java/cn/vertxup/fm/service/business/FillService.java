package cn.vertxup.fm.service.business;

import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FBook;
import cn.vertxup.fm.domain.tables.pojos.FPreAuthorize;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.FmCv;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FillService implements FillStub {

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
            // auditor
            Ke.umCreated(item, bill);
        }
    }

    @Override
    public void cancel(final FBillItem item, final JsonObject params) {
        item.setActive(Boolean.FALSE);
        item.setStatus(FmCv.Status.INVALID);
        item.setType("Cancel");
        item.setUpdatedAt(LocalDateTime.now());
        item.setUpdatedBy(params.getString(KName.UPDATED_BY));
    }

    // BILL-01
    @Override
    public void income(final FBill bill, final FBillItem item) {
        item.setBillId(bill.getKey());
        item.setSerial(bill.getSerial() + "-01");
        item.setCode(bill.getCode() + "-01");
        item.setStatus("Pending");
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
}
