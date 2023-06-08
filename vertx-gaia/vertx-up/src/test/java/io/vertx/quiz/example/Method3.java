package io.vertx.quiz.example;

import jakarta.ws.rs.Path;

@Path("api")
public class Method3 {

    @Path("test////:name")
    public void test() {
    }

    @Path("///test/:id")
    public void test1() {
    }
}
