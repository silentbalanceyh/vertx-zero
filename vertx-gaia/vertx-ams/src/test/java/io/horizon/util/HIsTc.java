package io.horizon.util;

import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

public class HIsTc {
    @Test
    public void testIs() {
        final JsonObject item = new JsonObject();
        item.put("name", new JsonObject()
            .put("email", "lang.yu@hpe.com"));
        final Object value = item.getValue("name");
        Assert.assertTrue(HUt.isJObject(value));

        final String password = "1111";
        System.out.println(HUt.encryptMD5(password));
    }
}
