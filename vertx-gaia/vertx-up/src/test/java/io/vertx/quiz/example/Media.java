package io.vertx.quiz.example;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@EndPoint
public class Media {

    @Produces(MediaType.APPLICATION_JSON)
    public void sayHello() {

    }

    @Consumes(MediaType.APPLICATION_JSON)
    public void sayH() {

    }
}
