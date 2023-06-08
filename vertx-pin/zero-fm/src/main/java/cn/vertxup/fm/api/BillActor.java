package cn.vertxup.fm.api;

import cn.vertxup.fm.domain.tables.daos.FBookDao;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FBook;
import cn.vertxup.fm.domain.tables.pojos.FPreAuthorize;
import cn.vertxup.fm.service.business.FanStub;
import cn.vertxup.fm.service.business.IndentStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.fm.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class BillActor {

    @Inject
    private transient FanStub fanStub;

    @Inject
    private transient IndentStub indentStub;

    @Me
    @Address(Addr.Bill.IN_PRE)
    public Future<JsonObject> inPre(final JsonObject data) {
        // Serial Generation for Bill
        return this.indentStub.initAsync(data).compose(bill -> {
            final FBillItem item = Ux.fromJson(data, FBillItem.class);
            final FPreAuthorize authorize;
            if (data.containsKey("preAuthorize")) {
                final JsonObject preAuthorize = data.getJsonObject("preAuthorize");
                final JsonObject authorizeJson = data.copy().mergeIn(preAuthorize);
                authorize = Ux.fromJson(authorizeJson, FPreAuthorize.class);
            } else {
                authorize = null;
            }
            return this.fanStub.singleAsync(bill, item, authorize);
        });
    }


    @Me
    @Address(Addr.Bill.IN_COMMON)
    public Future<JsonObject> inCommon(final JsonObject data) {
        // Bill building
        return this.indentStub.initAsync(data).compose(bill -> {
            final FBillItem item = Ux.fromJson(data, FBillItem.class);
            return this.fanStub.singleAsync(bill, item);
        });
    }

    @Me
    @Address(Addr.Bill.IN_MULTI)
    public Future<JsonObject> inMulti(final JsonObject data) {
        return this.indentStub.initAsync(data).compose(bill -> {
            final List<FBillItem> item = Ux.fromJson(data.getJsonArray(KName.ITEMS), FBillItem.class);
            return this.fanStub.multiAsync(bill, item);
        });
    }

    @Me
    @Address(Addr.BillItem.UP_SPLIT)
    public Future<JsonObject> upSplit(final String key, final JsonObject data) {
        return this.indentStub.itemAsync(key, data).compose(item -> {
            final List<FBillItem> items = Ux.fromJson(data.getJsonArray(KName.ITEMS), FBillItem.class);
            return this.fanStub.splitAsync(item, items);
        });
    }

    @Me
    @Address(Addr.BillItem.UP_REVERT)
    public Future<JsonObject> upRevert(final String key, final JsonObject data) {
        return this.indentStub.itemAsync(key, data).compose(item -> {
            final FBillItem to = Ux.fromJson(data.getJsonObject("item"), FBillItem.class);
            return this.fanStub.revertAsync(item, to);
        });
    }

    @Me
    @Address(Addr.Bill.UP_TRANSFER)
    public Future<JsonObject> upTransfer(final String bookId, final JsonObject data) {
        return Ux.Jooq.on(FBookDao.class).<FBook>fetchByIdAsync(bookId).compose(book -> {
            if (Objects.isNull(book)) {
                return Ux.future();
            } else {
                final JsonObject normalized = data.copy();
                normalized.remove(KName.ITEMS);
                return this.indentStub.itemAsync(data.getJsonArray(KName.ITEMS), normalized)
                    .compose(map -> this.fanStub.transferAsync(map, book, normalized));
            }
        });
    }

    @Me
    @Address(Addr.BillItem.UP_CANCEL)
    public Future<Boolean> upCancel(final String type, final JsonObject data) {
        final JsonArray keys = data.getJsonArray(KName.KEYS);
        if ("item".equals(type)) {
            // Cancel all
            return this.fanStub.cancelAsync(keys, data);
        } else if ("divide".equals(type)) {
            // Cancel divide
            final String key = data.getString(KName.KEY);
            return this.fanStub.cancelAsync(keys, key, data);
        } else {
            // Undo
            return Ux.futureT();
        }
    }
}
