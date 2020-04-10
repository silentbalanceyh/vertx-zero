package io.vertx.up.exception;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.exception.demon.RequiredFieldException;
import org.junit.Test;

public class EnsureTc extends ZeroBase {

    @Test
    public void testRequired(final TestContext context) {
        final DemonException error =
                new RequiredFieldException(getClass(), new JsonObject(), "name");

    }
}
