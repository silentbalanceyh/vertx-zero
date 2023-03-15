# Mime type supported

## 1. Types

### 1.1. Mime Types

* Text: `text/*`
* Multipart: `multipart/*`
* Application: `application/*`
* Message: `message/*`
* Image: `image/*`
* Audio: `audio/*`
* Video: `video/*`

### 1.2. Java Annotations

* [x] `javax.ws.rs.QueryParam`
* [x] `javax.ws.rs.FormParam`
* [ ] `javax.ws.rs.MatrixParam`
* [x] `javax.ws.rs.PathParam`
* [x] `javax.ws.rs.HeaderParam`
* [x] `javax.ws.rs.CookieParam`
* [x] `jakarta.ws.rs.BodyParam`
* [x] `jakarta.ws.rs.StreamParam`
* [x] `jakarta.ws.rs.SessionParam`

## 2. Java Types

### 2.1. Primary

Ignored `java.lang.char` and `java.lang.byte` because it's not common.

* `java.lang.int`
* `java.lang.short`
* `java.lang.long`
* `java.lang.double`
* `java.lang.float`
* `java.lang.boolean`

### 2.2. Wrapper

* `java.lang.Integer`
* `java.lang.Short`
* `java.lang.Long`
* `java.lang.Double`
* `java.lang.Float`
* `java.lang.Enum`
* `java.lang.Boolean`
* `java.lang.String`
* `java.lang.StringBuilder`
* `java.lang.StringBuffer`
* `java.math.BigDecimal`
* `java.util.Date`
* `java.util.Calendar` - Abstract Class
* `java.util.Set` - Interface
* `java.util.List` - Interface
* `java.util.Collection` - Interface
* `io.vertx.core.json.JsonArray`
* `io.vertx.core.json.JsonObject`
* `io.vertx.core.buffer.Buffer`
* `java.lang.byte[]` - Array
* `java.lang.Byte[]` - Array
* `Pojo Object - T`
* `Pojo Collection - Collection<T>`
* `Pojo Array - T[]`

### 2.3. Typed \( No annotation \)

* `io.vertx.ext.web.Session`
* `io.vertx.core.http.HttpServerRequest`
* `io.vertx.core.http.HttpServerResponse`
* `io.vertx.core.eventbus.EventBus`
* `io.vertx.core.Vertx`
* `io.vertx.ext.auth.User`
* `io.vertx.ext.web.RoutingContext`

## 3. Specification Definition

![matrix](image/mime-matrix.png)

