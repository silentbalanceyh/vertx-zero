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
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.fm.refine.Fm;
import io.vertx.up.atom.extension.KNaming;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BookService implements BookStub {
    @Override
    public Future<List<FPreAuthorize>> fetchAuthorize(final List<FBook> books) {
        final Set<String> bookIds = books.stream().map(FBook::getKey)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        final JsonObject condition = new JsonObject();
        condition.put("bookId,i", Ut.toJArray(bookIds));
        return Ux.Jooq.on(FPreAuthorizeDao.class).fetchAsync(condition);
    }

    @Override
    public Future<List<FBook>> fetchAsync(final JsonObject criteria) {
        return Ux.Jooq.on(FBookDao.class).fetchAsync(criteria);
    }

    @Override
    public Future<List<FBook>> fetchByOrder(final String orderId) {
        Objects.requireNonNull(orderId);
        final JsonObject condition = new JsonObject();
        condition.put("orderId", orderId);
        return this.fetchAsync(condition);
    }

    @Override
    public Future<List<FBook>> createAsync(final List<FBook> books, final KNaming spec) {
        final List<FBook> subBooks = Fm.umBook(spec, books);
        return Ux.Jooq.on(FBookDao.class).insertAsync(subBooks);
    }

    @Override
    public Future<JsonObject> fetchByKey(final String key) {
        return Ux.Jooq.on(FBookDao.class).<FBook>fetchByIdAsync(key).compose(book -> {
            // Fetch all bills in current book
            return Ux.Jooq.on(FBillDao.class).<FBill>fetchAsync("bookId", key).compose(bills -> {
                // Bills to fetch items
                final Set<String> billIds = bills.stream()
                    .map(FBill::getKey)
                    .filter(Ut::isNotNil)
                    .collect(Collectors.toSet());
                // Bill items
                return Ux.Jooq.on(FBillItemDao.class).<FBillItem>fetchInAsync("billId", Ut.toJArray(billIds)).compose(items -> {
                    // Grouped Items
                    final ConcurrentMap<String, List<FBillItem>> itemMap
                        = Ut.elementGroup(items, FBillItem::getBillId, item -> item);
                    // Response Building
                    final JsonObject bookJson = Ux.toJson(book);
                    final JsonArray billA = new JsonArray();
                    bills.forEach(bill -> {
                        final List<FBillItem> billItems = itemMap.getOrDefault(bill.getKey(), new ArrayList<>());
                        final JsonObject billJ = Ux.toJson(bill);
                        if (billItems.isEmpty()) {
                            billJ.put(KName.CHILDREN, new JsonArray());
                        } else {
                            billJ.put(KName.CHILDREN, Ux.toJson(billItems));
                        }
                        billA.add(billJ);
                    });
                    bookJson.put(KName.CHILDREN, billA);
                    return Ux.future(bookJson);
                });
            });
        });
    }
}
