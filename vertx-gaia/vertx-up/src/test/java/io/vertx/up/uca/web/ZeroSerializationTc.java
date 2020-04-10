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
        getLogger().info("[TEST] Data = {0}, Type = {1}.", value, clazz.getName());
        return value;
    }

    @Test
    public void testInt1() {
        get(Integer.class, "1234");
    }

    @Test
    public void testInt2() {
        get(int.class, "123456");
    }

    @Test
    public void testShort1() {
        get(short.class, "12345");
    }

    @Test
    public void testShort2() {
        get(Short.class, "12345");
    }

    @Test
    public void testLong1() {
        get(Long.class, "12345123123");
    }

    @Test
    public void testLong2() {
        get(long.class, "1234512312312");
    }

    @Test
    public void testEnum1() {
        get(TestEnum.STRING.getClass(), "STRING");
    }

    @Test
    public void testEnum2() {
        get(TestEnum.STRING.getClass(), "OBJECT");
    }

    @Test
    public void testFloat1() {
        get(float.class, "12345.33");
    }

    @Test
    public void testFloat2() {
        get(Float.class, "12345.33");
    }

    @Test
    public void testDouble1() {
        get(double.class, "12345.33");
    }

    @Test
    public void testDouble2() {
        get(Double.class, "12345.33");
    }

    @Test
    public void testDecimal() {
        get(BigDecimal.class, "12345.33");
    }

    @Test(expected = _400ParameterFromStringException.class)
    public void testBoolean1() {
        get(boolean.class, "TEST");
    }

    @Test
    public void testBoolean2() {
        get(Boolean.class, "TRUE");
    }

    @Test
    public void testBoolean3() {
        get(Boolean.class, "false");
    }

    @Test
    public void testJObject() {
        get(JsonObject.class, "{\"name\":\"Lang\"}");
    }

    @Test
    public void testJArray() {
        get(JsonArray.class, "[{\"name\":\"Lang\"}]");
    }

    @Test
    public void testBytes1() {
        get(Byte[].class, "Test Hello");
    }

    @Test
    public void testBytes2() {
        get(byte[].class, "Test Hello");
    }

    @Test
    public void testStringBuffer() {
        get(StringBuffer.class, "Test StringBuffer");
    }

    @Test
    public void testStringBuilder() {
        get(StringBuilder.class, "Test StringBuilder");
    }

    @Test
    public void testDate1() {
        get(Date.class, "2018-12-09");
    }

    @Test
    public void testDate2() {
        get(Calendar.class, "2018-12-09");
    }

    @Test
    public void testString() {
        get(String.class, "1234");
    }

    @Test
    public void testSet() {
        get(Set.class, "[{\"name\":\"Lang\"}]");
    }

    @Test
    public void testArray() {
        get(List.class, "[{\"name\":\"Lang\"}]");
    }

    @Test
    public void testList() {
        get(ArrayList.class, "[{\"name\":\"Lang\"}]");
    }

    @Test
    public void testSet2() {
        get(TreeSet.class, "[{\"name\":\"Lang\"}]");
    }

    @Test
    public void testSet3() {
        get(Set.class, "[\n" +
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
