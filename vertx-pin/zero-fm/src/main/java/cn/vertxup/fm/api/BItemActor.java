package cn.vertxup.fm.api;

import cn.vertxup.fm.service.BillStub;
import cn.vertxup.fm.service.BookStub;
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
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class BItemActor {
    @Inject
    private transient BillStub billStub;
    @Inject
    private transient BookStub bookStub;

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
}
