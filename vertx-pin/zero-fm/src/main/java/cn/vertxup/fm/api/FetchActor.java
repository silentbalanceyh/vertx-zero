package cn.vertxup.fm.api;

import cn.vertxup.fm.domain.tables.daos.FBillDao;
import cn.vertxup.fm.domain.tables.daos.FBillItemDao;
import cn.vertxup.fm.domain.tables.daos.FSettlementDao;
import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FDebt;
import cn.vertxup.fm.service.BillStub;
import cn.vertxup.fm.service.BookStub;
import cn.vertxup.fm.service.end.QrStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.fm.atom.BillData;
import io.vertx.mod.fm.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
            .compose(this.billStub::fetchPayments)
            .compose(payments -> data.response(true));
    }


    @Address(Addr.Bill.FETCH_BILLS)
    public Future<JsonObject> fetchBills(final JsonObject query) {
        // Search Bills by Pagination ( Qr Engine )
        return Ux.Jooq.on(FBillDao.class).searchAsync(query).compose(response -> {
            final JsonArray bill = Ut.valueJArray(response, KName.LIST);
            final Set<String> bills = Ut.valueSetString(bill, KName.KEY);
            return Ux.Jooq.on(FBillItemDao.class).fetchJInAsync("billId", Ut.toJArray(bills))
                .compose(items -> {
                    final ConcurrentMap<String, JsonArray> grouped = Ut.elementGroup(items, "billId");
                    Ut.itJArray(bill).forEach(json -> {
                        final String key = json.getString(KName.KEY);
                        if (grouped.containsKey(key)) {
                            json.put(KName.ITEMS, grouped.getOrDefault(key, new JsonArray()));
                        } else {
                            json.put(KName.ITEMS, new JsonArray());
                        }
                    });
                    response.put(KName.LIST, bill);
                    return Ux.future(response);
                });
        });
        // return Ux.Jooq.on(FBillDao.class).searchAsync(query);
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
            return this.billStub.fetchByBills(bills).compose(items -> {
                response.put(KName.ITEMS, Ux.toJson(items));
                return this.billStub.fetchSettlements(items);
            }).compose(settlements -> this.billStub.fetchPayments(settlements).compose(payments -> {
                // Append `payment` into settlement list ( JsonArray )
                final JsonArray paymentJ = Ux.toJson(payments);
                final ConcurrentMap<String, JsonArray> paymentMap =
                    Ut.elementGroup(paymentJ, "settlementId");
                final JsonArray settlementJ = Ux.toJson(settlements);
                Ut.itJArray(settlementJ).forEach(settlement -> {
                    final String settleKey = settlement.getString(KName.KEY);
                    if (paymentMap.containsKey(settleKey)) {
                        settlement.put("payment", paymentMap.getOrDefault(settleKey, new JsonArray()));
                    } else {
                        settlement.put("payment", new JsonArray());
                    }
                });
                response.put("settlements", settlementJ);
                return Ux.future(response);
            })).otherwise(Ux.otherwise(new JsonObject()));
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
        return Fn.ofJObject(this.bookStub::fetchByKey).apply(bookId);
    }

    @Address(Addr.Settle.FETCH_BY_KEY)
    public Future<JsonObject> fetchSettlement(final String key) {
        return Fn.ofJObject(this.qrStub::fetchSettlement).apply(key);
    }


    @Address(Addr.Settle.FETCH_BY_QR)
    public Future<JsonObject> searchSettle(final JsonObject qr) {
        return Ux.Jooq.on(FSettlementDao.class).searchAsync(qr).compose(pageData -> {
            final JsonArray settlementData = Ut.valueJArray(pageData, "list");
            final Set<String> keys = Ut.valueSetString(settlementData, KName.KEY);
            return this.qrStub.fetchDebtMap(keys).compose(debt -> {
                Ut.itJArray(settlementData).forEach(settleJ -> {
                    final String key = Ut.valueString(settleJ, KName.KEY);
                    if (debt.containsKey(key)) {
                        // linked
                        final FDebt found = debt.get(key);
                        if (0 < found.getAmount().doubleValue()) {
                            settleJ.put("linked", "Debt");
                        } else {
                            settleJ.put("linked", "Refund");
                        }
                    } else {
                        settleJ.put("linked", "Pure");
                    }
                });
                pageData.put("list", settlementData);
                return Ux.future(pageData);
            });
        });
    }

    @Address(Addr.Settle.FETCH_DEBT)
    public Future<JsonObject> fetchDebt(final String key) {
        return Fn.ofJObject(this.qrStub::fetchDebt).apply(key);
    }
}
