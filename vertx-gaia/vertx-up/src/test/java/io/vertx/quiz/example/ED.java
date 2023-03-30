package io.vertx.quiz.example;

import io.vertx.up.annotations.EndPoint;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@EndPoint
public class ED {

    @GET
    @Path("/hello")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String sayHello() {
        return "Hello";
    }

    public void test() {
    }
}
