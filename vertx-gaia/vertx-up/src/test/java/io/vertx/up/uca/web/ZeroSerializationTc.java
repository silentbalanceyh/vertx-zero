package io.vertx.up.uca.web;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.exception.web._400ParameterFromStringException;
import io.vertx.up.runtime.ZeroSerializer;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

enum TestEnum {
    STRING, OBJECT
}

public class ZeroSerializationTc extends ZeroBase {

    @SuppressWarnings("unchecked")
    private <T> T get(final Class<?> clazz, final String literal) {
        final T value = (T) ZeroSerializer.getValue(clazz, literal);
        this.logger().info("[TEST] Data = {0}, Type = {1}.", value, clazz.getName());
        return value;
    }

    @Test
    public void testInt1() {
        this.get(Integer.class, "1234");
    }

    @Test
    public void testInt2() {
        this.get(int.class, "123456");
    }

    @Test
    public void testShort1() {
        this.get(short.class, "12345");
    }

    @Test
    public void testShort2() {
        this.get(Short.class, "12345");
    }

    @Test
    public void testLong1() {
        this.get(Long.class, "12345123123");
    }

    @Test
    public void testLong2() {
        this.get(long.class, "1234512312312");
    }

    @Test
    public void testEnum1() {
        this.get(TestEnum.STRING.getClass(), "STRING");
    }

    @Test
    public void testEnum2() {
        this.get(TestEnum.STRING.getClass(), "OBJECT");
    }

    @Test
    public void testFloat1() {
        this.get(float.class, "12345.33");
    }

    @Test
    public void testFloat2() {
        this.get(Float.class, "12345.33");
    }

    @Test
    public void testDouble1() {
        this.get(double.class, "12345.33");
    }

    @Test
    public void testDouble2() {
        this.get(Double.class, "12345.33");
    }

    @Test
    public void testDecimal() {
        this.get(BigDecimal.class, "12345.33");
    }

    @Test(expected = _400ParameterFromStringException.class)
    public void testBoolean1() {
        this.get(boolean.class, "TEST");
    }

    @Test
    public void testBoolean2() {
        this.get(Boolean.class, "TRUE");
    }

    @Test
    public void testBoolean3() {
        this.get(Boolean.class, "false");
    }

    @Test
    public void testJObject() {
        this.get(JsonObject.class, "{\"name\":\"Lang\"}");
    }

    @Test
    public void testJArray() {
        this.get(JsonArray.class, "[{\"name\":\"Lang\"}]");
    }

    @Test
    public void testBytes1() {
        this.get(Byte[].class, "Test Hello");
    }

    @Test
    public void testBytes2() {
        this.get(byte[].class, "Test Hello");
    }

    @Test
    public void testStringBuffer() {
        this.get(StringBuffer.class, "Test StringBuffer");
    }

    @Test
    public void testStringBuilder() {
        this.get(StringBuilder.class, "Test StringBuilder");
    }

    @Test
    public void testDate1() {
        this.get(Date.class, "2018-12-09");
    }

    @Test
    public void testDate2() {
        this.get(Calendar.class, "2018-12-09");
    }

    @Test
    public void testString() {
        this.get(String.class, "1234");
    }

    @Test
    public void testSet() {
        this.get(Set.class, "[{\"name\":\"Lang\"}]");
    }

    @Test
    public void testArray() {
        this.get(List.class, "[{\"name\":\"Lang\"}]");
    }

    @Test
    public void testList() {
        this.get(ArrayList.class, "[{\"name\":\"Lang\"}]");
    }

    @Test
    public void testSet2() {
        this.get(TreeSet.class, "[{\"name\":\"Lang\"}]");
    }

    @Test
    public void testSet3() {
        this.get(Set.class, "[\n" +
            "\t{\n" +
            "\t\t\"username\":\"Lang\",\n" +
            "\t\t\"age\":33\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"username\":\"Xi\",\n" +
            "\t\t\"email\":\"lang.xi@hpe.com\"\n" +
            "\t}\n" +
            "]");
    }
}
