package io.vertx.mod.rbac.acl.region;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * For Data Region enhancement module for `visitant` extension
 */
public interface Cosmo {
    /*
     * When before happened on data region
     * The cosmo should process on Envelop
     */
    Future<Envelop> before(Envelop request, JsonObject matrix);

    /*
     * When after happened on data region
     * The cosmo should process on Envelop based on visitant
     */
    Future<Envelop> after(Envelop response, JsonObject matrix);
}
