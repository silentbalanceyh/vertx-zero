package io.vertx.up.util;

import io.horizon.exception.internal.EmptyIoException;
import io.horizon.util.HaS;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

public class StreamTe extends ZeroBase {

    @Test(expected = EmptyIoException.class)
    public void testRead(final TestContext context) {
        context.assertNotNull(HaS.ioStream(this.ioString("ini.txt")));
    }
}
