package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.eon.Plugins;
import org.junit.Test;

public class ZeroErrorTc extends ZeroBase {

    @Test
    public void testError(final TestContext context) {
        final Node<JsonObject> node = Node.infix(Plugins.ERROR);
        final JsonObject map = node.read();
        System.out.println(map);
    }
}
