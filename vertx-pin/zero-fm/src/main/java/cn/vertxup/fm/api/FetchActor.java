package cn.vertxup.fm.api;

import cn.vertxup.fm.domain.tables.daos.FBillDao;
import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.service.BillStub;
import cn.vertxup.fm.service.BookStub;
import cn.vertxup.fm.service.end.QrStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.atom.BillData;
import io.vertx.tp.fm.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class FetchActor {
    @Inject
    private transient BillStub billStub;
    @Inject
    private transient BookStub bookStub;
    @Inject
    private transient QrStub qrStub;

    @Address(Addr.BillItem.FETCH_AGGR)
    public Future<JsonObject> fetchAggr(final String orderId) {
        /* bookId / orderId to build List<FBook> */
        final BillData data = new BillData();
        return this.billStub.fetchByOrder(orderId)
            .compose(data::bill)
            /* Fetch Items */
            .compose(bills -> this.billStub.fetchByBills(bills))
            .compose(data::items)
            .compose(this.billStub::fetchSettlements)
            .compose(data::settlement)
            .compose(nil -> data.response(true));
    }


    @Address(Addr.Bill.FETCH_BILLS)
    public Future<JsonObject> fetchBills(final JsonObject query) {
        // Search Bills by Pagination ( Qr Engine )
        return Ux.Jooq.on(FBillDao.class).searchAsync(query);
    }

    @Address(Addr.Bill.FETCH_BILL)
    public Future<JsonObject> fetchByKey(final String key) {
        // Fetch Bill details
        /*
         * {
         *     "items":
         *     "settlements":
         * }
         */
        final JsonObject response = new JsonObject();
        return Ux.Jooq.on(FBillDao.class).<FBill>fetchByIdAsync(key).compose(bill -> {
            if (Objects.isNull(bill)) {
                return Ux.futureJ();
            }
            response.mergeIn(Ux.toJson(bill));
            final List<FBill> bills = new ArrayList<>();
            bills.add(bill);
            return this.billStub.fetchByBills(bills)
                .compose(items -> {
                    response.put(KName.ITEMS, Ux.toJson(items));
                    return this.billStub.fetchSettlements(items);
                })
                .compose(settlement -> {
                    response.put("settlements", Ux.toJson(settlement));
                    return Ux.future(response);
                });
        });
    }

    @Address(Addr.BillItem.FETCH_BOOK)
    public Future<JsonArray> fetchBooks(final String orderId) {
        return this.bookStub.fetchByOrder(orderId)
            .compose(books -> this.bookStub.fetchAuthorize(books)
                .compose(authorized -> {
                    // Books Joined into PreAuthorize
                    final JsonArray bookArray = Ux.toJson(books);
                    final JsonArray authArray = Ux.toJson(authorized);
                    final ConcurrentMap<String, JsonArray> grouped = Ut.elementGroup(authArray, "bookId");
                    Ut.itJArray(bookArray).forEach(bookJson -> {
                        final String key = bookJson.getString(KName.KEY);
                        bookJson.put("authorize", grouped.getOrDefault(key, new JsonArray()));
                    });
                    return Ux.future(bookArray);
                })
            );
    }

    @Address(Addr.BillItem.FETCH_BOOK_BY_KEY)
    public Future<JsonObject> fetchBook(final String bookId) {
        // Null Prevent
        return Ut.ifNil(JsonObject::new, this.bookStub::fetchByKey)
            .apply(bookId);
    }

    @Address(Addr.Settle.FETCH_BY_KEY)
    public Future<JsonObject> fetchSettlement(final String key) {
        return Ut.ifNil(JsonObject::new, this.qrStub::fetchSettlement)
            .apply(key);
    }

    @Address(Addr.Settle.FETCH_DEBT)
    public Future<JsonObject> fetchDebt(final String key) {
        return Ut.ifNil(JsonObject::new, this.qrStub::fetchDebt)
            .apply(key);
    }
}
