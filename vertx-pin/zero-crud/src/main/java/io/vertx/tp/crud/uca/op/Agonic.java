package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.error._501NotSupportException;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.fn.Fn;

/**
 * I -> I
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Agonic {

    static Agonic write(final ChangeFlag flag) {
        if (ChangeFlag.ADD == flag) {
            return Fn.poolThread(Pooled.AGONIC_MAP, AgonicCreate::new, AgonicCreate.class.getName());
        } else if (ChangeFlag.DELETE == flag) {
            return Fn.poolThread(Pooled.AGONIC_MAP, AgonicDelete::new, AgonicDelete.class.getName());
        } else {
            return Fn.poolThread(Pooled.AGONIC_MAP, AgonicUpdate::new, AgonicUpdate.class.getName());
        }
    }

    static Agonic get() {
        return Fn.poolThread(Pooled.AGONIC_MAP, AgonicByID::new, AgonicByID.class.getName());
    }

    static Agonic search() {
        return Fn.poolThread(Pooled.AGONIC_MAP, AgonicSearch::new, AgonicSearch.class.getName());
    }

    static Agonic count() {
        return Fn.poolThread(Pooled.AGONIC_MAP, AgonicCount::new, AgonicCount.class.getName());
    }

    static Agonic apeak(final boolean isMy) {
        if (isMy) {
            return Fn.poolThread(Pooled.AGONIC_MAP, AngleMy::new, AngleMy.class.getName());
        } else {
            return Fn.poolThread(Pooled.AGONIC_MAP, AngleFull::new, AngleFull.class.getName());
        }
    }

    static Agonic all() {
        return Fn.poolThread(Pooled.AGONIC_MAP, AngleAll::new, AngleAll.class.getName());
    }

    default Future<JsonObject> runJAsync(final JsonObject input, final IxIn in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    default Future<JsonArray> runAAsync(final JsonArray input, final IxIn in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    default Future<JsonArray> runJAAsync(final JsonObject input, final IxIn in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    default Future<JsonArray> runAJAsync(final JsonArray input, final IxIn in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
