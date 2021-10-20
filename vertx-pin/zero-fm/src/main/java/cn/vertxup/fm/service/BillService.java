package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.daos.FBillDao;
import cn.vertxup.fm.domain.tables.daos.FBillItemDao;
import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FBook;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BillService implements BillStub {
    @Inject
    private transient BookStub bookStub;

    @Override
    public Future<List<FBill>> fetchByOrder(final String orderId, final String bookId) {
        final JsonObject condition = Ux.whereAnd().put("major", Boolean.TRUE);
        return this.bookStub.fetchAsync(condition).compose(books -> {
            /*
             * Fetch Bill List
             */
            final JsonObject condBill = Ux.whereAnd();
            condBill.put("orderId", orderId);
            final Set<String> bookSet = new HashSet<>();
            if (Ut.notNil(bookId)) {
                bookSet.add(bookId);
            }
            bookSet.addAll(books.stream().map(FBook::getKey)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
            if (!bookSet.isEmpty()) {
                condBill.put("bookId,i", Ut.toJArray(bookSet));
            }
            return Ux.Jooq.on(FBillDao.class).fetchAsync(condBill);
        });
    }

    @Override
    public Future<List<FBillItem>> fetchByBills(final List<FBill> bills, final String type) {
        final JsonObject condition = Ux.whereAnd();
        condition.put("billId,i", Ut.toJArray(bills.stream().map(FBill::getKey)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet())));
        if (Ut.notNil(type) && !"ALL".equals(type)) {
            if ("INVALID".equals(type)) {
                // Cancelled
                condition.put(KName.ACTIVE, Boolean.FALSE);
            } else {
                condition.put(KName.ACTIVE, Boolean.TRUE);
            }
            if ("PENDING".equals(type)) {
                condition.put("settlementId,n", Strings.EMPTY);
            } else {
                condition.put("settlementId,!n", Strings.EMPTY);
            }
        }
        return Ux.Jooq.on(FBillItemDao.class).fetchAsync(condition);
    }
}
