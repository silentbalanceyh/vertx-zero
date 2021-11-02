package cn.vertxup.fm.service.business;

import cn.vertxup.fm.domain.tables.daos.FBillDao;
import cn.vertxup.fm.domain.tables.daos.FBookDao;
import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FBook;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.FmCv;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AccountService implements AccountStub {
    @Override
    public Future<Boolean> inBook(final FBill bill, final List<FBillItem> items) {
        final UxJooq jq = Ux.Jooq.on(FBookDao.class);
        return jq.<FBook>fetchByIdAsync(bill.getBookId())
            .compose(book -> this.bookAsync(book, bill, items))
            .compose(jq::updateAsync)
            .compose(nil -> Ux.futureT());
    }

    private Future<FBook> bookAsync(final FBook book, final FBill bill, final List<FBillItem> items) {
        return Ux.future(this.book(book, bill, items));
    }

    private FBook book(final FBook book, final FBill bill, final List<FBillItem> items) {
        items.forEach(item -> {
            BigDecimal bookDecimal = Objects.requireNonNull(book.getAmount());
            // Bill for `income` checking
            final BigDecimal adjust = Objects.requireNonNull(item.getAmount());
            if (bill.getIncome()) {
                if (FmCv.Status.INVALID.equals(item.getStatus()) || FmCv.Status.FINISHED.equals(item.getStatus())) {
                    // Move out, Consume, -
                    bookDecimal = bookDecimal.subtract(adjust);
                } else {
                    // Move in, Consume, +
                    bookDecimal = bookDecimal.add(adjust);
                }
            } else {
                if (FmCv.Status.INVALID.equals(item.getStatus()) || FmCv.Status.FINISHED.equals(item.getStatus())) {
                    // Move out, Pay, +
                    bookDecimal = bookDecimal.add(adjust);
                } else {
                    // Move in, Pay, -
                    bookDecimal = bookDecimal.subtract(adjust);
                }
            }
            book.setUpdatedAt(LocalDateTime.now());
            book.setUpdatedBy(item.getUpdatedBy());
            book.setAmount(bookDecimal);
        });
        return book;
    }

    @Override
    public Future<Boolean> inBook(final List<FBillItem> items) {
        // Collect bill ids from items
        final Set<String> billKeys = items.stream()
            .map(FBillItem::getBillId)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        // Bills processing
        final JsonObject condition = new JsonObject();
        condition.put("key,i", Ut.toJArray(billKeys));
        return Ux.Jooq.on(FBillDao.class).<FBill>fetchAsync(condition).compose(bills -> {
            if (bills.isEmpty()) {
                return Ux.future();
            } else {
                final ConcurrentMap<String, List<FBill>> billMap = Ut.elementGroup(bills, FBill::getBookId, item -> item);
                final JsonObject criteria = new JsonObject();
                criteria.put("key,i", Ut.toJArray(billMap.keySet()));
                return Ux.Jooq.on(FBookDao.class).<FBook>fetchAsync(criteria).compose(books -> {

                    // Book Processing Grouped
                    final ConcurrentMap<String, FBook> bookMap = Ut.elementMap(books, FBook::getKey);
                    final List<FBook> bookList = new ArrayList<>();
                    bookMap.forEach((key, book) -> {
                        final List<FBill> billEach = billMap.get(key);
                        billEach.forEach(billItem -> bookList.add(this.book(book, billItem, items)));
                    });
                    return Ux.Jooq.on(FBookDao.class).updateAsync(bookList);
                });
            }
        }).compose(nil -> Ux.futureT());
    }
}
