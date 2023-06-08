package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.runtime.ZeroStore;
import org.junit.Test;

public class ZeroErrorTestCase extends ZeroBase {

    @Test
    public void testError(final TestContext context) {
        final JsonObject errorJ = ZeroStore.option(YmlCore.error.__KEY);
        System.out.println(errorJ);
    }
}
