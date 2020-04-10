package io.vertx.up.uca.rs;

import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.example.AnnoAgent;
import io.vertx.quiz.example.AnnoExceAgent;
import io.vertx.up.eon.em.ServerType;
import io.vertx.up.verticle.ZeroHttpAgent;
import io.vertx.up.runtime.ZeroAnno;
import io.vertx.up.runtime.ZeroHelper;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

public class VertxAnnoTc {

    private static final ConcurrentMap<ServerType, List<Class<?>>>
            AGENTS = ZeroAnno.getAgents();

    private boolean isDefine(final Class<?>... args) {
        synchronized (AGENTS) {
            final ConcurrentMap<ServerType, Boolean>
                    defined = ZeroHelper.isAgentDefined(AGENTS, args);
            return defined.get(ServerType.HTTP);
        }
    }

    public void testNo() {

    }

    public void testAnno(final TestContext context) {
        context.assertNotNull(AGENTS);
    }

    public void testDefined(final TestContext context) {
        final boolean isDefined = this.isDefine(AnnoExceAgent.class,
                ZeroHttpAgent.class);
        context.assertTrue(isDefined);
    }

    public void testUndefined(final TestContext context) {
        final boolean isDefined = this.isDefine(AnnoExceAgent.class,
                ZeroHttpAgent.class, AnnoAgent.class);
        context.assertFalse(isDefined);
    }

    public void testExcpetion() {
        this.isDefine(ZeroHttpAgent.class);
    }
}
