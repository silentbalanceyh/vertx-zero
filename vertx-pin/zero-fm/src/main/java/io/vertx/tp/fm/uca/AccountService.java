package io.vertx.tp.fm.uca;

import cn.vertxup.fm.domain.tables.daos.FBillDao;
import cn.vertxup.fm.domain.tables.daos.FBookDao;
import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FBook;
import io.vertx.core.Future;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AccountService implements AccountStub {
    @Override
    public Future<Boolean> inBook(final FBill bill, final List<FBillItem> items) {
        final BigDecimal total = items.stream().map(FBillItem::getAmount)
            .filter(Objects::nonNull)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        return this.updateBook(bill, total);
    }

    private Future<Boolean> updateBook(final FBill bill, final BigDecimal amount) {
        final UxJooq jq = Ux.Jooq.on(FBookDao.class);
        return jq.<FBook>fetchByIdAsync(bill.getBookId()).compose(book -> {
            BigDecimal decimal = Objects.requireNonNull(book.getAmount());
            if (bill.getIncome()) {
                // Consume, +
                decimal = decimal.add(amount);
            } else {
                // Pay, -
                decimal = decimal.subtract(amount);
            }
            book.setAmount(decimal);
            return jq.updateAsync(book).compose(nil -> Ux.futureT());
        });
    }

    private Future<FBill> updateBill(final FBill bill, final BigDecimal amount) {
        BigDecimal decimal = Objects.requireNonNull(bill.getAmount());
        if (bill.getIncome()) {
            // Consume, +
            decimal = decimal.add(amount);
        } else {
            // Pay, -
            decimal = decimal.subtract(amount);
        }
        return Ux.Jooq.on(FBillDao.class).updateAsync(bill.setAmount(decimal));
    }

    @Override
    public Future<Boolean> revertBook(final FBillItem item) {
        final BigDecimal amount = Objects.requireNonNull(item.getAmount());
        return Ux.Jooq.on(FBillDao.class).<FBill>fetchByIdAsync(item.getBillId())
            .compose(bill -> this.updateBill(bill, amount))
            .compose(bill -> this.updateBook(bill, amount));
    }
}
