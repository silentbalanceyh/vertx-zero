# Vert.x Native, SharedData

Actually, shared client is not native client, but we wrapper a concept in vert.x named **SharedData** , this client used
this concept provide following features.

* Temporary storage of Map to store the data such as verification code etc.
* Temporary storage of Map to store the data that will be used once.

## 1. Configuration

This chapter is duplicated with [D10106 - Configuration, vertx-tp.yml](d10106-configuration-vertx-tpyml.md), it's
pre-condition to use SharedClient.

### 1.1. vertx.yml

In major configuration up.god.file, you must extend to `vertx-tp.yml` up.god.file to enable this configuration.

```yaml
zero:
  lime: mongo,readible,secure,tp
  vertx:
    instance:
    - name: vx-zero
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

### 1.2. vertx-tp.yml

This up.god.file must contain shared data information, actually there is only one configuration node named `shared`，if
you want to enable this feature you can set as following:

```yaml
shared:
  config:
    async: true
```

### 1.3. vertx-inject.xml

The last configuration for shared data usage is that you must set `inject` in your configuration:

```yaml
shared: io.vertx.mod.plugin.shared.MapInfix
```

Once you have finished above three configuration, the shared data will be enabled.

## 2. Source Code

```java
package up.god.micro;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.annotations.Infusion;
import io.vertx.mod.plugin.shared.SharedClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public class SharedActor {

    @Plugin
    private transient SharedClient<String, String> sharedClient;

    @Path("/shared")
    @GET
    public JsonObject sayShared() {
        System.out.println(this.sharedClient);
        return new JsonObject();
    }
}
```

When you test this url, you should see following information in output console:

```shell
io.vertx.mod.plugin.shared.SharedClientImpl@245fc212
```

It means that you have got reference of `SharedClient`, then we'll move to some apis of SharedClient to see how to use
this client in different situations.

## 3. Usage

### 3.1. Definition of ShareClient

Here shared client contains following definition:

```java
public interface SharedClient<K, V> { ... }
```

Here are two generic types: `K, V`, these two types described key type and value type, that's why we could see following
reference definition:

```java
private transient SharedClient<String,String> ...
```

### 3.2. Get reference

Here are two important api to get reference of `AsyncMap/LocalMap` of vert.x as following:

```java
    /**
     * Get reference of AsyncMap
     */
    AsyncMap<K, V> fetchAsync();

    /**
     * Get reference of LocalMap
     */
    LocalMap<K, V> fetchSync();
```

It means that some vert.x native developers want to use `AsyncMap/LocalMap` directly, in this situation you can call
above two APIs to get reference.

> But you must be careful about the configuration `config -> async`, in zero system you must use correct mode of
> SharedData that reflect to `async` , in other words, async = true, you can use AsyncMap, async = false, you can use
> LocalMap.

### 3.3. Switch Pool

In zero system, except the default shared pool, you also could switch to create new pool by following API:

```java
    SharedClient<K, V> switchClient(final String name);
```

The new created `SharedClient` generic type `K, V` must be the same as original. The concept is as following:

![](/doc/image/D10105-1.png)

You can use SharedClient create any new SharedClient, all the APIs belong to the client must impact each one in the same
Data Pool. If you did not create any new SharedClient, the client must refer the default.

```java
private static final String NAME = "ZERO_MAP_POOL";
```

### 3.4. Common APIs

The last APIs of SharedClient are as following:

```java
    KeyPair<K, V> put(K key, V value);

    KeyPair<K, V> remove(K key);

    V get(K key);

    @Fluent
    SharedClient<K, V> put(K key, V value, Handler<AsyncResult<KeyPair<K, V>>> handler);

    @Fluent
    SharedClient<K, V> remove(K key, Handler<AsyncResult<KeyPair<K, V>>> handler);

    @Fluent
    SharedClient<K, V> get(K key, Handler<AsyncResult<V>> handler);
```

Above six APIs described common operations such as `put, remove, get` by different mode \( `async/sync` \), these APIs
is common used in HashMap and zero provide to developer to do some temp storage.

### 3.5. Once/Expired

Except common data pool, zero support two special map:

```java

    KeyPair<K, V> put(K key, V value, int expiredSecs);
    
    @Fluent
    SharedClient<K, V> put(K key, V value, int expiredSecs, Handler<AsyncResult<KeyPair<K, V>>> handler);
```

Here are additional `int expiredSecs`, it means that the `key = value` will be expired in `expiredSecs` seconds, it
could be used to store some verification code \( by mobile \) or other data that should be expired duration limit
seconds.

```java

    V get(K key, boolean once);
    
    @Fluent
    SharedClient<K, V> get(K key, boolean once, Handler<AsyncResult<V>> handler);
```

Another map is that when you get data from data pool, you can provide the parameter `once`, if it's false, the usage is
the same as common API, if it's true, after you get the data from data pool, the `key = value` will be removed and it's
once consume for developers.

## 4. Summary

This tutorial described the SharedData feature that zero system provided, it could be used in many business situations
such as

* Mobile verification code by sms \( The code must be expired in 30 seconds \);
* Authorization Code to exchange token \( The code must be used once \);

Then you can consider to use the data map in your projects.



