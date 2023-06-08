package io.horizon.uca.aop;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Before {
    Set<ChangeFlag> TYPE_ALL = new HashSet<>() {
        {
            this.add(ChangeFlag.ADD);
            this.add(ChangeFlag.UPDATE);
            this.add(ChangeFlag.DELETE);
        }
    };
    Set<ChangeFlag> TYPE_SAVE = new HashSet<>() {
        {
            this.add(ChangeFlag.ADD);
            this.add(ChangeFlag.UPDATE);
        }
    };

    Set<ChangeFlag> TYPE_ADD = new HashSet<>() {
        {
            this.add(ChangeFlag.ADD);
        }
    };

    Set<ChangeFlag> TYPE_UPDATE = new HashSet<>() {
        {
            this.add(ChangeFlag.UPDATE);
        }
    };

    Set<ChangeFlag> TYPE_DELETE = new HashSet<>() {
        {
            this.add(ChangeFlag.DELETE);
        }
    };

    Set<ChangeFlag> types();

    /*
     * Before Operation
     */
    default Future<JsonObject> beforeAsync(final JsonObject data, final JsonObject config) {
        return Future.succeededFuture(data);
    }

    default Future<JsonArray> beforeAsync(final JsonArray data, final JsonObject config) {
        return Future.succeededFuture(data);
    }
}
