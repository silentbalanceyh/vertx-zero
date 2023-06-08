# JSR340 Filter in Agent

This tutorial will describe the usage of @WebFilter which will belong to JSR340 to
implement [Chain of Responsibility](https://en.wikipedia.org/wiki/Chain-of-responsibility_pattern) pattern in zero
system, because there are some conflicts between Servlet and Vert.x, in zero system it won't implement @WebServlet
annotation but @WebFilter/@WebListener instead.

Demo projects:

* **Standalone - 6084**: `up-tethys`

In zero system there are two places to programming: Sender/Consumer, in this situation we'll introduce both modes in our
tutorials.

## 1. Source Code

### 1.1. Actor

At first we'll implement an actor in our project that has been introduced before.

```java
package up.god.micro.filter;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;

import jakarta.ws.rs.BodyParam;
import jakarta.ws.rs.ContextParam;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/api")
@EndPoint
public class FilterAgent {

    @POST
    @Path("/jsr340/agent")
    public JsonObject filter(@BodyParam final JsonObject data,
                             @ContextParam("key") final String filtered) {
        return new JsonObject().put("filter", filtered)
            .mergeIn(data);
    }
}
```

### 1.2. Filter

Then you can write the filter as following:

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

Here are some points that you should be careful:

* In the filter, you should use annotation `javax.servlet.annotations.WebFilter` to mark filter component
* Once point that to be careful is that the class must inherit from `io.vertx.up.container.filter.HttpFilter`.
* The parameters of `doFilter` is `HttpServerRequest/HttpServerResponse` in vert.x instead of JSR340.
* When your code flow went to Actor, you should use `@ContextParam` to extract the data.

## 2. Testing

**URL** : [http://localhost:6084/api/jsr340/agent](http://localhost:6084/api/jsr340/agent)

**Method**: POST

**Request** :

```json
{
	"username":"Lang"
}
```

**Response** :

```json
{
    "data": {
        "filter": "First Filter",
        "username": "Lang"
    }
}
```

From above demo you can see that we have got the data in actor that had been put in filter component.

## 3. Summary

Current tutorial described the filter usage that belong to JSR340, the filter will be triggered automatically if the uri
is matching. You can do anythings in filter such as data conversion, request modification or reject current restful
request.

