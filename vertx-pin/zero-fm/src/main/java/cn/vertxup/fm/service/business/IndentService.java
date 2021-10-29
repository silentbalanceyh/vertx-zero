package cn.vertxup.fm.service.business;

import cn.vertxup.fm.domain.tables.daos.FBillItemDao;
import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FPreAuthorize;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.FmCv;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IndentService implements IndentStub {
    private static final String[] SEQ = new String[]{"A", "B", "C", "D", "E", "F", "G"};

    @Override
    public Future<FBill> initAsync(final String indent, final JsonObject data) {
        Objects.requireNonNull(indent);
        // Bill building
        final FBill preBill = Ux.fromJson(data, FBill.class);
        // Serial Generation for Bill
        return Ke.umIndent(preBill, preBill.getSigma(), indent, FBill::setSerial).compose(bill -> {
            if (Objects.isNull(bill.getCode())) {
                bill.setCode(bill.getSerial());
            }
            return Ux.future(bill);
        });
    }

    @Override
    public Future<FBillItem> itemAsync(final String key, final JsonObject data) {
        return Ux.Jooq.on(FBillItemDao.class).fetchJByIdAsync(key).compose(queried -> {
            final JsonObject normalized = queried.copy().mergeIn(data);
            final FBillItem item = Ux.fromJson(normalized, FBillItem.class);
            return Ux.future(item);
        });
    }

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
                split.setSerial(item.getSerial() + SEQ[idx]);
                split.setCode(item.getCode() + SEQ[idx]);
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
}
