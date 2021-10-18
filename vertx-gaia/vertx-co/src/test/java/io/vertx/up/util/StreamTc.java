package io.vertx.up.util;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

public class StreamTc extends ZeroBase {

    @Test
    public void testRead(final TestContext context) {
        context.assertNotNull(Stream.read(this.ioString("in.txt")));
    }
}
