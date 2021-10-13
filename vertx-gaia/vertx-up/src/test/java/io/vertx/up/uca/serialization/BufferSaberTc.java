package io.vertx.up.uca.serialization;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.commune.Envelop;
import io.vertx.up.runtime.ZeroSerializer;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BufferSaberTc extends ZeroBase {
    @Test
    public void testContext(final TestContext context) {
        final Buffer buffer = this.ioBuffer("data");
        final JsonObject data = new JsonObject()
            .put("k", ZeroSerializer.toSupport(buffer))
            .put("b", buffer);
        // System.out.println(data.getJsonObject("b").getBinary("bytes"));

        final Envelop envelop = Envelop.success(buffer);
        Assert.assertNotNull(envelop.outBuffer());
    }
}
