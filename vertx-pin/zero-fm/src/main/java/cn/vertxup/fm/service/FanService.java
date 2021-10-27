package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.daos.FBillDao;
import cn.vertxup.fm.domain.tables.daos.FBillItemDao;
import cn.vertxup.fm.domain.tables.daos.FBookDao;
import cn.vertxup.fm.domain.tables.daos.FPreAuthorizeDao;
import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FBook;
import cn.vertxup.fm.domain.tables.pojos.FPreAuthorize;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FanService implements FanStub {
    @Override
    public Future<JsonObject> singleAsync(final FBill bill, final FBillItem billItem, final FPreAuthorize authorize) {
        if (Objects.nonNull(authorize)) {
            bill.setAmount(BigDecimal.ZERO);
            billItem.setAmount(BigDecimal.ZERO);
        }
        return Ux.Jooq.on(FBillDao.class).insertAsync(bill).compose(inserted -> {
            this.connect(bill, billItem, 1);
            final List<Future<JsonObject>> futures = new ArrayList<>();
            futures.add(Ux.Jooq.on(FBillItemDao.class).insertJAsync(billItem));
            if (Objects.nonNull(authorize)) {
                this.connect(bill, authorize);
                futures.add(Ux.Jooq.on(FPreAuthorizeDao.class).insertJAsync(authorize));
            }
            final List<FBillItem> itemList = new ArrayList<>();
            itemList.add(billItem);
            return Ux.thenCombine(futures)
                .compose(nil -> this.updateBook(bill, itemList))
                .compose(nil -> Ux.futureJ(bill));
        });
    }

    @Override
    public Future<JsonObject> multiAsync(final FBill bill, final List<FBillItem> items) {
        return Ux.Jooq.on(FBillDao.class).insertAsync(bill).compose(inserted -> {
            this.connect(bill, items);
            return Ux.Jooq.on(FBillItemDao.class).insertJAsync(items)
                .compose(nil -> this.updateBook(bill, items))
                .compose(nil -> Ux.futureJ(bill));
        });
    }

    private Future<FBill> updateBook(final FBill bill, final List<FBillItem> items) {
        final UxJooq jq = Ux.Jooq.on(FBookDao.class);
        return jq.<FBook>fetchByIdAsync(bill.getBookId()).compose(book -> {
            BigDecimal decimal = Objects.requireNonNull(book.getAmount());
            final BigDecimal total = items.stream().map(FBillItem::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (bill.getIncome()) {
                // Consume, +
                decimal = decimal.add(total);
            } else {
                // Pay, -
                decimal = decimal.subtract(total);
            }
            book.setAmount(decimal);
            return jq.updateAsync(book).compose(nil -> Ux.future(bill));
        });
    }

    private void connect(final FBill bill, final FBillItem item, final int number) {
        item.setBillId(bill.getKey());
        item.setSerial(bill.getSerial() + "-" + Ut.fromAdjust(number, 2));
        item.setCode(bill.getCode() + "-" + Ut.fromAdjust(number, 2));
        // price, quanlity, total
        item.setPrice(item.getAmount());
        item.setQuantity(1);
        item.setAmountTotal(item.getAmount());
        Ke.umCreated(item, bill);
    }

    private void connect(final FBill bill, final List<FBillItem> items) {
        for (int idx = 0; idx < items.size(); idx++) {
            final FBillItem item = items.get(idx);
            final int number = (idx + 1);
            item.setBillId(bill.getKey());
            item.setSerial(bill.getSerial() + "-" + Ut.fromAdjust(number, 2));
            item.setCode(bill.getCode() + "-" + Ut.fromAdjust(number, 2));
            item.setAmountTotal(item.getAmount());
            // auditor
            Ke.umCreated(item, bill);
        }
    }

    private void connect(final FBill bill, final FPreAuthorize authorize) {
        if (Objects.nonNull(authorize)) {
            authorize.setBillId(bill.getKey());
            authorize.setSerial(bill.getSerial() + "-AUTH");
            authorize.setCode(bill.getCode() + "-AUTH");
            // active, sigma
            Ke.umCreated(authorize, bill);
        }
    }
}
