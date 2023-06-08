package cn.vertxup.fm.service.business;

import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AccountStub {
    /*
     * Single Bill here for Accounting
     */
    Future<Boolean> inBook(FBill bill, List<FBillItem> items);

    default Future<Boolean> inBook(final FBill bill, final FBillItem item) {
        final List<FBillItem> items = new ArrayList<>();
        items.add(item);
        return this.inBook(bill, items);
    }

    /*
     * Multi bills here for Accounting
     */
    Future<Boolean> inBook(List<FBillItem> items);

    Future<Boolean> inBook(List<FBillItem> items, Set<String> closedKeys);
}
