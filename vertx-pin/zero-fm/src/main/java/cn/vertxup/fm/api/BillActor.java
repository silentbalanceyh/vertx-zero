package cn.vertxup.fm.api;

import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FPreAuthorize;
import cn.vertxup.fm.service.FanStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.Addr;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class BillActor {

    @Inject
    private transient FanStub fanStub;

    @Me
    @Address(Addr.Bill.IN_PRE)
    public Future<JsonObject> inPre(final JsonObject data) {
        // Bill building
        final FBill preBill = Ux.fromJson(data, FBill.class);
        // Serial Generation for Bill
        return Ke.umIndent(preBill, preBill.getSigma(), data.getString(KName.INDENT), FBill::setSerial).compose(bill -> {
            bill.setCode(bill.getSerial());
            final FBillItem item = Ux.fromJson(data, FBillItem.class);
            final FPreAuthorize authorize;
            if (data.containsKey("preAuthorize")) {
                final JsonObject preAuthorize = data.getJsonObject("preAuthorize");
                final JsonObject authorizeJson = data.copy().mergeIn(preAuthorize);
                authorize = Ux.fromJson(authorizeJson, FPreAuthorize.class);
            } else {
                authorize = null;
            }
            return this.fanStub.preAsync(bill, item, authorize);
        });
    }
}
