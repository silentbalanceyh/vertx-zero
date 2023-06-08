package cn.vertxup.psi.api;

import cn.vertxup.psi.domain.tables.daos.PPosDao;
import cn.vertxup.psi.domain.tables.daos.PWhDao;
import io.modello.eon.em.EmValue;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.psi.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.uca.jooq.UxJoin;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class WhActor {
    private static final String CHILD_FIELD = "positions";

    @Me
    @Address(Addr.WH_CREATE)
    public Future<JsonObject> createAsync(final JsonObject data) {
        return this.dao().insertAsync(data, CHILD_FIELD);
    }

    @Me(active = EmValue.Bool.IGNORE)
    @Address(Addr.WH_UPDATE)
    public Future<JsonObject> updateAsync(final String key, final JsonObject data) {
        return this.dao().updateAsync(key, data, CHILD_FIELD);
    }

    @Address(Addr.WH_DELETE)
    public Future<Boolean> removeAsync(final String key) {
        return this.dao().removeByIdAsync(key);
    }

    @Address(Addr.WH_READ)
    public Future<JsonObject> readAsync(final String key) {
        return this.dao().fetchByIdAAsync(key, CHILD_FIELD);
    }

    private UxJoin dao() {
        return Ux.Join.on(PWhDao.class).join(PPosDao.class, "whId");
    }
}
