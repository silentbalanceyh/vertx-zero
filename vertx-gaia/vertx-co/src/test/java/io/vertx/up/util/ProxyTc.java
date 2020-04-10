package io.vertx.up.util;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

interface Hello {
    void sayHello();
}

public class ProxyTc extends ZeroBase {
    @Test
    public void testInvoke(final TestContext context) {
        final Object proxy = Invoker.getProxy(Hello.class);
        Ut.invoke(proxy, "sayHello");
    }
}