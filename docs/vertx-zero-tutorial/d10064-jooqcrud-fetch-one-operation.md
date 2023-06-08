# Jooq/CRUD, Fetch One Operation

This chapter we'll focus on some frequently situations: **query unique data record by one field**.

Demo projects:

* **Standalone - 6093** : `up-thea`

For example sometimes you may query the user record by name instead of primary key, in this kind of situation
the `Read Operations` that we introduced may be useless.

## 1. Source Code

### 1.1. Api

```java
package up.god.micro.fetch;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface FetchApi {

    @Path("tabular/by/{name}")
    @GET
    @Address("ZERO://QUEUE/BY/NAME")
    String fetchByName(@PathParam("name") String name);
}
```

### 1.2. Consumer

```java
package up.god.micro.fetch;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

import jakarta.inject.Inject;

@Queue
public class FetchWorker {

    @Inject
    private transient FetchStub stub;

    @Address("ZERO://QUEUE/BY/NAME")
    public Future<JsonObject> byName(final Envelop envelop) {
        final String name = Ux.getString(envelop);
        return this.stub.fetchByName(name);
    }
}
```

### 1.3. Stub \( Interface \)

```java
package up.god.micro.fetch;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface FetchStub {

    Future<JsonObject> fetchByName(String name);
}
```

### 1.4. Service \( Implementation \)

```java
package up.god.micro.fetch;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import up.god.domain.tables.daos.SysTabularDao;
import up.god.domain.tables.pojos.SysTabular;

public class FetchService implements FetchStub {
    @Override
    public Future<JsonObject> fetchByName(final String name) {
        return Ux.Jooq.on(SysTabularDao.class)
                .<SysTabular>fetchOneAsync("S_NAME", name)
                .compose(item -> Ux.thenJsonOne(item, "tabular"));
    }
}
```

## 2. Testing

Here we used `fetchOneAsync` method to query the data by `S_NAME` column, and you can test with following:

**
URL** : [http://localhost:6093/api/tabular/by/%e6%8c%82%e7%89%8c%e4%bb%b7](http://localhost:6093/api/tabular/by/%e6%8c%82%e7%89%8c%e4%bb%b7)

**Method** : GET

**Response** :

```java
{
    "data": {
        "key": 1,
        "active": true,
        "createTime": "2018-02-07T12:09:32",
        "name": "挂牌价",
        "code": "Standard",
        "type": "code.pricecat",
        "order": 1,
        "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
        "language": "cn"
    }
}
```

Except above fetchOneAsync api, `Ux.Jooq` class provide all the `fetchOne` apis as following:

* `<T> Future<T> fetchOneAsync(String column, Object value)`
* `<T> Future<T> fetchOneAndAsync(JsonObject andFilters)`

## 3. Summary

This chapter described how to use `fetchOne` method as well and we'll continue to introduce fetching operations and tell
the developers how to use `andFilters` in forward tutorials.

