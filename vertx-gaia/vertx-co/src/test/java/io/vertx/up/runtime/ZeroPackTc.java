package io.vertx.up.runtime;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

import java.util.Set;

public class ZeroPackTc extends ZeroBase {

    @Test
    public void testScan(final TestContext context) {
        final Set<Class<?>> clazzes = ZeroPack.getClasses();
        for (final Class<?> clazz : clazzes) {
            // System.out.println(clazz);
        }
        context.assertNotNull(clazzes);
    }
}
