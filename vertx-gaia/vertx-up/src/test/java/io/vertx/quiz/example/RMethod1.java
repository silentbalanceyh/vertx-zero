package io.vertx.quiz.example;

import io.vertx.up.eon.Strings;
import jakarta.ws.rs.GET;

public class RMethod1 {
    @GET
    public String sayHell() {
        return Strings.EMPTY;
    }

    public String sayHell1() {
        return null;
    }
}
