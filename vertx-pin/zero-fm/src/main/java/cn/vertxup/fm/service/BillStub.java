package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FPaymentItem;
import cn.vertxup.fm.domain.tables.pojos.FSettlement;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface BillStub {

    Future<List<FBill>> fetchByOrder(String orderId);

    Future<List<FBillItem>> fetchByBills(List<FBill> bills);

    Future<List<FSettlement>> fetchSettlements(List<FBillItem> items);

    Future<List<FPaymentItem>> fetchPayments(List<FSettlement> settlements);
}
