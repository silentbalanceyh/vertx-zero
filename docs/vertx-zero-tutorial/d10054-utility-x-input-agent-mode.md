# Utility X Input, Agent Mode

Zero system support two different styles: Agent Mode / Interface Style, if we enable event bus we could see our web
request will go through following workflow:

**Agent Mode**

```shell
Api -> Sender ( Agent Area ) -> ( Event Bus ) -> Consumer ( Worker Area )
```

**Interface Style**

```shell
Api -> ( Event Bus ) -> Consumer ( Worker Area )
```

## 1. Zero Recommend

Here are the problems: we have explained how to get data from client, but when you send the data to event bus, we could
extract data from `Message<Envelop>` , `Envelop`, or use the Mode 5 Experimental extension to get data directly. But we
still recommend to use `Envelop` instead, in this situation we need to read data to our expected, this chapter will tell
you how to get input request data based on agent mode from EventBus. Based on this situation, we suggest developers obey
following rules:

* Use uniform resource model instead of user defined types.
* Use Utility X tool package to get the input data.

Zero system design is that to provide a framework to developer to do fast development, not only on the feature for
business requirement, but also to provide some small tips. Then we focus on Agent Mode only.

## 2. Source Code

> All the code segments came from real project of Video App

### 2.1. Api definition

```java
package com.tlk.micro.comment;

import com.tlk.infra.up.god.cv.Addr;
import com.tlk.infra.up.god.cv.InValid;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/comments")
@EndPoint
public interface IrCommentApi {

    @GET
    @Path("/list")
    @Address(Addr.COMMENTS_LIST_VIDEO)
    JsonObject list(
            @QueryParam("videoId") final String videoId,
            @QueryParam("page") @Min(value = 1, message = InValid.PAGE)
            @DefaultValue("1") final Integer page,
            @QueryParam("size") @Min(value = 1, message = InValid.SIZE)
            @DefaultValue("10") final Integer size
    );
}
```

### 2.2. Sender Code \( Actor \)

```java
package com.tlk.micro.comment;

import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.fn.Fn;

public class IrCommentActor implements IrCommentApi {

    @Override
    public JsonObject list(final String videoId,
                           final Integer page,
                           final Integer size) {
        final JsonObject params = new JsonObject();
        Fn.safeNull(() -> params.put("videoId", videoId), videoId);
        params.put("pager", Ux.toPagerJson(page, size));
        return params;
    }
}
```

Until now we could see that the Agent component contains `Api, Actor` codes and this component will get the request data
to merge into `JsonObject`, it means that we'll send this JsonObject to event bus in `Envelop` object, in our code we
only need to extract data from `Envelop`.

### 2.3. Consumer Code \( Worker \)

```java
package com.tlk.micro.comment;

import com.tlk.eon.TargetType;
import com.tlk.infra.up.god.cv.Addr;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.mod.plugin.mongo.MongoReadOpts;

import jakarta.inject.Inject;

@Queue
public class IrCommentWorker {

    @Inject
    private transient CommentStub stub;

    @Address(Addr.COMMENTS_LIST_VIDEO)
    public void list(final Message<Envelop> message) {
        final JsonObject params = Ux.getBody(message);
        final String targetId = params.getString("videoId");
        final JsonObject filter = new JsonObject()
                .put("targetId", targetId)
                .put("targetType", TargetType.VIDEO);
        this.stub.query(filter, MongoReadOpts.toFull(
                Ux.toPager(params.getJsonObject("pager")),
                Ux.toSorter("commentTime", false))
        ).setHandler(Ux.toHandler(message));
    }
}
```

From above code we could see that here we called `Ux.getBody` to extract the data from `Envelop` object, this method is
fixed return type method, you could extract the data with type, please refer following chapters.

## 3. Summary

Utility X provide the api to do this things as following:

* `JsonObject getBody(Message<Envelop> message)`
* `<T> T getBodyT(Message<envelop> message, Class<T> clazz)`

There are two apis that could extract data from `Message<Envelop>` , one for JsonObject and it's also recommended by
vert.x, another one for generic type and it's freedom usage.



