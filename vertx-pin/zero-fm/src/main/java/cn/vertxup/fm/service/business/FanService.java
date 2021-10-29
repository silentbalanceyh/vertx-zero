package cn.vertxup.fm.service.business;

import cn.vertxup.fm.domain.tables.daos.FBillDao;
import cn.vertxup.fm.domain.tables.daos.FBillItemDao;
import cn.vertxup.fm.domain.tables.daos.FPreAuthorizeDao;
import cn.vertxup.fm.domain.tables.pojos.FBill;
import cn.vertxup.fm.domain.tables.pojos.FBillItem;
import cn.vertxup.fm.domain.tables.pojos.FPreAuthorize;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.fm.cv.FmCv;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FanService implements FanStub {
    @Inject
    private transient IndentStub indentStub;
    @Inject
    private transient AccountStub accountStub;

    @Override
    public Future<JsonObject> singleAsync(final FBill bill, final FBillItem billItem, final FPreAuthorize authorize) {
        if (Objects.nonNull(authorize)) {
            bill.setAmount(BigDecimal.ZERO);
            billItem.setAmount(BigDecimal.ZERO);
        }
        return Ux.Jooq.on(FBillDao.class).insertAsync(bill).compose(inserted -> {
            this.indentStub.income(bill, billItem);
            final List<Future<JsonObject>> futures = new ArrayList<>();
            futures.add(Ux.Jooq.on(FBillItemDao.class).insertJAsync(billItem));
            if (Objects.nonNull(authorize)) {
                this.indentStub.income(bill, authorize);
                futures.add(Ux.Jooq.on(FPreAuthorizeDao.class).insertJAsync(authorize));
            }
            final List<FBillItem> itemList = new ArrayList<>();
            itemList.add(billItem);
            return Ux.thenCombine(futures)
                .compose(nil -> this.accountStub.inBook(bill, itemList))
                .compose(nil -> Ux.futureJ(bill));
        });
    }

    @Override
    public Future<JsonObject> multiAsync(final FBill bill, final List<FBillItem> items) {
        return Ux.Jooq.on(FBillDao.class).insertAsync(bill).compose(inserted -> {
            this.indentStub.income(bill, items);
            return Ux.Jooq.on(FBillItemDao.class).insertJAsync(items)
                .compose(nil -> this.accountStub.inBook(bill, items))
                .compose(nil -> Ux.futureJ(bill));
        });
    }

    @Override
    public Future<JsonObject> splitAsync(final FBillItem item, final List<FBillItem> items) {
        this.indentStub.split(item, items);
        final UxJooq jooq = Ux.Jooq.on(FBillItemDao.class);
        return jooq.updateAsync(item)
            .compose(nil -> jooq.insertAsync(items))
            .compose(nil -> Ux.futureJ(item));
    }

    @Override
    public Future<JsonObject> revertAsync(final FBillItem item, final FBillItem to) {
        this.indentStub.revert(item, to);
        final UxJooq jooq = Ux.Jooq.on(FBillItemDao.class);
        return jooq.updateAsync(item)
            .compose(nil -> jooq.insertAsync(to))
            .compose(nil -> this.accountStub.revertBook(to))
            .compose(nil -> Ux.futureJ(item));
    }

    @Override
    public Future<Boolean> cancelAsync(final JsonArray keys, final JsonObject params) {
        final JsonObject condition = Ux.whereAnd();
        condition.put("key,i", keys);
        final UxJooq jq = Ux.Jooq.on(FBillItemDao.class);
        return jq.<FBillItem>fetchAsync(condition).compose(queried -> {
            queried.forEach(each -> this.indentStub.cancel(each, params));
            return jq.updateAsync(queried).compose(this.accountStub::revertBook);
        });
    }

    @Override
    public Future<Boolean> cancelAsync(final JsonArray keys, final String key, final JsonObject params) {
        final JsonObject condition = Ux.whereAnd();
        condition.put("key,i", keys);
        final JsonObject updated = new JsonObject();
        updated.put(KName.UPDATED_AT, params.getValue(KName.UPDATED_AT));
        updated.put(KName.UPDATED_BY, params.getValue(KName.UPDATED_BY));
        updated.put(KName.ACTIVE, Boolean.TRUE);
        updated.put(KName.STATUS, FmCv.Status.PENDING);
        return Ux.Jooq.on(FBillItemDao.class).deleteByAsync(condition)
            .compose(nil -> this.indentStub.itemAsync(key, updated))
            .compose(Ux.Jooq.on(FBillItemDao.class)::updateAsync)
            .compose(nil -> Ux.futureT());
    }
}
