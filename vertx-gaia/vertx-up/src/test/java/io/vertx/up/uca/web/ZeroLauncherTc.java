package io.vertx.up.uca.web;

import io.vertx.ext.unit.TestContext;
import io.vertx.up.Launcher;
import io.vertx.zero.exception.VertxCallbackException;
import io.vertx.up.util.Ut;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

public class ZeroLauncherTc extends ZeroBase {

    @Test(expected = VertxCallbackException.class)
    public void testLauncher(final TestContext context) {
        final Launcher launcher = Ut.singleton(ZeroLauncher.class);
        launcher.start(null);
    }
}
