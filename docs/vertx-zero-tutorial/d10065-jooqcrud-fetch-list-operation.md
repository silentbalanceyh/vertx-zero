# Jooq/CRUD, Fetch List Operation

This chapter we'll move to list fetching operations, here are four apis listed in `Ux.Jooq` class that could be used to
fetch list.

* `<T> Future<List<T>> fetchAsync(String column, Object value)`
* `<T> Future<List<T>> fetchInAsync(String column, Object... values)`
* `<T> Future<List<T>> fetchAndAsync(JsonObject andFilters)`
* `<T> Future<List<T>> fetchOrAsync(JsonObject orFilters)`

Above four apis could be used to fetch the list by different conditions and all the conditions are frequently used in
your real projects. This chapter we'll ignore the `filter` explain, focus on basic usage and then we'll go forward to.

Demo projects:

* **Standalone - 6093** : `up-thea`

## 1. Source Code

### 1.1. Api

```java
package up.god.micro.fetch;

import io.vertx.core.json.JsonArray;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.*;

@EndPoint
@Path("/api")
public interface FetchListApi {

    @Path("tabular/list/by/{type}")
    @GET
    @Address("ZERO://QUEUE/LIST/BY")
    String fetchByType(@PathParam("type") String type);

    @Path("tabular/list/by")
    @POST
    @Address("ZERO://QUEUE/LIST/BY/TYPES")
    JsonArray fetchByTypes(@BodyParam JsonArray types);

    @Path("tabular/list/multi")
    @GET
    @Address("ZERO://QUEUE/LIST/BY/MULTI")
    String fetchByMulti(@QueryParam("type") String type,
                        @QueryParam("code") String code);
}
```

### 1.2. Consumer

```java
package up.god.micro.fetch;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

import jakarta.inject.Inject;

@Queue
public class FetchListWorker {

    @Inject
    private transient FetchStub stub;

    @Address("ZERO://QUEUE/LIST/BY")
    public Future<JsonArray> fetchByType(final Envelop envelop) {
        final String type = Ux.getString(envelop);
        return this.stub.fetchByTypes(type);
    }

    @Address("ZERO://QUEUE/LIST/BY/TYPES")
    public Future<JsonArray> fetchByTypes(final Envelop envelop) {
        final JsonArray types = Ux.getArray(envelop);
        return this.stub.fetchByTypes(types.getList().toArray());
    }

    @Address("ZERO://QUEUE/LIST/BY/MULTI")
    public Future<JsonArray> fetchByMulti(final Envelop envelop) {
        final String type = Ux.getString(envelop);
        final String code = Ux.getString1(envelop);
        final JsonObject filters = new JsonObject();
        filters.put("S_TYPE", type).put("S_CODE", code);
        return this.stub.fetchByFilters(filters);
    }
}
```

### 1.3. Stub

Here we added two more methods in to interface definition: `fetchByTypes` and `fetchByFilters` .

```java
package up.god.micro.fetch;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface FetchStub {

    Future<JsonObject> fetchByName(String name);

    Future<JsonArray> fetchByTypes(Object... types);

    Future<JsonArray> fetchByFilters(JsonObject filters);
}
```

### 1.4. Service

```java
package up.god.micro.fetch;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
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

    @Override
    public Future<JsonArray> fetchByTypes(final Object... types) {
        return Ux.Jooq.on(SysTabularDao.class)
                .<SysTabular>fetchInAsync("S_TYPE", types)
                .compose(item -> Ux.thenJsonMore(item, "tabular"));
    }

    @Override
    public Future<JsonArray> fetchByFilters(final JsonObject filters) {
        return Ux.Jooq.on(SysTabularDao.class)
                .<SysTabular>fetchAndAsync(filters)
                .compose(item -> Ux.thenJsonMore(item, "tabular"));
    }
}
```

## 2. Testing

Above example showed how to fetch list by different filters with condition, you can test with following:

**
URL** : [http://localhost:6093/api/tabular/list/by/preorder.category](http://localhost:6093/api/tabular/list/by/preorder.category)

**Method** : GET

**Response** :

```json
{
    "data": [
        {
            "key": 44,
            "active": true,
            "createTime": "2018-02-07T12:09:32",
            "type": "preorder.category",
            "code": "Personal",
            "name": "散客预定",
            "order": 1,
            "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
            "language": "cn"
        },
        {
            "key": 45,
            "active": true,
            "createTime": "2018-02-07T12:09:32",
            "type": "preorder.category",
            "code": "Company",
            "name": "团队预定",
            "order": 2,
            "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
            "language": "cn"
        }
    ]
}
```

**URL** : [http://localhost:6093/api/tabular/list/by](http://localhost:6093/api/tabular/list/by)

**Method** : POST

**Request** :

```json
[
    "room.status",
    "room.op.status"
]
```

**Response** :

```json
{
    "data": [
        {
            "key": 8,
            "active": true,
            "createTime": "2018-02-07T12:09:32",
            "type": "room.status",
            "code": "Operation",
            "name": "可操作",
            "jconfig": "{\"visible\":false}",
            "order": 1,
            "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
            "language": "cn"
        },
        ......
    ]
}
```

**
URL** : [http://localhost:6093/api/tabular/list/multi?code=Single&type=code.pricecat](http://localhost:6093/api/tabular/list/multi?code=Single&type=code.pricecat)

**Method** : GET

**Response** :

```json
{
    "data": [
        {
            "key": 2,
            "active": true,
            "createTime": "2018-02-07T12:09:32",
            "type": "code.pricecat",
            "code": "Single",
            "name": "散客执行价",
            "order": 2,
            "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
            "language": "cn"
        }
    ]
}
```

## 3. Summary

This chapter described that how to fetch list from database by different conditions, based on the examples you could
know how to fetch list from database. Next chapter we'll introduce the filter details, actually `andFilters`
and  `orFilters` are both JsonObject, but there are some specific syntax that zero defined for developers to build
complex filters.

