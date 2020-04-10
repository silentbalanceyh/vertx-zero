# D10056 - Utility X Basic Future

Until now we have introduced all the Utility X tools for sender \( agent \), consumer \( worker \), and there are some more utility x tool that will be introduced in future, especially for `Future` object on service layer, here we listed all the topics and it won't introduced in current chapter, but in forward tutorial we'll introduce it when we met.

## 1. Rpc Future

```java
static <T> Future<JsonObject> thenRpc
    (final String name, final String address, final JsonObject params)
static <T> Future<JsonObject> thenRpc
    (final String name, final String address, final String field, final Object value)
```

Above two methods is for Rpc Future building, in zero system we recommend you to define service layer interface with Future style instead of others, it means that your service layer interface should be as following:

```java
package com.htl.micro.user;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface UserStub {

    Future<JsonObject> modify(
            String key,
            JsonObject data);
}
```

It means that we recommend you return `io.vertx.core.Future` instead of other type, there are two reasons:

1. The `Future` returned could let you consider the programming style from OO to FP;
2. The `Future` type is async mode, you also could use some native vert.x component such as SqlClient, MongoClient etc.

> In our Video App and Hotel Micro Platform, all the service interface are defined as above, the returned type is `Future` instead of other java types. That's why we provide Utility X tool to build different `Future`.

A method to call `thenRpc` may like following:

```java
    private Future<JsonArray> findCats(final String sigma) {
        final JsonObject params = new JsonObject().put("sigma", sigma);
        return Ux.thenRpc("htl-datum-ipc", "IPC://EVENT/DATUM/ATM/PAYTERM", params)
                .compose(item -> {
                    LOGGER.info("[ Rpc ] Category info {0}", null == item ? null : item.encode());
                    return Future.succeededFuture(null == item ?
                            new JsonArray() :
                            item.getJsonArray("data"));
                });
    }
```

For more details we'll introduce in Rpc Part, you should know that in zero system service communication is very simple and the codes are only around 10 lines.

## 2. Json Future

```java
static <T> Future<JsonArray> thenJsonMore(final List<T> list, final String pojo)
static <T> Future<JsonObject> thenJsonOne(final List<T> list, final String pojo)
static <T> Future<JsonObject> thenJsonOne(final T entity, final String pojo)
```

Above methods provide the interface to convert the result `T` or `List<T>` to Future with JsonObject, JsonArray, it's very useful because when you access database, you may use Jooq, Hibernate, Mybatis etc, all these database accessor may return `T` or `List<T>` instead of other java types \( Based on OO \), here above three methods could help you normalized the returned values to Future and passed to consumer, then in zero system the response will be standard.

## 3. Envelop Future

```java
static <T> Future<Envelop> thenMore(final List<T> list, final String pojo)
static <T> Future<Envelop> thenOne(final T entity, final String pojo)
```

Here these two methods could returned Future with Envelop, the uniform resource model in zero system. You can convert the result to the useful future directly.

## 4. Future Chain

```java
static <T> Future<T> thenGeneric(final Consumer<Future<T>> consumer)
```

This method is used to wrapper some async operations, actually in zero system when we develop some Mongo/Jooq tools, we often use this future to do conversion, the sample codes is as following \( Came from `UxMongo` \):

```java
    static Future<JsonObject> insert(final String collection, final JsonObject data) {
        return Ux.thenGeneric(future -> CLIENT.insert(collection, data, res -> {
            if (res.succeeded()) {
                LOGGER.debug(Info.MSG_INSERT, collection, data);
                future.complete(data);
            } else {
                LOGGER.debug(Info.MSG_INSERT, collection, null);
                future.complete();
            }
        }));
    }
```

Here we used `MongoClient` do to async operations, you can see that MongoClient signature is as following:

```java
MongoClient findOne(String collection, JsonObject query, JsonObject fields, 
    Handler<AsyncResult<JsonObject>> resultHandler)
```

From above method, we could see that there is no place to hold the returned value such as return JsonObject except we call resultHandler, that's why we provide Utility X to convert the Future to another outer Future. Here the `Consumer<Future>` could let you write the function to wrapper the async operations, but this Future could not returned the expected data structure that you wanted, the major method could convert this Future, then you can returned the results to **Consumer** component directly.

## 5. Summary

This chapter we have seen the basic Future usage in zero system utility x tool, then next chapter we'll focus on some complex Future building for more advanced use.



