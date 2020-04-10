package io.vertx.quiz.example;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@EndPoint
public class User {

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
