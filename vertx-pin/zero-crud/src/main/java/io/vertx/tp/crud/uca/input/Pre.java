package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.Pooled;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.error._501NotSupportException;
import io.vertx.tp.ke.atom.specification.KColumn;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;

import java.util.Objects;

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
     * 3) Fabric for DictFabric
     * 4) Initial Data
     */
    static Pre user() {
        return Fn.poolThread(Pooled.PRE_MAP, UserPre::new, UserPre.class.getName());
    }

    static Pre auditor(final boolean created) {
        if (created) {
            return Fn.poolThread(Pooled.PRE_MAP, CreatePre::new, CreatePre.class.getName());
        } else {
            return Fn.poolThread(Pooled.PRE_MAP, UpdatePre::new, UpdatePre.class.getName());
        }
    }

    static Pre fabric(final boolean isFrom) {
        if (isFrom) {
            return Fn.poolThread(Pooled.PRE_MAP, DiFromPre::new, DiFromPre.class.getName());
        } else {
            return Fn.poolThread(Pooled.PRE_MAP, DiToPre::new, DiToPre.class.getName());
        }
    }

    static Pre initial() {
        return Fn.poolThread(Pooled.PRE_MAP, InitialPre::new, InitialPre.class.getName());
    }

    static Pre tree(final boolean isFrom) {
        if (isFrom) {
            return Fn.poolThread(Pooled.PRE_MAP, DiFTreePre::new, DiFTreePre.class.getName());
        } else {
            return Fn.poolThread(Pooled.PRE_MAP, DiTTreePre::new, DiTTreePre.class.getName());
        }
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

    /*
     * 1) UniqueKey condition
     * 2) All key condition: sigma = xxx
     * 3) PrimaryKey condition
     * 4) View key
     */
    static Pre qVk() {
        return Fn.poolThread(Pooled.PRE_MAP, QVkPre::new, QVkPre.class.getName());
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

class T {

    /*
     * Processing `view` parameters here
     */
    static void viewProc(final JsonObject data, final KColumn column) {
        Fn.safeSemi(Objects.isNull(data.getValue(KName.VIEW)), () ->
            // Vis: Fix bug of default view
            data.put(KName.VIEW, Vis.smart(column.getView())));
    }
}
