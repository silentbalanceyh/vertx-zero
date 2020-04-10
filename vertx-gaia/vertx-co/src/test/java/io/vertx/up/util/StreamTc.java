package io.vertx.up.util;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.StoreBase;
import org.junit.Test;

public class StreamTc extends StoreBase {

    @Test
    public void testRead(final TestContext context) {
        context.assertNotNull(Stream.read(this.ioFile("in.txt")));
    }
}
