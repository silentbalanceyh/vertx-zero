package io.vertx.tp.rbac.meansure;

import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.Status;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.up.uca.monitor.meansure.AbstractQuota;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ResourceQuota extends AbstractQuota {
    public ResourceQuota(final Vertx vertx) {
        super(vertx);
    }

    @Override
    public void handle(final Promise<Status> event) {
        // Permission Pool
        final ScConfig config = ScPin.getConfig();
        this.mapAsync(config.getPermissionPool(), map -> {
            System.out.println(map);
        });
    }
}
