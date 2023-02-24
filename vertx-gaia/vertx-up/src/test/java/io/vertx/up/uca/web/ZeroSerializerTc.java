package io.vertx.up.uca.web;

import io.vertx.core.json.JsonObject;
import io.vertx.quiz.ZeroBase;
import io.vertx.up.runtime.ZeroSerializer;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

enum KTest {
    STRING,
    VALUE
}

public class ZeroSerializerTc extends ZeroBase {

    private <T> void put(final T input) {
        final JsonObject data = new JsonObject();
        data.put("request", ZeroSerializer.toSupport(input));
        this.logger().info("[TEST] Data = {0}, Type = {1}.",
            data.encode(), null == input ? "null" : input.getClass().getName());
    }

    @Test
    public void testInt() {
        this.put(2);
    }

    @Test
    public void testInt1() {
        this.put(Integer.valueOf("3"));
    }

    @Test
    public void testLong() {
        this.put(Long.valueOf("3"));
    }

    @Test
    public void testLong2() {
        this.put(22L);
    }

    @Test
    public void testNull() {
        this.put(null);
    }

    @Test
    public void testDecimal() {
        this.put(3.33f);
        this.put(new BigDecimal("2.44"));
    }

    @Test
    public void testDecimal2() {
        this.put(2.33);
    }

    @Test
    public void testDecimal3() {
        this.put(3.33f);
    }

    @Test
    public void testDate() {
        this.put(new Date());
    }

    @Test
    public void testDate1() {
        this.put(Calendar.getInstance());
    }

    @Test
    public void testDate2() {
        this.put(LocalDate.now());
        this.put(LocalTime.now());
        this.put(LocalDateTime.now());
    }

    @Test
    public void testUser() {
        this.put(new User());
    }

    @Test
    public void testUserList() {
        final List<User> users = new ArrayList<>();
        users.add(new User());
        this.put(users);
    }

    @Test
    public void testBuffer() {
        final StringBuffer buffer = new StringBuffer("Buffer");
        this.put(buffer);
    }

    @Test
    public void testSet() {
        final Set<String> sets = new HashSet<>();
        sets.add("Hello");
        this.put(sets);
    }

    @Test
    public void testUsers() {
        final User[] users = new User[2];
        users[0] = new User();
        this.put(users);
    }

    @Test
    public void testStringArray() {
        final String[] str = new String[2];
        str[0] = "Hello";
        str[1] = "Lang";
        this.put(str);
    }

    @Test
    public void testBytes() {
        final byte[] bytes = new byte[16];
        this.put(bytes);
    }

    @Test
    public void testEnum() {
        this.put(KTest.STRING);
    }
}

class User {
    String name = "Lang";
    String email = "silentbalanceyh@126.com";

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
