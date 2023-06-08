package io.vertx.mod.fm.atom;

import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FSettlement;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BillData implements Serializable {
    private final transient List<FBill> bills = new ArrayList<>();
    private final transient List<FBillItem> items = new ArrayList<>();
    private final transient List<FSettlement> settlements = new ArrayList<>();

    public Future<List<FBill>> bill(final List<FBill> bills) {
        this.bills.clear();
        this.bills.addAll(bills);
        return Ux.future(bills);
    }

    public Future<List<FBillItem>> items(final List<FBillItem> items) {
        this.items.clear();
        this.items.addAll(items);
        return Ux.future(items);
    }

    public Future<List<FSettlement>> settlement(final List<FSettlement> settlements) {
        this.settlements.clear();
        this.settlements.addAll(settlements);
        return Ux.future(settlements);
    }


    public Future<JsonObject> response(final boolean reduce) {
        final JsonObject response = new JsonObject();
        response.put(KName.ITEMS, Ux.toJson(this.items));
        if (reduce) {
            final Set<String> bIds = this.items.stream()
                .map(FBillItem::getBillId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
            final List<FBill> bills = this.bills.stream()
                .filter(item -> bIds.contains(item.getKey()))
                .collect(Collectors.toList());
            final Set<String> sIds = this.items.stream()
                .map(FBillItem::getSettlementId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
            final List<FSettlement> settlements = this.settlements.stream()
                .filter(item -> sIds.contains(item.getKey()))
                .collect(Collectors.toList());
            response.put("bills", Ux.toJson(bills));
            response.put("settlements", Ux.toJson(settlements));
        } else {
            response.put("bills", Ux.toJson(this.bills));
            response.put("settlements", Ux.toJson(this.settlements));
        }
        return Ux.future(response);
    }
}
