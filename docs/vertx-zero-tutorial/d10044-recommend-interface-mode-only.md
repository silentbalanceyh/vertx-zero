# Interface Style

This chapter will introduce another coding style that zero system supported, we call it "Interface Style", be careful
about this style for coding because it does not support JSR303, only Zero JSR303 could be supported by this style here.
This chapter will introduce interface style for more details. In this chapter we focus on **Api/Worker** only.

## 1. Source Code

### 1.1. Api Code

```java
package up.god.micro.styles;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api")
public interface FacadeApi {

    @GET
    @Path("/facade")
    @Address("ZERO://FACADE/STYLE")
    String sayFacade(
            @QueryParam("style") String style,
            @QueryParam("mode") String mode
    );
}
```

### 1.2. Worker Code

```java
package up.god.micro.styles;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class FacadeWorker {

    @Address("ZERO://FACADE/STYLE")
    public Future<JsonObject> testFacade(final Envelop envelop) {
        final String arg0 = Ux.getString(envelop);
        final String arg1 = Ux.getString1(envelop);
        return Future.succeededFuture(new JsonObject()
                .put("style", arg0)
                .put("mode", arg1));
    }
}
```

## 2. Testing

**URL** : http://localhost:6083/api/facade?mode=Interface&style=Lang

**Method** : GET

**Response** :

```json
{
    "data": {
        "style": "Lang",
        "mode": "Interface"
    }
}
```

## 3. Summary

Here we used Ux \( Belong to UtilityX package in zero system \), now you could ignore the Ux usage, we'll introduce in
forward tutorials. In this demo there is no **Actor** to implement api interface, but we still could pass the data to
worker thread to process it, that's why we call this style "Interface Style". It's useful in real projects because you'
ll find that most situations, it's not needed to require Actor class to do some business logicals.



