package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;
import org.junit.Test;

import java.util.concurrent.ConcurrentMap;

public class ZeroLimeTc {

    private transient final Node<ConcurrentMap<String, String>> node
            = Ut.singleton(ZeroLime.class);

    @Test
    public void testGen() {
        final Node<JsonObject> dyanmic = Ut.singleton(ZeroUniform.class);
        final JsonObject data = dyanmic.read();
        System.out.println(data.encodePrettily());
    }
}
