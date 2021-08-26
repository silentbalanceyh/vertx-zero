package io.vertx.up.unity.jq;

import io.vertx.core.json.JsonObject;
import io.vertx.quiz.JooqBase;
import io.vertx.tp.plugin.jooq.condition.JooqCond;
import org.jooq.Condition;
import org.jooq.Operator;
import org.jooq.impl.DSL;
import org.junit.Assert;
import org.junit.Test;

public class JooqCondTc extends JooqBase {

    public Condition eq(final String name, final Object value) {
        return DSL.field(name).eq(value);
    }

    public Condition condAnd(final String filename) {
        final JsonObject filters = this.ioJObject(filename);
        return JooqCond.transform(filters, Operator.AND, null);
    }

    @Test
    public void testExistsOneAsync() {
        final Condition condition = this.condAnd("existsOneAsync.json");
        // Expected
        final Condition expected = this.eq("name", "Lang")
            .and(this.eq("code", "Test"));
        Assert.assertEquals(condition, expected);
    }


    @Test
    public void testExistsOneAsync2() {
        final Condition condition = this.condAnd("existsOneAsync2.json");
        // Expected
        final Condition expected = this.eq("name", "Lang")
            .and(this.eq("code", "Test"));
        Assert.assertEquals(condition, expected);
    }

    @Test
    public void testFetchOneAndAsync() {
        final Condition condition = this.condAnd("fetchOneAnyAsync.json");
        // Expected
        final Condition expected = this.eq("name", "Lang")
            .and(this.eq("code", "Test"));
        Assert.assertEquals(condition, expected);
    }
}
