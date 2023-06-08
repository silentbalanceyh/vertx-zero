package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.runtime.ZeroStore;
import org.junit.Test;

public class ZeroVertxTestCase extends ZeroBase {

    @Test
    public void testVertxRead() {
        final JsonObject data = ZeroStore.containerJ();
        System.out.println(data.encodePrettily());
    }
}
