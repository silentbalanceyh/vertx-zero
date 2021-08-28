package io.vertx.quiz;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import io.vertx.quiz.nova.Qz;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.tp.plugin.session.SessionInfix;
import io.vertx.tp.plugin.shared.MapInfix;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public abstract class QzTc {

    static final Vertx VERTX = Vertx.vertx();
    @Rule
    public final RunTestOnContext rule = new RunTestOnContext();
    protected Qz qz = Qz.get(this.getClass());

    @BeforeClass
    public static void setUp() {
        /* Jooq Infix */
        JooqInfix.init(VERTX);
        /* Shared Infix */
        MapInfix.init(VERTX);
        /* Session Infix */
        SessionInfix.init(VERTX);
    }
}
