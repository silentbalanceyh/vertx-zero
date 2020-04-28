package io.vertx.up.commune.rule;

import io.vertx.core.json.JsonObject;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.util.Ut;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class UniqueTc extends ZeroBase {

    @Test
    public void testUnique() {
        final JsonObject input = this.ioJObject("data.json");
        final RuleUnique unique = Ut.deserialize(input, RuleUnique.class);
        Assert.assertNotNull(unique);
        Assert.assertNotNull(unique.child("ci.device"));
        Assert.assertNotNull(unique.child("ci.business"));
        Assert.assertTrue(unique.valid());
        Assert.assertTrue(unique.child("ci.device").valid());
        Assert.assertFalse(unique.child("ci.business").valid());
    }
}
