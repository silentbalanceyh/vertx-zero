package cn.vertxup.fm.api;

import cn.vertxup.fm.service.BillStub;
import cn.vertxup.fm.service.SettleStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.atom.BillData;
import io.vertx.tp.fm.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class BItemActor {
    @Inject
    private transient BillStub billStub;
    @Inject
    private transient SettleStub settleStub;

    @Address(Addr.BillItem.FETCH_AGGR)
    public Future<JsonObject> fetchAggr(final String orderId) {
        /* bookId / orderId to build List<FBook> */
        final BillData data = new BillData();
        return this.billStub.fetchByOrder(orderId)
            .compose(data::bill)
            /* Fetch Items */
            .compose(bills -> this.billStub.fetchByBills(bills))
            .compose(data::items)
            .compose(this.settleStub::fetchByItems)
            .compose(data::settlement)
            .compose(nil -> data.response(true));
    }
}
