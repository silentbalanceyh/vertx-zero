package io.vertx.up.util;

import io.horizon.util.HUt;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.TestContext;
import io.vertx.quiz.ZeroBase;
import org.junit.Test;

public class JacksonTc extends ZeroBase {

    @Test
    public void testvJson(final TestContext context) {
        final JsonObject data = HUt.ioJObject(this.ioString("jackson-testInt.json"));
        final JsonObject hitted = Jackson.visitJObject(data, "lang", "home", "email");
        context.assertNotNull(hitted);
    }

    @Test
    public void testvInt(final TestContext context) {
        final JsonObject data = HUt.ioJObject(this.ioString("jackson-testInt.json"));
        final Integer hitted = Jackson.visitInt(data, "lang", "home", "email", "index");
        context.assertEquals(3, hitted);
    }

    @Test
    public void testMerge(final TestContext context) {
        final JsonArray fromData = HUt.ioJArray(this.ioString("from.json"));
        final JsonArray toData = HUt.ioJArray(this.ioString("to.json"));
        final JsonArray result = HUt.elementJoin(fromData, toData, "key", "id");
        System.out.println(result);
    }

    @Test
    public void testvString(final TestContext context) {
        final JsonObject data = HUt.ioJObject(this.ioString("jackson-testInt.json"));
        final String hitted = Jackson.visitString(data, "lang", "visit");
        context.assertEquals("Home City", hitted);
    }

    @Test
    public void testvString1(final TestContext context) {
        final JsonObject data = HUt.ioJObject(this.ioString("jackson-testInt.json"));
        final String hitted = Jackson.visitString(data, "lang", "home", "deep1", "deep2", "deep3");
        context.assertEquals("Home City", hitted);
    }

    @Test
    public void testvEmpty(final TestContext context) {
        final JsonObject data = HUt.ioJObject(this.ioString("jackson-testInt.json"));
        final Integer hitted = Jackson.visitInt(data, "lang", "visitx");
        context.assertNull(hitted);
    }

    @Test
    public void testSerializer(final TestContext context) {
        final JsonObject data = HUt.ioJObject(this.ioString("jackson-json.json"));
        final JsonObject content = new JsonObject(HUt.serialize(data));
        System.out.println(content.encodePrettily());
    }

    @Test
    public void testJsonObject(final TestContext context) {
        final JsonObject data = this.ioJObject("jackson-json.json");
        final JsonObject content = Jackson.deserialize(data.toString(), JsonObject.class, false);
        System.out.println(content.encodePrettily());
    }

    @Test
    public void testSubset(final TestContext context) {
        final JsonObject source = this.ioJObject("subset-source.json");
        final JsonObject expected = this.ioJObject("subset-target.json");
        final JsonObject subset = Ut.elementSubset(source, "username", "age");
        System.out.println(subset);
        context.assertEquals(expected, subset);
    }
}
