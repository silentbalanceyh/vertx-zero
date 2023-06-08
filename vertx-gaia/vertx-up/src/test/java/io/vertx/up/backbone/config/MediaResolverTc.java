package io.vertx.up.backbone.config;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.quiz.example.Media;
import jakarta.ws.rs.core.MediaType;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Set;

public class MediaResolverTc extends ZeroBase {

    @Test
    public void testProduce(final TestContext context) throws NoSuchMethodException {
        final Method method = Media.class.getDeclaredMethod("sayHello");
        final Set<MediaType> types = MediaResolver.produces(method);
        context.assertEquals(1, types.size());
    }

    @Test
    public void testProduce1(final TestContext context) throws NoSuchMethodException {
        final Method method = Media.class.getDeclaredMethod("sayH");
        final Set<MediaType> types = MediaResolver.produces(method);
        context.assertEquals(1, types.size());
        context.assertTrue(types.contains(MediaType.WILDCARD_TYPE));
    }

    @Test
    public void testConsumes(final TestContext context) throws NoSuchMethodException {
        final Method method = Media.class.getDeclaredMethod("sayHello");
        final Set<MediaType> types = MediaResolver.consumes(method);
        context.assertEquals(1, types.size());
        context.assertTrue(types.contains(MediaType.WILDCARD_TYPE));
    }

    @Test
    public void testConsumes1(final TestContext context) throws NoSuchMethodException {
        final Method method = Media.class.getDeclaredMethod("sayH");
        final Set<MediaType> types = MediaResolver.consumes(method);
        context.assertEquals(1, types.size());
    }
}
