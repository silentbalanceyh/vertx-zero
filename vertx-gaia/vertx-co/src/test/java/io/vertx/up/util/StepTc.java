package io.vertx.up.util;

import io.vertx.core.json.JsonObject;
import io.vertx.quiz.EpicBase;
import org.junit.Test;

public class StepTc extends EpicBase {

    @Test
    public void testStep() {
        final JsonObject input = this.ioJObject("input.json");
        System.err.println(input.encodePrettily());
    }
}
