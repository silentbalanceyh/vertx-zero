package io.vertx.up.uca.jooq.util;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

/**
 * @author lang : 2023/4/25
 */
public class JqCondTc extends ZeroBase {

    @Test
    public void test(final TestContext context) {
        final JsonObject condition = this.ioJObject("input.json");
        final JsonObject filters = JqCond.compress(condition);
        System.out.println(filters.encodePrettily());
    }
}
