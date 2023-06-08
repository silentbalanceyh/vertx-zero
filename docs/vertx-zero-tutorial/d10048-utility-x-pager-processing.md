# Utility X, Pager Processing

In zero system, it provide normalized object to store pager information, it's name is `io.horizon.uca.qr.Pager`,
this object could help you to do pagination. this object contains following apis for extract different metadata:

* `getStart()`: Get the start index of current pager. \( \( page - 1 \) \* size \)
* `getPage()`: Get total pages of current pager.
* `getEnd()`: Get the end index of current page \( page \* size \).
* `getSize()`: Get the size of each page.

The pagination parameters may came from http request query string, body or other place, in this kind of situation, the
sender could take this place to do parameter normalization.

> In "Interface Style", the sender code is ignored, but in current chapter we need this sender class to do useful
> things.

## 1. Source Code

The code segment came from real projects instead of demos

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

Here we defined an interface to be annotated as EndPoint, here the parameters `page` and `size` came from query string
and contains the default values. Then let's see how to use Utility X to process the pager parameters:

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

In above examples we used the method `toPagerJson`, this method return value is not pager, but JsonObject instead, in
this method, the Actor \( Sender \) has done following things:

```shell
# request url -> xxxxx?videoId=a&page=1&size=20
# The converted data should be as following:
{
    "videoId":a,
    "pager":{
        "size":20,
        "page":1
    }
}
```

Then the consumer could consume normalized Json data before it called service interfaces.

In your worker you can do as following:

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

You can ignored the code logical details of this example, but here you could see we called: `toPager(JsonObject)` api to
convert the params's pager node to valid Pager object.

## 2. Pager Apis

Actually zero system provide three standard pager related apis:

* `static JsonObject toPagerJson(int page, int size)`
* `static Pager toPager(int page, int size)`
* `static Pager toPager(JsonObject json)`

## 3. Summary

The pager object of `io.horizon.uca.qr.Pager` is defined by zero system and provide small interfaces to developer
to do normalized Pager building, in this situation you can finish any kind of pagination in zero system instead of
implement for each projects.

