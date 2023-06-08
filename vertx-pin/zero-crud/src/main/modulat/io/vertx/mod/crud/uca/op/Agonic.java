package io.vertx.mod.crud.uca.op;

import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.Pooled;
import io.vertx.mod.crud.uca.desk.IxMod;

/**
 * I -> I
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Agonic {

    int EXPIRED = 2 * 60 * 60;

    static Agonic write(final ChangeFlag flag) {
        if (ChangeFlag.ADD == flag) {
            return Pooled.CC_AGONIC.pick(AgonicCreate::new, AgonicCreate.class.getName());
        } else if (ChangeFlag.DELETE == flag) {
            return Pooled.CC_AGONIC.pick(AgonicDelete::new, AgonicDelete.class.getName());
        } else {
            return Pooled.CC_AGONIC.pick(AgonicUpdate::new, AgonicUpdate.class.getName());
        }
    }

    static Agonic saveYou(final IxMod module) {
        return Pooled.CC_AGONIC.pick(() -> new AgonicYouSave(module), AgonicYouSave.class.getName());
    }

    static Agonic file() {
        return Pooled.CC_AGONIC.pick(AgonicImport::new, AgonicImport.class.getName());
    }

    static Agonic get() {
        return Pooled.CC_AGONIC.pick(AgonicByID::new, AgonicByID.class.getName());
    }

    static Agonic search() {
        return Pooled.CC_AGONIC.pick(AgonicSearch::new, AgonicSearch.class.getName());
    }

    static Agonic count() {
        return Pooled.CC_AGONIC.pick(AgonicCount::new, AgonicCount.class.getName());
    }

    static Agonic apeak(final boolean isMy) {
        if (isMy) {
            return Pooled.CC_AGONIC.pick(AgonicMy::new, AgonicMy.class.getName());
        } else {
            return Pooled.CC_AGONIC.pick(AgonicFull::new, AgonicFull.class.getName());
        }
    }

    static Agonic view() {
        return Pooled.CC_AGONIC.pick(AgonicView::new, AgonicView.class.getName());
    }

    static Agonic fetch() {
        return Pooled.CC_AGONIC.pick(AgonicFetch::new, AgonicFetch.class.getName());
    }

    default Future<JsonObject> runJAsync(final JsonObject input, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    default Future<JsonArray> runAAsync(final JsonArray input, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    default Future<JsonArray> runJAAsync(final JsonObject input, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    default Future<JsonObject> runAJAsync(final JsonArray input, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
