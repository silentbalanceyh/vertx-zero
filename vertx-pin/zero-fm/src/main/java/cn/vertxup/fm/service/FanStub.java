package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FPreAuthorize;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface FanStub {

    Future<JsonObject> singleAsync(FBill bill, FBillItem billItem, FPreAuthorize authorize);

    default Future<JsonObject> singleAsync(final FBill bill, final FBillItem billItem) {
        return this.singleAsync(bill, billItem, null);
    }
}
