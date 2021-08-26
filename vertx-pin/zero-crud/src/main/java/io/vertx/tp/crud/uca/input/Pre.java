package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.error._501NotSupportException;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Pre {
    /*
     * Codex for validation
     */
    static Pre codex() {
        return Fn.poolThread(Pooled.PRE_MAP, CodexPre::new, CodexPre.class.getName());
    }

    /*
     * sigma
     * appId
     * appKey
     * language
     */
    static Pre head() {
        return Fn.poolThread(Pooled.PRE_MAP, HeadPre::new, HeadPre.class.getName());
    }

    /*
     * uri
     * method
     */
    static Pre uri() {
        return Fn.poolThread(Pooled.PRE_MAP, UriPre::new, UriPre.class.getName());
    }

    /*
     * user
     * habitus
     */
    static Pre user() {
        return Fn.poolThread(Pooled.PRE_MAP, UserPre::new, UserPre.class.getName());
    }

    /*
     * createdAt
     * createdBy
     * updatedAt
     * updatedBy
     */
    static Pre auditor(final boolean created) {
        if (created) {
            return Fn.poolThread(Pooled.PRE_MAP, CreatePre::new, CreatePre.class.getName());
        } else {
            return Fn.poolThread(Pooled.PRE_MAP, UpdatePre::new, UpdatePre.class.getName());
        }
    }

    static Pre key(final boolean isNew) {
        if (isNew) {
            return Fn.poolThread(Pooled.PRE_MAP, UuidPre::new, UuidPre.class.getName());
        } else {
            return Fn.poolThread(Pooled.PRE_MAP, KeyPre::new, KeyPre.class.getName());
        }
    }

    /*
     * number definition for `X_NUMBER`
     */
    static Pre serial() {
        return Fn.poolThread(Pooled.PRE_MAP, SerialPre::new, SerialPre.class.getName());
    }

    static Pre apeak(final boolean isMy) {
        if (isMy) {
            return Fn.poolThread(Pooled.PRE_MAP, ApeakMyPre::new, ApeakMyPre.class.getName());
        } else {
            return Fn.poolThread(Pooled.PRE_MAP, ApeakPre::new, ApeakPre.class.getName());
        }
    }

    static Pre qUk() {
        return Fn.poolThread(Pooled.PRE_MAP, QUkPre::new, QUkPre.class.getName());
    }

    static Pre qAll() {
        return Fn.poolThread(Pooled.PRE_MAP, QAllPre::new, QAllPre.class.getName());
    }

    static Pre qPk() {
        return Fn.poolThread(Pooled.PRE_MAP, QPkPre::new, QPkPre.class.getName());
    }

    // JsonObject -> JsonObject
    default Future<JsonObject> inJAsync(final JsonObject data, final IxIn in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    // JsonArray -> JsonArray
    default Future<JsonArray> inAAsync(final JsonArray data, final IxIn in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    // JsonArray -> JsonObject
    default Future<JsonObject> inAJAsync(final JsonArray data, final IxIn in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    // JsonObject -> JsonArray
    default Future<JsonArray> inJAAsync(final JsonObject data, final IxIn in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
