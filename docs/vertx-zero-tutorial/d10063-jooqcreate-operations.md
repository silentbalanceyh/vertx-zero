# Jooq/CRUD, Write Operations

Then we move to `CRUD` - Create/Update/Delete Operation, these operations is common when you process business.

Demo projects:

* **Standalone - 6093** : `up-thea`

## 1. Source Code

### 1.1. Api

```java
package up.god.micro.tabular;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Codex;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.*;

@EndPoint
@Path("/api")
public interface TabularApi {

    @Path("tabular")
    @POST
    @Address("ZERO://QUEUE/TABULAR/CREATE")
    JsonObject create(@BodyParam @Codex JsonObject data);

    @Path("tabular/{id}")
    @PUT
    @Address("ZERO://QUEUE/TABULAR/UPDATE")
    JsonObject update(@PathParam("id") Long id, @BodyParam @Codex JsonObject data);


    @Path("tabular/{id}")
    @DELETE
    @Address("ZERO://QUEUE/TABULAR/DELETE")
    JsonObject delete(@PathParam("id") Long id);
}
```

Above three apis are mapping to `insert, update, delete` operations.

### 1.2. Consumer

Here we add methods to original consumer code as following:

```java
package up.god.micro.tabular;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

import jakarta.inject.Inject;

@Queue
public class TabularWorker {

    @Inject
    private transient TabularStub stub;

    @Address("ZERO://QUEUE/TABULAR/ID")
    Future<JsonObject> get(final Envelop envelop) {
        final Long id = Ux.getLong(envelop);
        // First version
        return this.stub.fetchOne(id);
    }

    // New added three methods for writing operations.
    @Address("ZERO://QUEUE/TABULAR/CREATE")
    Future<JsonObject> create(final Envelop envelop) {
        final JsonObject data = Ux.getJson(envelop);
        return this.stub.create(data);
    }

    @Address("ZERO://QUEUE/TABULAR/UPDATE")
    Future<JsonObject> update(final Envelop envelop) {
        final Long id = Ux.getLong(envelop);
        final JsonObject data = Ux.getJson1(envelop);
        return this.stub.update(id, data);
    }

    @Address("ZERO://QUEUE/TABULAR/DELETE")
    Future<JsonObject> delete(final Envelop envelop) {
        final Long id = Ux.getLong(envelop);
        return this.stub.delete(id);
    }
}
```

### 1.3. Stub \( Service Interface \)

The definition of service interface as following:

```java
package up.god.micro.tabular;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface TabularStub {

    Future<JsonObject> fetchOne(Long id);

    // New added three apis for writing operations.
    Future<JsonObject> create(JsonObject data);

    Future<JsonObject> update(Long id, JsonObject data);

    Future<JsonObject> delete(Long id);
}
```

## 2. Service Implementation

Here we need to mention is that we'll split different code logical for `insert, update, delete` to explain the usage
of `Ux.Jooq` class.

### 2.1. create

```java
    @Override
    public Future<JsonObject> create(final JsonObject data) {
        final SysTabular tabular = Ux.fromJson(data, SysTabular.class, "tabular");
        tabular.setZCreateTime(LocalDateTime.now());
        tabular.setZUpdateTime(LocalDateTime.now());
        return Ux.Jooq.on(SysTabularDao.class)
                .insertReturningPrimaryAsync(tabular, tabular::setPkId)
                .compose(item -> Ux.thenJsonOne(item, "tabular"));
    }
```

Here the first part is the same that we called `Ux.Jooq.on` api to mount our client to `SysTabularDao` dao class, then
we called `insertReturningPrimaryAsync` method for object tabular, this method will help you to set auto generated
primary key, here we need to mention:

* All the generated key is `Long` type instead of `Integer`.
* The generated key will be passed into `tabular::setPkId` method to consume instead you use it.

There is another method `insertAsync` that you could use is that you do not care the returned generated key, it just
like following:

```java
        return Ux.Jooq.on(SysTabularDao.class).insertAsync(tabular)
                .compose(item -> Ux.thenJsonOne(item, "tabular"));
```

