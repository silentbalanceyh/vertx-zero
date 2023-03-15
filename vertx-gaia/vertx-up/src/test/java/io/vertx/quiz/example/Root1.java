package io.vertx.quiz.example;

import jakarta.ws.rs.Path;

/**
 * 1. Root: api
 */
@Path("api")
public class Root1 {

    @Path("/test")
    public void test() {
    }
}
