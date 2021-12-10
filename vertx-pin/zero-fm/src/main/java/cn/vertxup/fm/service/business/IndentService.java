package cn.vertxup.fm.service.business;

import cn.vertxup.fm.domain.tables.daos.FBillItemDao;
import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FSettlement;
import cn.vertxup.fm.domain.tables.pojos.FSettlementItem;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.FmCv;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IndentService implements IndentStub {

    @Inject
    private transient FillStub fillStub;

    @Override
    public Future<FBill> initAsync(final String indent, final JsonObject data) {
        Objects.requireNonNull(indent);
        // Bill building
        final FBill preBill = Ux.fromJson(data, FBill.class);
        // Serial Generation for Bill
        return Ke.umIndent(preBill, preBill.getSigma(), indent, FBill::setSerial).compose(bill -> {
            if (Objects.isNull(bill.getCode())) {
                bill.setCode(bill.getSerial());
            }
            return Ux.future(bill);
        });
    }

    @Override
    public Future<FBillItem> itemAsync(final String key, final JsonObject data) {
        return Ux.Jooq.on(FBillItemDao.class).fetchJByIdAsync(key).compose(queried -> {
            final JsonObject normalized = queried.copy().mergeIn(data);
            final FBillItem item = Ux.fromJson(normalized, FBillItem.class);
            return Ux.future(item);
        });
    }

    @Override
    public Future<ConcurrentMap<Boolean, List<FBillItem>>> itemAsync(final JsonArray items, final JsonObject data) {
        final List<FBillItem> itemFrom = Ux.fromJson(items, FBillItem.class);
        // Build New Items
        final List<FBillItem> itemTo = new ArrayList<>();
        itemFrom.forEach(item -> {
            item.setUpdatedBy(data.getString(KName.UPDATED_BY));
            item.setUpdatedAt(LocalDateTime.now());

            final JsonObject itemJson = Ux.toJson(item);
            final FBillItem itemN = Ux.fromJson(itemJson, FBillItem.class);
            itemN.setRelatedId(item.getKey());
            itemN.setCreatedAt(LocalDateTime.now());
            itemN.setCreatedBy(item.getUpdatedBy());
            itemTo.add(itemN);
        });
        this.fillStub.transfer(itemFrom, itemTo);
        final ConcurrentMap<Boolean, List<FBillItem>> map = new ConcurrentHashMap<>();
        map.put(Boolean.FALSE, itemFrom);
        map.put(Boolean.TRUE, itemTo);
        return Ux.future(map);
    }

    @Override
    public Future<FSettlement> settleAsync(final String indent, final JsonObject data) {
        Objects.requireNonNull(indent);
        // Bill building
        final FSettlement preSettlement = Ux.fromJson(data, FSettlement.class);
        if (Objects.nonNull(preSettlement.getFinished()) && preSettlement.getFinished()) {
            preSettlement.setFinishedAt(LocalDateTime.now());
        }
        // Serial Generation for Bill
        return Ke.umIndent(preSettlement, preSettlement.getSigma(), indent, FSettlement::setSerial).compose(settlement -> {
            if (Objects.isNull(settlement.getCode())) {
                settlement.setCode(settlement.getSerial());
            }
            return Ux.future(settlement);
        });
    }

    @Override
    public Future<List<FBillItem>> settleAsync(final JsonArray items, final JsonObject data) {
        final List<FBillItem> itemList = Ux.fromJson(items, FBillItem.class);
        itemList.forEach(item -> {
            item.setStatus(FmCv.Status.FINISHED);
            item.setUpdatedAt(LocalDateTime.now());
            item.setUpdatedBy(data.getString(KName.UPDATED_BY));
        });
        return Ux.future(itemList);
    }

    @Override
    public Future<List<FSettlementItem>> settleAsync(final FSettlement settlement, final List<FBillItem> items) {
        final List<FSettlementItem> settlements = new ArrayList<>();
        for (int idx = 0; idx < items.size(); idx++) {
            final FBillItem item = items.get(idx);
            final JsonObject record = Ux.toJson(item);

            record.remove(KName.KEY);
            final FSettlementItem settlementItem = Ux.fromJson(record, FSettlementItem.class);
            settlementItem.setSettlementId(settlement.getKey());
            settlementItem.setRelatedId(item.getKey());

            Ke.umCreated(settlementItem, item);
            settlementItem.setSerial(settlement.getSerial() + "-" + Ut.fromAdjust(idx + 1, 3));
            settlementItem.setCode(settlement.getCode() + "-" + Ut.fromAdjust(idx + 1, 3));
            settlements.add(settlementItem);
        }
        return Ux.future(settlements);
    }
}
