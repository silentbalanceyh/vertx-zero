package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.cv.em.QrType;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Pre {
    /*
     * 1) Codex for validation
     * 2) Head values: sigma, appId, appKey, language
     * 3) Uri calculation: uri, method
     * 4) Primary Key calculation
     * 5) Excel file calculation
     */
    static Pre codex() {
        return Fn.poolThread(Pooled.PRE_MAP, CodexPre::new, CodexPre.class.getName());
    }

    static Pre head() {
        return Fn.poolThread(Pooled.PRE_MAP, HeadPre::new, HeadPre.class.getName());
    }

    static Pre uri() {
        return Fn.poolThread(Pooled.PRE_MAP, UriPre::new, UriPre.class.getName());
    }

    static Pre key(final boolean isNew) {
        if (isNew) {
            return Fn.poolThread(Pooled.PRE_MAP, UuidPre::new, UuidPre.class.getName());
        } else {
            return Fn.poolThread(Pooled.PRE_MAP, KeyPre::new, KeyPre.class.getName());
        }
    }

    static Pre excel(final ExcelClient client) {
        return Fn.poolThread(Pooled.PRE_MAP, () -> new ExcelPre(client),
            ExcelPre.class.getName() + client.hashCode());
    }

    /*
     * 1) User information: user, habitus
     * 2) Auditor: createdAt / createdBy / updatedAt / updatedBy
     * 3) Combiner for DictFabric
     * 4) Initial Data
     */
    static Pre user() {
        return Fn.poolThread(Pooled.PRE_MAP, UserPre::new, UserPre.class.getName());
    }

    static Pre auditor(final boolean created) {
        if (created) {
            return Fn.poolThread(Pooled.PRE_MAP, CAuditPre::new, CAuditPre.class.getName());
        } else {
            return Fn.poolThread(Pooled.PRE_MAP, UAuditPre::new, UAuditPre.class.getName());
        }
    }

    static Pre auditorBy() {
        return Fn.poolThread(Pooled.PRE_MAP, DAuditPre::new, DAuditPre.class.getName());
    }

    /*
     * 1) number definition for `X_NUMBER`
     * 2) column calculation
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

    static Pre fileIn(final boolean createOnly) {
        if (createOnly) {
            return Fn.poolThread(Pooled.PRE_MAP, CFilePre::new, CFilePre.class.getName());
        } else {
            return Fn.poolThread(Pooled.PRE_MAP, UFilePre::new, UFilePre.class.getName());
        }
    }

    static Pre fileOut() {
        return Fn.poolThread(Pooled.PRE_MAP, DFilePre::new, DFilePre.class.getName());
    }

    /*
     * 1) UniqueKey condition
     * 2) All key condition: sigma = xxx
     * 3) PrimaryKey condition
     * 4) View key
     */
    static Pre qr(final QrType type) {
        if (QrType.ALL == type) {
            return Fn.poolThread(Pooled.PRE_MAP, RWholePre::new, RWholePre.class.getName());
        } else if (QrType.BY_UK == type) {
            return Fn.poolThread(Pooled.PRE_MAP, RUkPre::new, RUkPre.class.getName());
        } else if (QrType.BY_VK == type) {
            return Fn.poolThread(Pooled.PRE_MAP, RVkPre::new, RVkPre.class.getName());
        } else {
            return Fn.poolThread(Pooled.PRE_MAP, RPkPre::new, RPkPre.class.getName());
        }
    }

    /*
     * Major code execution logical
     * J - JsonObject -> JsonObject
     * A - JsonArray -> JsonArray
     * AJ - JsonArray -> JsonObject
     * JA - JsonObject -> JsonArray
     */
    // JsonObject -> JsonObject
    default Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    // JsonArray -> JsonArray
    default Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    // JsonArray -> JsonObject
    default Future<JsonObject> inAJAsync(final JsonArray data, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }

    // JsonObject -> JsonArray
    default Future<JsonArray> inJAAsync(final JsonObject data, final IxMod in) {
        return Future.failedFuture(new _501NotSupportException(this.getClass()));
    }
}
