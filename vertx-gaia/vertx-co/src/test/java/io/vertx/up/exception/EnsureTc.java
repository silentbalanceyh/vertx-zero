package io.vertx.up.exception;

import io.horizon.exception.DaemonException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.exception.daemon.RequiredFieldException;
import org.junit.Test;

public class EnsureTc extends ZeroBase {

    @Test
    public void testRequired(final TestContext context) {
        final DaemonException error =
            new RequiredFieldException(this.getClass(), new JsonObject(), "name");

    }
}
