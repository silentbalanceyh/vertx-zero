package io.vertx.tp.crud.uca.tran;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.desk.IxWeb;
import io.vertx.tp.error._501NotSupportException;
import io.vertx.up.fn.Fn;

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
            return Fn.poolThread(Pooled.CO_MAP, () -> new NtAQr(in), NtAQr.class.getName() + in.keyPool());
        } else {
            return Fn.poolThread(Pooled.CO_MAP, () -> new NtJQr(in), NtJQr.class.getName() + in.keyPool());
        }
    }

    static Co nextJ(final IxMod in) {
        return Fn.poolThread(Pooled.CO_MAP, () -> new NtJRecord(in), NtJRecord.class.getName() + in.keyPool());
    }

    static Co endV(final boolean isMy) {
        if (isMy) {
            return Fn.poolThread(Pooled.CO_MAP, OkAActive::new, "ApeakMy:" + OkAActive.class.getName());
        } else {
            return Fn.poolThread(Pooled.CO_MAP, OkAApeak::new, OkAApeak.class.getName());
        }
    }

    static Co endE(final List<String> columns) {
        return Fn.poolThread(Pooled.CO_MAP, () -> new OkAExport(columns),
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

interface OkA<I> extends Co<I, JsonArray, Object, JsonArray> {
}

interface OkJ<I> extends Co<I, JsonObject, Object, JsonObject> {
}

interface NtJ<O> extends Co<JsonObject, JsonObject, JsonObject, O> {
}

interface NtA<O> extends Co<JsonArray, JsonArray, JsonArray, O> {
}