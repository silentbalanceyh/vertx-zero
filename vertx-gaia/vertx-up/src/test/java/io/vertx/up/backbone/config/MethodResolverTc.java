package io.vertx.up.backbone.config;

import io.vertx.core.http.HttpMethod;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import io.vertx.quiz.example.RMethod1;
import org.junit.Test;

import java.lang.reflect.Method;

public class MethodResolverTc extends ZeroBase {

    @Test
    public void testMethod1(final TestContext context)
        throws NoSuchMethodException {
        final Method method = RMethod1.class.getDeclaredMethod("sayHell");
        final HttpMethod httpMethod = MethodResolver.resolve(method);
        context.assertEquals(HttpMethod.GET, httpMethod);
    }

    @Test
    public void testMethod2(final TestContext context)
        throws NoSuchMethodException {
        final Method method = RMethod1.class.getDeclaredMethod("sayHell1");
        final HttpMethod httpMethod = MethodResolver.resolve(method);
        context.assertNull(httpMethod);
    }
}
