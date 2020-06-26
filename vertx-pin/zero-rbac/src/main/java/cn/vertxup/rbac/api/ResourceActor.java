package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.daos.SActionDao;
import cn.vertxup.rbac.domain.tables.daos.SResourceDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Queue
public class ResourceActor {

    @Address(Addr.Authority.RESOURCE_SEARCH)
    public Future<JsonObject> searchResource(final JsonObject query) {
        /*
         *
         * for searching resource here.
         * - The first step is creating resource with action at the same time
         * - The action and resource is the relation ( 1:1 ) binding
         * - The permissionId ( S_ACTION ) could be null when the new resource created.
         */
        Sc.infoWeb(this.getClass(),
                "The criteria of input parameters: {0}", query.encode());
        return Ux.Join.on()
                /*
                 * Join in jooq here:
                 *
                 * S_RESOURCE ( key )
                 *      JOIN
                 * S_ACTION ( resourceId )
                 */
                .add(SResourceDao.class)
                .join(SActionDao.class, "resourceId")
                .searchAsync(query);
    }
}
