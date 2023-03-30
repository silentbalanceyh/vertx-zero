package io.vertx.quiz.example;

import io.vertx.up.annotations.EndPoint;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@EndPoint
public class ED1 {

    @GET
    @Path("/hello")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String sayHello() {
        return "Hello";
    }

    @POST
    @Path("/hello")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces({MediaType.APPLICATION_JSON})
    public String sayHelloP() {
        return "Hello";
    }

    public void test() {
    }
}
