# D10055 - Utility X, Interface Style

This chapter we'll go back to interface style again, interface style is designed for most frequent situations:

**Agent Mode**

```shell
Api -> Sender ( Agent Area ) -> ( Event Bus ) -> Consumer ( Worker Area )
```

**Interface Style**

```shell
Api -> ( Event Bus ) -> Consumer ( Worker Area )
```

Actually interface style is often used in our real projects and based on our experience it's very popular to face real
business requirements.

## 1. Introduction

In zero system we have designed many methods to extract data from api interface, for different data types we defined the
common usage methods:

* `static String getString(Message<Envelop> message)`
* `static String getString(Envelop envelop)`
* `static String getString1(Message<Envelop> message)`
* `static String getString1(Envelop envelop)`
* `static String getString2(Message<Envelop> message)`
* `static String getString2(Envelop envelop)`
* `static String getString3(Message<Envelop> message)`
* `static String getString3(Envelop envelop)`
* `static String getString(Message<Envelop> message, int index)`
* `static String getString(Envelop envelop, int index)`
* `static JsonObject getJson(Message<Envelop> message)`
* `static JsonObject getJson(Envelop envelop)`
* `static JsonObject getJson1(Message<Envelop> message)`
* `static JsonObject getJson1(Envelop envelop)`
* `static JsonObject getJson2(Message<Envelop> message)`
* `static JsonObject getJson2(Envelop envelop)`
* `static JsonObject getJson3(Message<Envelop> message)`
* `static JsonObject getJson3(Envelop envelop)`
* `static JsonObject getJson(Message<Envelop> message, int index)`
* `static JsonObject getJson(Envelop envelop, int index)`
* `static Integer getInteger(Message<Envelop> message)`
* `static Integer getInteger(Envelop envelop)`
* `static Integer getInteger1(Message<Envelop> message)`
* `static Integer getInteger1(Envelop envelop)`
* `static Integer getInteger2(Message<Envelop> message)`
* `static Integer getInteger2(Envelop envelop)`
* `static Integer getInteger3(Message<Envelop> message)`
* `static Integer getInteger3(Envelop envelop)`
* `static Integer getInteger(Message<Envelop> message, int index)`
* `static Integer getInteger(Envelop envelop, int index)`
* `static Long getLong(Message<Envelop> message)`
* `static Long getLong(Envelop envelop)`
* `static Long getLong1(Message<Envelop> message)`
* `static Long getLong1(Envelop envelop)`
* `static Long getLong2(Message<Envelop> message)`
* `static Long getLong2(Envelop envelop)`
* `static Long getLong3(Message<Envelop> message)`
* `static Long getLong3(Envelop envelop)`
* `static Long getLong(Message<Envelop> message, int index)`
* `static Long getLong(Envelop envelop, int index)`
* `static <T> T getT(Message<Envelop> message)`
* `static <T> T getT(Envelop envelop)`
* `static <T> T getT1(Message<Envelop> message)`
* `static <T> T getT1(Envelop envelop)`
* `static <T> T getT2(Message<Envelop> message)`
* `static <T> T getT2(Envelop envelop)`
* `static <T> T getT3(Message<Envelop> message)`
* `static <T> T getT3(Envelop envelop)`
* `static <T> T getT(Message<Envelop> message, int index)`
* `static <T> T getT(Envelop envelop, int index)`

Here are the basic rules to understand above methods:

1. The parameters are two types: the one is `Message<Envelop>`, and another one is `Envelop`, it's for different style
   writing, you can refer previous **Consumer** writing style to know which kind of method you want to choose. In our
   real project we often use `Message<Envelop>` type.
2. There are five types for your to extract parameters: String, JsonObject, Long, Integer and generic type T.
3. The last thing is that about the index, the index means that the definition sequence in your interface.

Please consider following codes

```java
String sayHello(String first, Integer second, String third);
```

If you defined above method in your api interface, it means that you should have a method to extract the three
parameters: `first, second, third`, how to do it? You can do with Utility X as following:

* Call `getString(Message<Envelop>)` to extract "first";
* Call `getInteger1(Message<Envelop>)` to extract "second";
* Call`getString2(Message<Envelop>)` to extract "third";

Here the digit in the method name is the index of your definition parameters, in our design we consider that your
interface api should not contain more than 4 parameters, then you could use `getX, getX1, getX2, getX3` to extract all
the parameter data, it could avoid you provide wrong index. If your parameter length is greater than 4, you can use the
method with `index` continue to extract the rest parameters.

## 2. Source Code

> The codes came from micro hotel system

### 2.1. Sender

```java
package com.needee.micro.user;

import com.needee.up.god.cv.Addr;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Codex;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/oth")
@EndPoint
public interface SelfApi {

    @POST
    @Path("password")
    @Address(Addr.OTH_PASSWORD)
    JsonObject modifyPassword(@BodyParam @Codex JsonObject params);
}

```

### 2.2. Consumer

```java
package com.needee.micro.user;

import com.needee.up.god.cv.Addr;
import com.needee.micro.login.LoginStub;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

import javax.inject.Inject;

@Queue
public class SelfWorker {

    @Inject
    private transient LoginStub stub;

    @Address(Addr.OTH_PASSWORD)
    public void change(final Message<Envelop> message) {
        final JsonObject data = Ux.getJson(message);
        this.stub.modifyPassword(data.getString("key"),
                        data.getString("opassword"), data.getString("npassword"))
                .setHandler(Ux.toHandler(message));
    }
}
```

## 3. Summary

From above code you could see that we used `Ux.getJson` api to get the data that you defined in interface. We also need
to mention that, interface style could not support JSR303 Bean Validation, but it could support our Zero JSR303
validation. It's the core point that you should know.



