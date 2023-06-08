package io.vertx.mod.crud.uca.next;

import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.mod.crud.cv.Pooled;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.mod.crud.uca.desk.IxWeb;

import java.util.List;

/**
 * The interface for module selection
 * Phase 1: Combine input request into IxOpt and pass to IxPanel
 *
 * 1) Combine input ( Envelop, Body, Module ) three format
 * 2) Calculate the result to IxOpt
 *
 * > This component will be called by IxOpt internal
 *
 *
 * 「Sequence」
 * Phase 2: For sequence only
 *
 * 1) Execute `active` function
 * 2) Pass the result of `active` into `standBy` ( Tran Component )
 * 3) Execute `standBy` function
 *
 * Phase 3: Response building
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public interface Co<I, A, S, O> {

    static Co nextQ(final IxMod in, final boolean isArray) {
        if (isArray) {
            return Pooled.CC_CO.pick(() -> new NtAQr(in), NtAQr.class.getName() + in.keyPool());
        } else {
            return Pooled.CC_CO.pick(() -> new NtJQr(in), NtJQr.class.getName() + in.keyPool());
        }
    }

    static Co nextJ(final IxMod in, final boolean isArray) {
        if (isArray) {
            return Pooled.CC_CO.pick(() -> new NtAData(in), NtAData.class.getName() + in.keyPool());
        } else {
            return Pooled.CC_CO.pick(() -> new NtJData(in), NtJData.class.getName() + in.keyPool());
        }
    }

    static Co endV(final boolean isMy) {
        if (isMy) {
            return Pooled.CC_CO.pick(OkAActive::new, "ApeakMy:" + OkAActive.class.getName());
        } else {
            return Pooled.CC_CO.pick(OkAApeak::new, OkAApeak.class.getName());
        }
    }

    static Co endE(final List<String> columns) {
        return Pooled.CC_CO.pick(() -> new OkAExport(columns),
            OkAExport.class.getName() + columns.hashCode());
    }

    /*
     * 「Response」
     * Json + Json -----> Json
     *
     * active - The first executor result
     * standBy - The second executor result
     * response - The API final result
     */
    default Future<O> ok(final A active, final S standBy) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    /*
     * 「Middle」
     * Json + Json -----> Json
     * input - The input data of the first executor
     * active - The first executor result
     * standBy - The standBy result
     */
    default Future<S> next(final I input, final A active) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    default Co<I, A, S, O> bind(final IxWeb request) {
        return this;
    }
}