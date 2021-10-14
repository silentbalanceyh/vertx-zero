package io.vertx.up.util.net;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.util.Ut;
import org.junit.Test;

public class NetTc extends ZeroBase {

    @Test
    public void testIp(final TestContext context) {
        final String ip = Ut.netIP();
        this.logger().info("[ ZERO TK ] Ip Address = {0}.", ip);
        context.assertNotNull(ip);
    }

    @Test
    public void testIpV4(final TestContext context) {
        final String ip = Ut.netIPv4();
        this.logger().info("[ ZERO TK ] Ip v4 Address = {0}.", ip);
        context.assertNotNull(ip);
    }

    @Test
    public void testIpV6(final TestContext context) {
        final String ip = Ut.netIPv6();
        this.logger().info("[ ZERO TK ] Ip v6 Address = {0}.", ip);
        context.assertNotNull(ip);
    }
}
