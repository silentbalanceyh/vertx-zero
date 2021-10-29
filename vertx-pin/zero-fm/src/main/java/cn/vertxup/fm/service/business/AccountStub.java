package cn.vertxup.fm.service.business;

import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import io.vertx.core.Future;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AccountStub {
    /*
     * In Updating
     */
    Future<Boolean> inBook(FBill bill, List<FBillItem> items);

    /*
     * Revert Updating
     */
    Future<Boolean> revertBook(FBillItem item);

    Future<Boolean> revertBook(List<FBillItem> items);
}
