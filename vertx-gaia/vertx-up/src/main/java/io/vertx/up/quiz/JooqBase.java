package io.vertx.up.quiz;

import io.vertx.quiz.ZeroBase;
import io.vertx.tp.plugin.jooq.JooqInfix;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class JooqBase extends ZeroBase {
    static {
        JooqInfix.init(VERTX);
    }
}
