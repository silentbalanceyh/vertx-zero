package io.vertx.up.util;

import io.horizon.exception.internal.EmptyIoException;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

public class IOTe extends ZeroBase {

    @Test(expected = EmptyIoException.class)
    public void testProp(final TestContext context) {
        context.assertNotNull(IO.getProp("in.properties"));
    }
}
