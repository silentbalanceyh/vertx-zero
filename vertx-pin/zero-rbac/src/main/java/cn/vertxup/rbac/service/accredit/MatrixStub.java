package cn.vertxup.rbac.service.accredit;

import io.vertx.core.Future;
import io.vertx.tp.rbac.logged.ScResource;
import io.vertx.up.commune.secure.DataBound;

/*
 * ResourceMatrix capture for user/role session storage
 */
public interface MatrixStub {
    /*
     * Fetch DataBound by:
     * request - userId, session, fetchProfile
     */
    Future<DataBound> fetchBound(String userId, ScResource resource);
}
