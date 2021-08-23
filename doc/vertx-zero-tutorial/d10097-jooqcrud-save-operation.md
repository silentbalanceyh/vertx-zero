# D10097 - Jooq/CURI, Save/Existing Operation

In many business scenarios, we often need to merge `Save/Upsert/Existing` operations here, in this kind of situations,
we provide following Apis in Utility X package:

* `<T> Future<T> saveAsync(Object id, T updated)`
* `<T> Future<T> saveAsync(Object id, Function<T, T> copyFun)`
* `<T> Future<T> upsertReturningPrimaryAsync(JsonObject andFilters, T updated, Consumer<Long> consumer)`
* `<T> Future<T> upsertAsync(JsonObject andFilters, final T updated)`
* `<T> Future<Boolean> existsByIdAsync(Object id)`
* `<T> Future<Boolean> existsOneAsync(JsonObject filters)`

Based on the method signature you should know the usage here.

## 1. Source Code

> The source code came from Hotel Micro Projects directly

### 1.1. ShopApi

```java
package com.needee.micro.shop;

import com.needee.up.god.cv.Addr;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.*;

@EndPoint
@Path("/api")
public interface ShopApi {

    @Path("/crud/shop/:id")
    @GET
    @Address(Addr.SHOP_GET)
    Long get(@PathParam("id") Long id);

    @Path("/crud/shop/:id")
    @PUT
    @Address(Addr.SHOP_EDIT)
    Long put(@PathParam("id") Long id, @BodyParam JsonObject data);

    @Path("/crud/shop/:id")
    @DELETE
    @Address(Addr.SHOP_DEL)
    Long delete(@PathParam("id") Long id);

    @Path("/crud/shop")
    @POST
    @Address(Addr.SHOP_ADD)
    Long post(@BodyParam JsonObject data);
}
```

You can focus on the last method named `post` here to check the details:

### 1.2. ShopWorker

```java
package com.needee.micro.shop;

import com.needee.up.god.cv.Addr;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

import javax.inject.Inject;

@Queue
public class ShopWorker {

    @Inject
    private transient ShopStub stub;

    @Address(Addr.SHOP_GET)
    public Future<JsonObject> get(final Envelop envelop) {
        final Long id = Ux.getLong(envelop);
        return this.stub.read(id);
    }

    @Address(Addr.SHOP_ADD)
    public Future<JsonObject> add(final Envelop envelop) {
        final JsonObject data = Ux.getJson(envelop);
        return this.stub.create(data);
    }

    @Address(Addr.SHOP_EDIT)
    public Future<JsonObject> edit(final Envelop envelop) {
        final Long id = Ux.getLong(envelop);
        final JsonObject data = Ux.getJson1(envelop);
        return this.stub.update(id, data);
    }

    @Address(Addr.SHOP_DEL)
    public Future<Boolean> delete(final Envelop envelop) {
        final Long id = Ux.getLong(envelop);
        return this.stub.delete(id);
    }
}
```

### 1.3. ShopStub \( Service Interface \)

```java
package com.needee.micro.shop;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;

public interface ShopStub {

    Future<JsonArray> shops(Long hotelId);

    Future<JsonObject> search(Inquiry inquiry);

    Future<JsonObject> read(Long id);

    Future<JsonObject> update(Long id, JsonObject data);

    Future<JsonObject> create(JsonObject data);

    Future<Boolean> delete(Long id);

    Future<Boolean> existing(JsonObject filters);
}
```

### 1.4. Shop Service \( Service Implementation \)

```java
package com.needee.micro.shop;

import com.needee.domain.tables.daos.HtlShopDao;
import com.needee.domain.tables.pojos.HtlShop;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.atom.query.Inquiry;

public class ShopService implements ShopStub {

    @Override
    public Future<JsonArray> shops(final Long hotelId) {
        return Ux.Jooq.on(HtlShopDao.class)
                .fetchAsync("R_HOTEL_ID", hotelId)
                .compose(list -> Ux.thenJsonMore(list, "shop"));
    }

    @Override
    public Future<JsonObject> search(final Inquiry inquiry) {
        return Ux.Jooq.on(HtlShopDao.class)
                .searchAndAsync(inquiry, "shop");
    }

    @Override
    public Future<JsonObject> read(final Long id) {
        return Ux.Jooq.on(HtlShopDao.class)
                .<HtlShop>findByIdAsync(id)
                .compose(item -> Ux.thenJsonOne(item, "shop"));
    }

    @Override
    public Future<JsonObject> update(final Long id, final JsonObject data) {
        final HtlShop entity = Ux.fromJson(data, HtlShop.class, "shop");
        return Ux.Jooq.on(HtlShopDao.class)
                .saveAsync(id, entity)
                .compose(item -> Ux.thenJsonOne(item, "shop"));
    }

    @Override
    public Future<JsonObject> create(final JsonObject data) {
        final HtlShop entity = Ux.fromJson(data, HtlShop.class, "shop");
        return Ux.Jooq.on(HtlShopDao.class)
                .upsertReturningPrimaryAsync(this.uniqueFilter(entity), entity, entity::setPkId)
                .compose(item -> Ux.thenJsonOne(item, "shop"));
    }

    @Override
    public Future<Boolean> delete(final Long id) {
        return Ux.Jooq.on(HtlShopDao.class)
                .deleteByIdAsync(id);
    }

    @Override
    public Future<Boolean> existing(final JsonObject filters) {
        return Ux.Jooq.on(HtlShopDao.class)
                .existsOneAsync(filters);
    }

    private JsonObject uniqueFilter(final HtlShop shop) {
        return new JsonObject().put("Z_SIGMA", shop.getZSigma()).put("S_CODE", shop.getSCode());
    }
}
```

## 2. Summary

Here we listed `existing` and `saving` operations of Api in Utility X apis, you can refer details of all above codes.

