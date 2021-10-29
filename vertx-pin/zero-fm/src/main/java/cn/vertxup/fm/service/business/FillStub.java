package cn.vertxup.fm.service.business;

import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FBook;
import cn.vertxup.fm.domain.tables.pojos.FPreAuthorize;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface FillStub {

    /*
     * Serial Sub Generation
     */
    void income(FBill bill, List<FBillItem> items);

    void income(FBill bill, FBillItem item);

    void income(FBill bill, FPreAuthorize authorize);

    void split(FBillItem item, List<FBillItem> items);

    void revert(FBillItem item, FBillItem to);

    void cancel(FBillItem item, JsonObject params);

    void transfer(List<FBillItem> from, List<FBillItem> to);

    void transfer(FBook book, FBill bill);
}
