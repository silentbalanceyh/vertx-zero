package io.vertx.tp.fm.uca;

import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FPreAuthorize;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface IndentStub {
    /*
     * Serial Generation by `indent`
     */
    default Future<FBill> initAsync(final JsonObject data) {
        return this.initAsync(data.getString(KName.INDENT), data);
    }

    Future<FBill> initAsync(String indent, JsonObject data);

    Future<FBillItem> itemAsync(JsonObject data);

    /*
     * Serial Sub Generation
     */
    void income(FBill bill, List<FBillItem> items);

    void income(FBill bill, FBillItem item);

    void income(FBill bill, FPreAuthorize authorize);

    void split(FBillItem item, List<FBillItem> items);

    void revert(FBillItem item, FBillItem to);
}
