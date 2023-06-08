package cn.vertxup.fm.api;

import cn.vertxup.fm.domain.tables.daos.*;
import cn.vertxup.fm.domain.tables.pojos.*;
import cn.vertxup.fm.service.business.AccountStub;
import cn.vertxup.fm.service.business.FillStub;
import cn.vertxup.fm.service.business.IndentStub;
import cn.vertxup.fm.service.end.PayStub;
import cn.vertxup.fm.service.end.QrStub;
import io.horizon.atom.common.Refer;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.mod.fm.cv.Addr;
import io.vertx.mod.fm.cv.FmCv;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.time.Instant;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class SettleActor {

    @Inject
    private transient IndentStub indentStub;

    @Inject
    private transient AccountStub accountStub;

    @Inject
    private transient FillStub fillStub;

    @Inject
    private transient QrStub qrStub;

    @Inject
    private transient PayStub payStub;

    @Me
    @Address(Addr.Settle.UP_PAYMENT)
    public Future<JsonObject> upPayment(final boolean isRunUp,      // S Bill
                                        final JsonObject data) {
        final Refer settleRef = new Refer();
        /*
         * 1. Settlement Creation with Number Generation
         * The number stored in `indent` of zero extension module, system will
         * call `X_NUMBER` by `code = indent`
         * 2. After processing, the settlement object will be stored in settleRef
         */
        return this.indentStub.settleAsync(data)
            .compose(Ux.Jooq.on(FSettlementDao.class)::insertAsync)
            .compose(settleRef::future)


            /*
             * BillItem updating for settlementId updated and status updating
             * Bill              Settlement
             *    \                /
             *     \              /
             *      \            /
             *        BillItem ( billId, settlementId )
             */
            .compose(inserted -> this.indentStub.settleAsync(data.getJsonArray(KName.ITEMS), data))
            .compose(items -> {


                /*
                 * Update field
                 * - settlementId
                 * - updatedAt
                 * - updatedBy
                 */
                this.fillStub.settle(settleRef.get(), items);
                return Ux.Jooq.on(FBillItemDao.class).updateAsync(items).compose(itemsUpdated -> {
                    /*
                     * FBook Updating,
                     * status is Pending / Finished
                     */
                    final Set<String> bookKeys = Ut.toSet(data.getJsonArray("book"));
                    return this.accountStub.inBook(itemsUpdated, bookKeys)
                        .compose(nil -> Ux.future(items));
                });
            })


            // Settlement item copy from BillItem here
            .compose(items -> this.indentStub.settleAsync(settleRef.get(), items)
                .compose(Ux.Jooq.on(FSettlementItemDao.class)::insertAsync)
            )


            /*
             * Debt generation here
             * 1. Condition one: the price less than 0
             * 2. Condition two: isRunUp ( Must generate debt )
             */
            .compose(settleItems -> {
                final FSettlement settlement = settleRef.get();
                /*
                 * Here are different workflow for `FDebt` and `FPayment`
                 * 1) When isRunUp = true, the system will create `FDebt` here and
                 *    then when user process: Refund / Debt in future, the system will
                 *    create `FPayment` soon.
                 * 2) When isRunUp = false, ignore the FDebt creation because there is
                 *    no continue workflow, create `FPayment` directly.
                 */
                if (isRunUp) {
                    final FDebt debt = Ux.fromJson(data, FDebt.class);
                    debt.setFinished(Boolean.FALSE);
                    return this.createDebt(debt, settlement, settleItems);
                } else {
                    return this.createPayment(data, settlement);
                }
            })
            .compose(nil -> Ux.future(settleRef.get()))
            .compose(Ux::futureJ);
    }

    private Future<Boolean> createPayment(final JsonObject data, final FSettlement settlement) {
        final JsonArray paymentJ = Ut.valueJArray(data, FmCv.ID.PAYMENT);
        final List<FPaymentItem> payments = Ux.fromJson(paymentJ, FPaymentItem.class);
        this.fillStub.payment(settlement, payments);
        return Ux.Jooq.on(FPaymentItemDao.class).insertAsync(payments)
            .compose(nil -> Ux.futureT());
    }

    private Future<Boolean> createDebt(final FDebt debt,
                                       final FSettlement settlement,
                                       final List<FSettlementItem> settleItems) {
        this.fillStub.settle(settlement, debt);
        return Ux.Jooq.on(FDebtDao.class).insertAsync(debt).compose(insertd -> {
            settleItems.forEach(each -> each.setDebtId(insertd.getKey()));
            return Ux.Jooq.on(FSettlementItemDao.class).updateAsync(settleItems)
                .compose(nil -> Ux.futureT());
        });
    }


    @Address(Addr.Settle.UNLOCK_AUTHORIZE)
    public Future<JsonArray> unlockAuthorize(final JsonArray authorized, final User user) {
        // Authorized Modification
        final String userKey = Ux.keyUser(user);
        Ut.itJArray(authorized).forEach(json -> {
            json.put(KName.UPDATED_AT, Instant.now());
            json.put(KName.UPDATED_BY, userKey);
            json.put(KName.STATUS, FmCv.Status.FINISHED);
        });
        final List<FPreAuthorize> authorizeList = Ux.fromJson(authorized, FPreAuthorize.class);
        return Ux.Jooq.on(FPreAuthorizeDao.class).updateAsync(authorizeList).compose(Ux::futureA);
    }

    @Address(Addr.Settle.UP_BOOK)
    public Future<JsonArray> finalizeBook(final JsonArray books, final User user) {
        // Book Finalize ( Not Settlement )
        final String userKey = Ux.keyUser(user);
        Ut.itJArray(books).forEach(json -> {
            json.put(KName.UPDATED_AT, Instant.now());
            json.put(KName.UPDATED_BY, userKey);
        });
        final List<FBook> bookList = Ux.fromJson(books, FBook.class);
        return Ux.Jooq.on(FBookDao.class).updateAsync(bookList).compose(Ux::futureA);
    }

    @Me
    @Address(Addr.Settle.PAY_CREATE)
    public Future<JsonObject> paymentCreate(final JsonObject data) {
        return this.payStub.createAsync(data);
    }

    @Address(Addr.Settle.PAY_DELETE)
    public Future<Boolean> paymentDelete(final String key) {
        return this.payStub.deleteByItem(key);
    }
}
