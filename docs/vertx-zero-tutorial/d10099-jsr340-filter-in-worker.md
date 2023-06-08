# JSR340 Filter in Worker

This tutorial will continue to introduce how to use JSR340 @WebFilter in zero system. Here are some points that should
be mentioned because of some limitation between JSR340 and Vert.x:

* All the filters must implement `io.vertx.up.container.filter.Filter` instead of JSR340;
* We recommend developers extend from `io.vertx.up.container.filter.HttpFilter` directly because there are some abstract
  implementations in parent filters.
* Please be careful of the signature of `doFilter` especially focus on the parameter types.

Demo projects:

* **Standalone - 6084**: `up-tethys`

This tutorial will describe the usage of Filters in Consumer \( Worker \).

## 1. Source Code

### 1.1. Api

```java
package up.god.micro.filter;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import jakarta.ws.rs.BodyParam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public interface FilterApi {

    @POST
    @Path("/jsr340/worker")
    @Address("ZERO://JSR340/WORKER")
    JsonObject filter(@BodyParam final JsonObject data);
}
```

### 1.2. Consumer

```java
package up.god.micro.filter;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class FilterWorker {

    @Address("ZERO://JSR340/WORKER")
    public Future<JsonObject> work(final Envelop envelop) {
        final String key = envelop.context("key", String.class);
        return Future.succeededFuture(new JsonObject().put("key", key));
    }
}
```

### 1.3. Filter

> Here the code is the same as agent as following

```java
package up.god.micro.filter;

import io.vertx.core.VertxException;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.up.backbone.filter.HttpFilter;

import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/api/jsr340/*")
public class FirstFilter extends HttpFilter {

  @Override
  public void doFilter(final HttpServerRequest request,
                       final HttpServerResponse response)
          throws IOException, VertxException {
    System.out.println("First Filter");
    this.put("key", "First Filter");
  }
}

```

## 2. Testing

Then you can test this feature as following:

**URL** : http://localhost:6084/api/jsr340/worker

**Method** : POST

**Response** :

```json
{
    "data": {
        "key": "First Filter"
    }
}
```

Here please be careful about the api of `Envelop.context`, you can extract the data from this api include any data type,
the signature of this method is as following:

```java
public <T> T context(final String key, final Class<T> clazz)
```

## 3. Summary

Based on current tutorial you can know how to use filters in Consumer \( Worker \) of zero system, and now you can set
any code logical before your API.



