package cn.vertxup.fm.service.business;

import cn.vertxup.fm.domain.tables.pojos.*;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface FillStub {

    void income(FBill bill, List<FBillItem> items);

    void income(FBill bill, FBillItem item);

    void income(FBill bill, FPreAuthorize authorize);

    void split(FBillItem item, List<FBillItem> items);

    void revert(FBillItem item, FBillItem to);

    void cancel(FBillItem item, JsonObject params);

    void transfer(List<FBillItem> from, List<FBillItem> to);

    void transfer(FBook book, FBill bill);

    void settle(FSettlement settlement, List<FBillItem> items);

    void settle(FSettlement settlement, FDebt debt);

    void payment(FSettlement settlement, List<FPaymentItem> payments);

    void payment(FPayment payment, List<FPaymentItem> payments);
}
