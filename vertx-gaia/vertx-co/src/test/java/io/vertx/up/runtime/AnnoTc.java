package io.vertx.up.runtime;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.example.AnnoOne;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentMap;

public class AnnoTc extends ZeroBase {

    @Test
    public void testAnnos(final TestContext context) {
        final ConcurrentMap<String, Annotation> clazzes = Anno.get(AnnoOne.class);
        for (final String item : clazzes.keySet()) {
            System.out.println("key=" + item + ",value=" + clazzes.get(item));
        }
        context.assertEquals(2, clazzes.size());
    }
}
