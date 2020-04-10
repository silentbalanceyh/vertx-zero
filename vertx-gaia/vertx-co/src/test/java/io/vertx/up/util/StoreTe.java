package io.vertx.up.util;

import io.vertx.up.exception.heart.EmptyStreamException;
import io.vertx.up.exception.heart.JsonFormatException;
import io.vertx.quiz.StoreBase;
import org.junit.Test;

public class StoreTe extends StoreBase {

    @Test(expected = EmptyStreamException.class)
    public void testNone() {
        this.execJObject("zero.json", config -> {

        });
    }

    @Test(expected = JsonFormatException.class)
    public void testEmpty() {
        this.execJObject("format.json", config -> {

        });
    }
}
