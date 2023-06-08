package io.vertx.mod.crud.uca.input;

import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.crud.cv.Pooled;
import io.vertx.mod.crud.cv.em.QrType;
import io.vertx.mod.crud.uca.desk.IxMod;
import io.vertx.up.plugin.excel.ExcelClient;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Pre {

    // ------------------- Utility Pre -------------------
    /*
     * 1) Codex for validation
     * 2) Head values: sigma, appId, appKey, language
     * 3) Uri calculation: uri, method
     * 4) Primary Key calculation
     * 5) Excel file calculation
     */
    static Pre codex() {
        return Pooled.CC_PRE.pick(CodexPre::new, CodexPre.class.getName());
        // Pooled.CC_PRE.pick(CodexPre::new, CodexPre.class.getName());
    }

    static Pre head() {
        return Pooled.CC_PRE.pick(HeadPre::new, HeadPre.class.getName());
    }

    static Pre uri() {
        return Pooled.CC_PRE.pick(UriPre::new, UriPre.class.getName());
    }

    static Pre key(final boolean isNew) {
        if (isNew) {
            return Pooled.CC_PRE.pick(UuidPre::new, UuidPre.class.getName());
        } else {
            return Pooled.CC_PRE.pick(KeyPre::new, KeyPre.class.getName());
        }
    }

    static Pre excel(final ExcelClient client) {
        return Pooled.CC_PRE.pick(() -> new ExcelPre(client),
            ExcelPre.class.getName() + client.hashCode());
    }


    // ------------------- User / Audit Pre -------------------
    /*
     * 1) User information: user, habitus
     * 2) Auditor: createdAt / createdBy / updatedAt / updatedBy
     * 3) Combiner for DictFabric
     * 4) Initial Data
     */
    static Pre user() {
        return Pooled.CC_PRE.pick(UserPre::new, UserPre.class.getName());
    }

    static Pre audit(final boolean created) {
        if (created) {
            return Pooled.CC_PRE.pick(CAuditPre::new, CAuditPre.class.getName());
        } else {
            return Pooled.CC_PRE.pick(UAuditPre::new, UAuditPre.class.getName());
        }
    }

    static Pre audit() {
        return Pooled.CC_PRE.pick(DAuditPre::new, DAuditPre.class.getName());
    }

    // ------------------- Column Related -------------------
    /*
     * 1) number definition for `X_NUMBER`
     * 2) column calculation
     */
    static Pre serial() {
        return Pooled.CC_PRE.pick(SerialPre::new, SerialPre.class.getName());
    }

    static Pre apeak(final boolean isMy) {
        if (isMy) {
            return Pooled.CC_PRE.pick(ApeakMyPre::new, ApeakMyPre.class.getName());
        } else {
            return Pooled.CC_PRE.pick(ApeakPre::new, ApeakPre.class.getName());
        }
    }

    // ------------------- Import / Export Pre -------------------
    static Pre fileIn(final boolean createOnly) {
        if (createOnly) {
            return Pooled.CC_PRE.pick(CFilePre::new, CFilePre.class.getName());
        } else {
            return Pooled.CC_PRE.pick(UFilePre::new, UFilePre.class.getName());
        }
    }

    static Pre fileOut() {
        return Pooled.CC_PRE.pick(DFilePre::new, DFilePre.class.getName());
    }

    // ------------------- Qr Related -------------------
    /*
     * 1) UniqueKey condition
     * 2) All key condition: sigma = xxx
     * 3) PrimaryKey condition
     * 4) View key
     */
    static Pre qr(final QrType type) {
        if (QrType.ALL == type) {
            return Pooled.CC_PRE.pick(RWholePre::new, RWholePre.class.getName());
        } else if (QrType.BY_UK == type) {
            return Pooled.CC_PRE.pick(RUkPre::new, RUkPre.class.getName());
        } else if (QrType.BY_VK == type) {
            return Pooled.CC_PRE.pick(RVkPre::new, RVkPre.class.getName());
        } else {
            return Pooled.CC_PRE.pick(RPkPre::new, RPkPre.class.getName());
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
