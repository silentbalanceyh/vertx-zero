# JSR340 Multi Filters

This tutorial we'll modify the demo that has been described in previous tutorial to write multi filters in zero system,
in this kind of situation we could manage all the filters before your API, this feature is more useful and powerful.

Demo projects:

* **Standalone - 6084**: `up-tethys`

## 1. Source Code

### 1.1. API

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
        final String key1 = envelop.context("key1", String.class);
        return Future.succeededFuture(new JsonObject().put("key", key).put("key1", key1));
    }
}
```

### 1.3. Filters

Here are two filters in this example, we'll manage all the filters by `io.vertx.up.annotations.Ordered`, The default
order value is 0, it means that all the filters will be triggered in sequence by order.

**FirstFilter**

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

**SecondFilter**

```java
package up.god.micro.filter;

import io.vertx.core.VertxException;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.up.annotations.Ordered;
import io.vertx.up.backbone.filter.HttpFilter;

import javax.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/api/jsr340/*")
@Ordered(2)
public class SecondFilter extends HttpFilter {

  @Override
  public void doFilter(final HttpServerRequest request,
                       final HttpServerResponse response)
          throws IOException, VertxException {
    System.out.println("Second Filter");
    this.put("key1", "Second Filter");
  }
}
```

## 2. Testing

Then you can test this demo:

**URL** : http://localhost:6084/api/jsr340/worker

**Method**: POST

**Request** :

```json
{
	"username":"Filter"
}
```

**Response** :

```json
{
    "data": {
        "key": "First Filter",
        "key1": "Second Filter"
    }
}
```

### 3. Summary

In this demo we defined 2 filters in sequence to implement the
whole [Chain of Responsibility](https://en.wikipedia.org/wiki/Chain-of-responsibility_pattern) pattern. In zero system,
please be careful about the whole filter chain:

* All the filters could be managed by `io.vertx.up.annotations.Ordered`.
* The data could be passed by `RoutingContext` instead of other form, you can put the data into `RoutingContext`
  by `put`.
* In Sender, you should extract data by `@ContextParam` or get `RoutingContext` reference to process it by yourself.
* In Consumer, you can call `Envelop.context` api to get the data from `RoutingContext`.





