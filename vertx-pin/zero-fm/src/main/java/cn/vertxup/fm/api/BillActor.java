package cn.vertxup.fm.api;

import cn.vertxup.fm.domain.tables.daos.FBillItemDao;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FPreAuthorize;
import cn.vertxup.fm.service.FanStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.Addr;
import io.vertx.tp.fm.uca.IndentStub;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;
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
    @Address(Addr.BillItem.IN_SPLIT)
    public Future<JsonObject> inSplit(final JsonObject data) {
        final String key = data.getString(KName.KEY);
        Objects.requireNonNull(key);
        return Ux.Jooq.on(FBillItemDao.class).fetchJByIdAsync(key).compose(queried -> {
            final JsonObject normalized = queried.copy().mergeIn(data);
            final FBillItem item = Ux.fromJson(normalized, FBillItem.class);
            final List<FBillItem> items = Ux.fromJson(data.getJsonArray(KName.ITEMS), FBillItem.class);
            return this.fanStub.splitAsync(item, items);
        });
    }
}
