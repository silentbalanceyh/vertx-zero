package io.vertx.mod.feign;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.exception.booting.DynamicKeyMissingException;
import io.vertx.up.plugin.init.TpConfig;
import org.junit.Test;

public class FeignDepotTc extends ZeroBase {

    @Test(expected = DynamicKeyMissingException.class)
    public void testFeign(final TestContext context) {
        final TpConfig tpconfig = TpConfig.create("tvk");
        this.logger().info("[ TEST ] Tp Config: {0}", tpconfig);
    }

    @Test
    public void testTlk(final TestContext context) {
        final TpConfig depot = TpConfig.create("qiy");
        this.logger().info("[ TEST ] Endpoint: {0}, Config: {1}", depot.getEndPoint(), depot.getConfig());
    }
}
