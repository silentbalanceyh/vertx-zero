# Advanced usage for Envelop

This chapter will introduce the usage for completed example in our project, it came from a app backend development based
on zero system.

* `Version > 0.4.4`
* `Mongo Client`

The method to use event bus is the sample version, please refer [Event Bus sample version](zero-ebs.md) to check
more details about how to use event bus in another way instead of complex up.god.file structure.

## 0. File List

* `com.tlk.topic.api.TopicApi`：( Interface ) The interface definition for restful endpoint.
* `com.tlk.topic.api.TopicWorker`：The worker method definition to consume Envelop that came from `@Address` defined
  in `TopicApi` directly.
* `com.tlk.topic.api.TopicStub`：( Interface ) The service layer interface definition.
* `com.tlk.topic.api.TopicService`：The service interface implementation classes.

Finally this service up.god.file structure should be as following, here ignored `Topic` domain class definition, it's
sample **POJO**:

![ZeroTlk](image/zero-tlk.png)

The request flow will be in following:

```
( Request ) -> Api -> ( Event Bus ) -> Worker -> Stub -> Service
```

## 1. Address Management

In this project, the address must be managed by constant up.god.file to avoid wrong user operations. It's defined
in `com.tlk.up.god.cv.Addr` interface as constant values:

```java
public interface Addr {
    // ......

    String TOPIC_HOT = "TLK://TOPIC/HOT";

    String TOPIC_LATEST = "TLK://TOPIC/LATEST";

    String TOPIC_MY = "TLK://TOPIC/MY";
}
```

## 2. TopicApi

It's interface annotated with `@EndPoint`, the source code is as following:

```java
package com.tlk.topic.api;

import com.tlk.up.god.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/topic")
@EndPoint
public interface TopicApi {

    @GET
    @Path("/hot/{limit}")
    @Address(Addr.TOPIC_HOT)
    String top(@PathParam("limit") final Integer limit);

    @GET
    @Path("/latest/{limit}")
    @Address(Addr.TOPIC_LATEST)
    String latest(@PathParam("limit") final Integer limit);

    @GET
    @Path("/my/{limit}")
    @Address(Addr.TOPIC_MY)
    String my(@PathParam("limit") final Integer limit,
              @HeaderParam("X-User-Id") final String userId);

}
```

Be careful about following points:

1. This class definition is java interface instead of class, it means that you could ignore the implementation.
2. Hibernate validation is **ignored** in this mode, there is no proxy implementation class to validate parameters, in
   this kind of situation you can use `@Codex` instead.

## 3. TopicWorker

```java
package com.tlk.topic.api;

import com.tlk.up.god.cv.Addr;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.kidd.Rapider;

import jakarta.inject.Inject;

@Queue
public class TopicWorker {

    @Inject
    private transient TopicStub stub;

    @Address(Addr.TOPIC_HOT)
    public void hot(final Message<Envelop> message) {
        final Integer limit = Rapider.getInt(message, 0);
        stub.query(new JsonObject(), getOptions("subscribers", limit), message);
    }

    @Address(Addr.TOPIC_LATEST)
    public void latest(final Message<Envelop> message) {
        final Integer limit = Rapider.getInt(message, 0);
        stub.query(new JsonObject(), getOptions("createdTime", limit), message);
    }

    @Address(Addr.TOPIC_MY)
    public void my(final Message<Envelop> message) {
        final Integer limit = Rapider.getInt(message, 0);
        final String userId = Rapider.getString(message, 1);
        stub.query(new JsonObject().put("ownerId", userId),
                getOptions("createdTime", limit), message);
    }

    private FindOptions getOptions(final String sortField,
                                   final Integer limit) {
        final FindOptions options = new FindOptions();
        options.setLimit(limit);
        final JsonObject sort = new JsonObject().put(sortField, -1);
        options.setSort(sort);
        return options;
    }
}
```

Be careful about following code

```java
        final Integer limit = Rapider.getInt(message, 0);
        final String userId = Rapider.getString(message, 1);
```

It's new api that provided by `io.vertx.up.kidd.Rapider`, kidd means `Kidd`, this name refer to a person "Kid the
Phantom Thief", it means that in this request mode, zero system hidden the implementation just like a thief to steal the
request data from agent interface to worker directly, it skipped the agent implementation, also `Rapider` hidden the
details that how to convert the request data to expected params. Above code we could get path variable `limit` and
header parameter `X-User-Id`.

## 4. TopicStub

```java
package com.tlk.topic.api;

import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.up.commune.Envelop;

public interface TopicStub {

    void query(final JsonObject filter,
               final FindOptions options,
               final Message<Envelop> message);
}
```

## 5. TopicService

```java
package com.tlk.topic.api;

import com.tlk.atom.Topic;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.FindOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.up.commune.Envelop;
import io.vertx.up.kidd.Heart;

import jakarta.inject.infix.Mongo;

public class TopicService implements TopicStub {

    @Mongo
    private transient MongoClient client;

    @Override
    public void query(final JsonObject filter,
                      final FindOptions options,
                      final Message<Envelop> message) {
        // 1. Mongo Client to query by filter.
        client.findWithOptions(Topic.TABLE, filter, options, res -> {
            // 2. Build response.
            final Envelop envelop = Heart.getReacts(getClass())
                    .connect(res).result().to();
            message.reply(envelop);
        });
    }
}
```

Here provide another response building called `Heart`, it could help us to build expected result rapidly such as
following:

```java
final Envelop envelop = Heart.getReacts(getClass())
                    .connect(res).result().to();
```

1. Once there are errors, 500 Server Internal Error will be filled into Envelop.
2. The `_id` will be replaced with `key` in request/response because React will use `key` as primary key, but mongo will
   use `_id` as primary key, also you can replace `Spy` with connect `Spy<T>` to set this code logical.
3. The result() will build `JsonArray` data structure and returned the list result as expected.

## Summary

We will add more features into zero system focus on how to normalize request/response working flow, also limit the
developer random operations, but you also could ignore above specifical code to implement your code logical by yourself,
it's free to developers. Only one point is that be sure the uniform model used `Envelop`. Please refer reference to
check Uniform Resource Model: [How to use Envelop ( Uniform Resource Model )](zero-envelop.md). 