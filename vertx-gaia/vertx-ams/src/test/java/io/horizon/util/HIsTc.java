package io.horizon.util;

import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author lang : 2023/4/27
 */
public class HIsTc {
    @Test
    public void testIs() {
        final JsonObject item = new JsonObject();
        item.put("name", new JsonObject()
            .put("email", "lang.yu@hpe.com"));
        final Object value = item.getValue("name");
        Assert.assertTrue(HUt.isJObject(value));
    }
}
