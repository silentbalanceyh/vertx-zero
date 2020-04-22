package io.vertx.up.util;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.exception.heart.EmptyStreamException;
import org.junit.Test;

public class StreamTe extends ZeroBase {

    @Test(expected = EmptyStreamException.class)
    public void testRead(final TestContext context) {
        context.assertNotNull(Stream.read(this.ioFile("ini.txt")));
    }
}