In zero system here provide three apis for Jooq creating:

* `insertAsync(T)`
* `insertAsync(List<T>)`
* `insertReturningPrimaryAsync(T, Consumer<Long>)`

### 2.2. update

The second method that we'll introduce is update operation

```java
    @Override
    public Future<JsonObject> update(final Long id, final JsonObject data) {
        return Ux.Jooq.on(SysTabularDao.class).findByIdAsync(id)
                .compose(item -> Ux.thenJsonOne(item, "tabular"))
                .compose(item -> Future.succeededFuture(item.mergeIn(data)))
                .compose(item -> Future.succeededFuture(Ux.fromJson(item, SysTabular.class, "tabular")))
                .compose(item -> Ux.Jooq.on(SysTabularDao.class).updateAsync(item))
                .compose(item -> Ux.thenJsonOne(item, "tabular"));
    }
```

Above code described how to update the data in database, please be careful about the code logical:

1. Get the entity `SysTabular` from database by id first;
2. Convert the data object into JsonObject;
3. Merged JsonObject with updated json data;
4. Convert the updated JsonObject data back to `SysTabular`;
5. Update the `SysTabular` data object and returned the updated data object.

It's a little complex and not very well, in this situation we recommend to use another method of `Ux.Jooq` as following:

```java
    @Override
    public Future<JsonObject> update(final Long id, final JsonObject data) {
        final SysTabular updated = Ux.fromJson(data, SysTabular.class, "tabular");
        return Ux.Jooq.on(SysTabularDao.class).saveAsync(id, updated)
                .compose(item -> Ux.thenJsonOne(item, "tabular"));
    }
```

Here we called `saveAsync` with `id` and `updated` data object, then you can update the data here. When you send
request, you can see following results:

**URL** : [http://localhost:6093/api/tabular/172](http://localhost:6093/api/tabular/172)

**Method** : PUT

**Request** :

```json
{
    "key":172,
    "name":"Lang3",
    "active": true,
    "sigma": "test",
    "language": "cn",
    "code":"LANG.CODEEX",
    "type":"testx.type",
    "order":3
}
```

**Response** :

```json
{
    "data": {
        "key": 172,
        "active": true,
        "createTime": "2018-02-10T13:50:44",
        "zupdateTime": "2018-02-10T13:50:44",
        "type": "testx.type",
        "code": "LANG.CODEX",
        "name": "Lang3",
        "order": 3,
        "sigma": "test",
        "language": "cn"
    }
}
```

Although we provide update apis here, but the `save` apis are more useful, here are all the apis of `Ux.Jooq`:

* `<T> Future<T> updateAsync(T entity)`
* `<T> Future<List<T>> updateAsync(List<T> entities)`
* `<T> Future<T> saveAsync(Object id, T updated)`
* `<T> Future<T> saveAsync(Object id, Function<T,T> copyFun)`

### 2.3. delete

The last write operation api of Jooq is `delete`, you can do as following:

```java
    @Override
    public Future<JsonObject> delete(final Long id) {
        return Ux.Jooq.on(SysTabularDao.class).deleteByIdAsync(id)
                .compose(result -> (result) ?
                        Future.succeededFuture(new JsonObject().put("result", Boolean.TRUE)) :
                        Future.succeededFuture(new JsonObject().put("result", Boolean.FALSE))
                );
    }
```

Here you can delete record from database.

**URL** : http://localhost:6093/api/tabular/172

**Method** : DELETE

**Response** :

```json
{
    "data": {
        "result": true
    }
}
```

Here we listed all the delete api that `Ux.Jooq` provided:

* `<T> Future<T> deleteAsync(T entity)`
* `<T> Future<Boolean> deleteByIdAsync(Object id)`
* `<T> Future<Boolean> deleteByIdAsync(Collection<Object> ids)`
* `<T> Future<Boolean> deleteByIdAsync(Object... ids)`

## 3. Summary

Until now you have known how to `insert, update, delete` data by Jooq, here we have finished all the CRUD operation
tutorials, from next chapter we'll discuss some advanced Jooq features for real business project usage such as
pagination, sorting, searching etc.



