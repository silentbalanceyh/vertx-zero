package io.vertx.quiz.example;

import io.horizon.eon.VString;
import jakarta.ws.rs.GET;

public class RMethod1 {
    @GET
    public String sayHell() {
        return VString.EMPTY;
    }

    public String sayHell1() {
        return null;
    }
}
