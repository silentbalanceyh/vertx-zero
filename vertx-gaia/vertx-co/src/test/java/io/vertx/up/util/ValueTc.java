package io.vertx.up.util;

import io.vertx.core.json.JsonObject;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

public class ValueTc {

    private void testJ(final Object result, final String type, final Class<?> expected) {
        final JsonObject json = new JsonObject();
        System.out.println(type + " -> " + result.getClass() + " : " + result);
        Assert.assertEquals(expected, result.getClass());
        json.put("TEST", result);
    }

    private void testJInstant(final Object instant, final String type) {
        this.testJ(instant, type, Instant.class);
    }

    private void testJInt(final Object instant, final String type) {
        this.testJ(instant, type, Integer.class);
    }

    private void testJLong(final Object instant, final String type) {
        this.testJ(instant, type, Long.class);
    }

    private void testJShort(final Object instant, final String type) {
        this.testJ(instant, type, Short.class);
    }

    private void testJFloat(final Object instant, final String type) {
        this.testJ(instant, type, Float.class);
    }

    private void testJDouble(final Object instant, final String type) {
        this.testJ(instant, type, Double.class);
    }

    private void testJBoolean(final Object instant, final String type) {
        this.testJ(instant, type, Boolean.class);
    }

    /*
     * JsonObject data conversation
     */
    @Test
    public void testJValue() {
        /*
         * null
         */
        Assert.assertNull(Ut.aiJValue(null));
        Assert.assertNull(Ut.aiJValue("null"));
        Assert.assertNull(Ut.aiJValue("undefined"));

        System.out.println("Date");
        // -------------  To Time --------------
        /*
         * java.util.Date -> java.time.Instant
         */
        Object calculated = Ut.aiJValue(new Date());
        this.testJInstant(calculated, "Date");
        /*
         * java.time.LocalTime -> java.time.Instant
         */
        calculated = Ut.aiJValue(LocalTime.now());
        this.testJInstant(calculated, "LocalTime");
        /*
         * java.time.LocalDateTime -> java.time.Instant
         */
        calculated = Ut.aiJValue(LocalDateTime.now());
        this.testJInstant(calculated, "LocalDateTime");
        /*
         * java.time.LocalDate -> java.time.Instant
         */
        calculated = Ut.aiJValue(LocalDate.now());
        this.testJInstant(calculated, "LocalDate");
        /*
         * java.lang.String -> java.time.Instant
         */
        calculated = Ut.aiJValue("2019-12-26T07:32:37.309Z", Date.class);
        this.testJInstant(calculated, "String");
        calculated = Ut.aiJValue("2019-12-26T07:32:37.309Z");
        this.testJInstant(calculated, "String");

        // -------------  Number --------------
        /*
         * -> java.lang.Integer
         */
        System.out.println("Integer");
        calculated = Ut.aiJValue("123", Integer.class);
        this.testJInt(calculated, "String");
        calculated = Ut.aiJValue("AB", Integer.class);
        this.testJInt(calculated, "String");

        calculated = Ut.aiJValue(123L, Integer.class);
        this.testJInt(calculated, "Long");

        calculated = Ut.aiJValue(123.44, Integer.class);
        this.testJInt(calculated, "Double");

        calculated = Ut.aiJValue(12456.33f, Integer.class);
        this.testJInt(calculated, "Float");

        calculated = Ut.aiJValue(true, Integer.class);
        this.testJInt(calculated, "Boolean");
        /*
         * -> java.lang.Long
         */
        System.out.println("Long");
        calculated = Ut.aiJValue("123", Long.class);
        this.testJLong(calculated, "String");
        calculated = Ut.aiJValue("EX", Long.class);
        this.testJLong(calculated, "String");

        calculated = Ut.aiJValue(123, Long.class);
        this.testJLong(calculated, "Integer");

        calculated = Ut.aiJValue(123.44, Long.class);
        this.testJLong(calculated, "Double");

        calculated = Ut.aiJValue(12456.33f, Long.class);
        this.testJLong(calculated, "Float");

        calculated = Ut.aiJValue(true, Long.class);
        this.testJLong(calculated, "Boolean");
        /*
         * -> java.lang.Short
         */
        System.out.println("Short");
        calculated = Ut.aiJValue("123", Short.class);
        this.testJShort(calculated, "String");
        calculated = Ut.aiJValue("ET", Short.class);
        this.testJShort(calculated, "String");

        calculated = Ut.aiJValue(123, Short.class);
        this.testJShort(calculated, "Integer");

        calculated = Ut.aiJValue(123.44, Short.class);
        this.testJShort(calculated, "Double");

        calculated = Ut.aiJValue(12456.33f, Short.class);
        this.testJShort(calculated, "Float");

        calculated = Ut.aiJValue(true, Short.class);
        this.testJShort(calculated, "Boolean");

        // -------------  Float --------------
        /*
         * -> java.lang.Float
         */
        System.out.println("Float");
        calculated = Ut.aiJValue("123", Float.class);
        this.testJFloat(calculated, "String");
        calculated = Ut.aiJValue("A.X", Float.class);
        this.testJFloat(calculated, "String");

        calculated = Ut.aiJValue(123, Float.class);
        this.testJFloat(calculated, "Integer");

        calculated = Ut.aiJValue(123.44, Float.class);
        this.testJFloat(calculated, "Double");

        calculated = Ut.aiJValue(12456L, Float.class);
        this.testJFloat(calculated, "Long");

        calculated = Ut.aiJValue(true, Float.class);
        this.testJFloat(calculated, "Boolean");

        /*
         * -> java.lang.Double
         */
        System.out.println("Double");
        calculated = Ut.aiJValue("123.44", Double.class);
        this.testJDouble(calculated, "String");
        calculated = Ut.aiJValue("ET.X", Double.class);
        this.testJDouble(calculated, "String");

        calculated = Ut.aiJValue(123, Double.class);
        this.testJDouble(calculated, "Integer");

        calculated = Ut.aiJValue(123.44f, Double.class);
        this.testJDouble(calculated, "Float");

        calculated = Ut.aiJValue(12456L, Double.class);
        this.testJDouble(calculated, "Long");

        calculated = Ut.aiJValue(true, Double.class);
        this.testJDouble(calculated, "Boolean");

        /*
         * -> java.lang.Boolean
         */
        System.out.println("Boolean");
        calculated = Ut.aiJValue("true", Boolean.class);
        this.testJBoolean(calculated, "String");

        calculated = Ut.aiJValue(123, Boolean.class);
        this.testJBoolean(calculated, "Integer");

        calculated = Ut.aiJValue(123.44f, Boolean.class);
        this.testJBoolean(calculated, "Float");

        calculated = Ut.aiJValue(12456L, Boolean.class);
        this.testJBoolean(calculated, "Long");

    }

    /*
     * Java data conversation
     */
    @Test
    public void testValue() {
        final Object data = Ut.aiValue("2019-12-27T09:50:37.702Z", String.class);
        System.out.println(data);

        final Object value = Ut.aiJValue("2011-12-12", String.class);
        System.out.println(value);
    }
}
