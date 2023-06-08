package io.vertx.up.util;

import io.horizon.util.HUt;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

public class StreamTc extends ZeroBase {

    @Test
    public void testRead(final TestContext context) {
        context.assertNotNull(HUt.ioStream(this.ioString("in.txt")));
    }
}
