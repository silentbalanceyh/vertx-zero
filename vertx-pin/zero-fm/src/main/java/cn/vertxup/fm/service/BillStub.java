package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface BillStub {

    Future<List<FBill>> fetchByOrder(String orderId, String bookId);

    Future<List<FBillItem>> fetchByBills(List<FBill> bills, String type);
}
