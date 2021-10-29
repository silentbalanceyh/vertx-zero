package cn.vertxup.fm.service.business;

import cn.vertxup.fm.domain.tables.daos.FBillDao;
import cn.vertxup.fm.domain.tables.daos.FBookDao;
import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FBook;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AccountService implements AccountStub {
    @Override
    public Future<Boolean> inBook(final FBill bill, final List<FBillItem> items) {
        final BigDecimal total = items.stream().map(FBillItem::getAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return this.bookAsync(bill, total, bill.getUpdatedBy());
    }

    private Future<Boolean> bookAsync(final FBill bill, final BigDecimal amount, final String by) {
        final UxJooq jq = Ux.Jooq.on(FBookDao.class);
        return jq.<FBook>fetchByIdAsync(bill.getBookId()).compose(book -> {
            final BigDecimal decimal = this.amount(book.getAmount(), amount, bill.getIncome());
            book.setAmount(decimal);
            book.setUpdatedAt(LocalDateTime.now());
            book.setUpdatedBy(by);
            return jq.updateAsync(book).compose(nil -> Ux.futureT());
        });
    }

    private BigDecimal amount(final BigDecimal input, final BigDecimal adjust, final boolean income) {
        BigDecimal decimal = Objects.requireNonNull(input);
        if (income) {
            // Consume, +
            decimal = decimal.add(adjust);
        } else {
            // Pay, -
            decimal = decimal.subtract(adjust);
        }
        return decimal;
    }

    @Override
    public Future<Boolean> revertBook(final FBillItem item) {
        final BigDecimal amount = Objects.requireNonNull(item.getAmount());
        return Ux.Jooq.on(FBillDao.class).<FBill>fetchByIdAsync(item.getBillId())
            .compose(bill -> {
                final BigDecimal decimal = this.amount(bill.getAmount(), amount, bill.getIncome());
                bill.setAmount(decimal);
                bill.setUpdatedAt(LocalDateTime.now());
                bill.setUpdatedBy(item.getUpdatedBy());
                return Ux.Jooq.on(FBillDao.class).updateAsync(bill);
            })
            .compose(bill -> this.bookAsync(bill, amount, item.getUpdatedBy()));
    }

    @Override
    public Future<Boolean> revertBook(final List<FBillItem> items) {
        // Group all items
        final ConcurrentMap<String, List<FBillItem>> grouped = Ut.elementGroup(items, FBillItem::getBillId, item -> item);
        // Bills processing
        final JsonObject condition = new JsonObject();
        condition.put("key,i", Ut.toJArray(grouped.keySet()));
        return Ux.Jooq.on(FBillDao.class).<FBill>fetchAsync(condition).compose(bills -> {
            final ConcurrentMap<String, FBill> billMap = Ut.elementMap(bills, FBill::getKey);
            final List<FBill> billList = new ArrayList<>();
            billMap.forEach((key, bill) -> {
                final List<FBillItem> values = grouped.get(key);
                if (!values.isEmpty()) {
                    final String by = values.iterator().next().getUpdatedBy();
                    values.forEach(item -> {
                        final BigDecimal decimal = this.amount(bill.getAmount(), item.getAmount(), !bill.getIncome());
                        bill.setAmount(decimal);
                        bill.setUpdatedAt(LocalDateTime.now());
                        bill.setUpdatedBy(by);
                    });
                    billList.add(bill);
                }
            });
            return Ux.Jooq.on(FBillDao.class).updateAsync(billList);
        }).compose(bills -> {
            if (bills.isEmpty()) {
                return Ux.future();
            } else {
                final ConcurrentMap<String, List<FBill>> billMap = Ut.elementGroup(bills, FBill::getBookId, item -> item);
                // Books processing
                final JsonObject criteria = new JsonObject();
                criteria.put("key,i", Ut.toJArray(billMap.keySet()));
                return Ux.Jooq.on(FBookDao.class).<FBook>fetchAsync(criteria).compose(books -> {
                    final ConcurrentMap<String, FBook> bookMap = Ut.elementMap(books, FBook::getKey);
                    final List<FBook> bookList = new ArrayList<>();
                    bookMap.forEach((key, book) -> {
                        final List<FBill> billEach = billMap.get(key);
                        billEach.forEach(billItem -> items.forEach(item -> {
                            // Condition is !billItem.getIncome()
                            final BigDecimal decimal = this.amount(book.getAmount(), item.getAmount(), !billItem.getIncome());
                            book.setAmount(decimal);
                            book.setUpdatedAt(LocalDateTime.now());
                            book.setUpdatedBy(item.getUpdatedBy());
                        }));
                        bookList.add(book);
                    });
                    return Ux.Jooq.on(FBookDao.class).updateAsync(bookList);
                });
            }
        }).compose(nil -> Ux.futureT());
    }
}
