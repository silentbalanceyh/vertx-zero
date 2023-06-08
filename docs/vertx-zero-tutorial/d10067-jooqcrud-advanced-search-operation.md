# Jooq/CRUD, Paging/Sorting

This chapter will focus on Jooq advanced search operation, it could be used in different pagination list table, you can
refer current tutorial to check more details about how to write advanced searching operation.

Demo projects:

* **Standalone - 6093** : `up-thea`

Here are the all the apis that `Ux.Jooq` class provided for paging, sorting

* `Future<JsonObject> searchAndAsync(Inquiry inquiry)`
* `Future<JsonObject> searchAndAsync(Inquiry inquiry, String pojo)`
* `<T> Future<List<T>> searchAndListAsync(Inquiry inquiry)`
* `Future<JsonObject> searchOrAsync(Inquiry inquiry)`
* `Future<JsonObject> searchOrAsync(Inquiry inquiry, String pojo)`
* `<T> Future<List<T>> searchOrOrListAsync(Inquiry inquiry)`

In current version, zero system provided above three apis only, if you want to use dto, you must provide the mapping
up.god.file that has been introduced in previous tutorial:

**src/main/resources/pojo/tabular.yml**

```yaml
type: "up.god.domain.tables.pojos.SysTabular"
mapping:
  pkId: key
  isActive: active
  zsigma: sigma
  zlanguage: language
  zcreateTime: createTime
  scode: code
  stype: type
  iorder: order
  sname: name
columns:
  name: S_NAME
  code: S_CODE
  type: S_TYPE
  order: I_ORDER
  language: Z_LANGUAGE
  createTime: Z_CREATE_TIME
  updateTime: Z_UPDATE_TIME
  active: IS_ACTIVE
  sigma: Z_SIGMA
```

## 1. Source Code

### 1.1. Api

```java
package up.god.micro.advanced;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import jakarta.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/api")
public interface SearchApi {
    @Path("tabular/search")
    @POST
    @Address("ZERO://QUEUE/SEARCH")
    String search(@BodyParam String name);
}
```

Because we'll serialize the body data into Inquiry directly, here we recommend to use **POST** instead of **GET** method
do searching.

### 1.2. Consumer

```java
package up.god.micro.advanced;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

import jakarta.inject.Inject;

@Queue
public class SearchWorker {

    @Inject
    private transient SearchStub searchStub;

    @Address("ZERO://QUEUE/SEARCH")
    public Future<JsonObject> search(final Envelop envelop) {
        final JsonObject data = Ux.getJson(envelop);
        // The second parameter is the yml up.god.file name "tabular.yml"
        return this.searchStub.search(Ux.getInquiry(data, "tabular"));
    }
}
```

### 1.3. Stub \( Interface \)

```java
package up.god.micro.advanced;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Inquiry;

public interface SearchStub {

    Future<JsonObject> search(final Inquiry inquiry);
}
```

### 1.4. Service \( Implementation \)

```java
package up.god.micro.advanced;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import io.vertx.up.atom.query.Inquiry;
import up.god.domain.tables.daos.SysTabularDao;

public class SearchService implements SearchStub {
    @Override
    public Future<JsonObject> search(final Inquiry inquiry) {
        return Ux.Jooq.on(SysTabularDao.class)
                .searchAndAsync(inquiry, "tabular");
    }
}
```

## 2. Points for searching

* Here we used `Ux.getInquiry` api to build valid `Inquiry` object, the type of this object
  is `io.vertx.up.atom.query.Inquiry`.
* If you do not provide pojo mapping up.god.file \( The 2nd argument \), you should put SQL COLUMN instead instead of
  field name in `sorter` and `criteria` request json node.

## 3. Testing

Here we prepared different cases to test this searching api.

**URL** : [http://localhost:6093/api/tabular/search](http://localhost:6093/api/tabular/search)

**Method** : POST

### 3.1. Empty Request

**Request** :

```json
{
}
```

**Response** :

```json
{
    "data":{
        "list":[
            {
                "key": 1,
                "active": true,
                "name": "挂牌价",
                "code": "Standard",
                "type": "code.pricecat",
                "order": 1,
                "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                "language": "cn",
                "createTime": "2018-02-07T12:09:32"
            },
            ......
        ],
        "count":168
    }
}
```

Returned all the records in your database and the count.

### 3.2. Paging

**Request** :

```json
{
    "pager":{
        "page":1,
        "size":10
    }
}
```

**Response** :

```json
{
    "data":{
        "list":[
            {
                "key": 1,
                "active": true,
                "name": "挂牌价",
                "code": "Standard",
                "type": "code.pricecat",
                "order": 1,
                "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                "language": "cn",
                "createTime": "2018-02-07T12:09:32"
            },
            ......,
            {
                "key": 10,
                "active": true,
                "name": "预留房",
                "code": "Left",
                "type": "room.status",
                "jconfig": "{\"icon\":\"green heart\"}",
                "order": 3,
                "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                "language": "cn",
                "createTime": "2018-02-07T12:09:32"
            }
        ],
        "count":168
    }
}
```

### 3.3. Sorting

**Request** :

```json
{
    "pager":{
        "page":1,
        "size":10
    },
    "sorter":[
        "type,DESC",
        "order,DESC"
    ]
}
```

**Response** :

```json
{
    "data":{
        "list":[
            {
                "key": 144,
                "active": true,
                "name": "历史宾客",
                "code": "History",
                "type": "traveler.status",
                "order": 2,
                "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                "language": "cn",
                "createTime": "2018-02-07T12:09:32"
            },
            ......,
            {
                "key": 159,
                "active": true,
                "name": "客房",
                "code": "Room",
                "type": "shift.type",
                "order": 1,
                "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
                "language": "cn",
                "createTime": "2018-02-07T12:09:32"
            }
        ],
        "count":168
    }
}
```

### 3.4. Projection

In this request you can set some filters for returned columns.

**Request** :

```json
{
    "pager":{
        "page":1,
        "size":10
    },
    "sorter":[
        "type,DESC",
        "order,DESC"
    ],
    "projection":[
        "active",
        "sigma",
        "language"
    ]
}
```

**Response** :

```json
{
    "data":{
        "list":[
            {
                "key": 144,
                "name": "历史宾客",
                "code": "History",
                "type": "traveler.status",
                "order": 2,
                "createTime": "2018-02-07T12:09:32"
            },
            ......,
            {
                "key": 159,
                "name": "客房",
                "code": "Room",
                "type": "shift.type",
                "order": 1,
                "createTime": "2018-02-07T12:09:32"
            }
        ],
        "count":168
    }
}
```

### 3.5. Criteria

The last core parameters are `criteria` for condition setting.

**Request** :

```json
{
    "pager":{
        "page":1,
        "size":10
    },
    "sorter":[
        "type,DESC",
        "order,DESC"
    ],
    "projection":[
        "active",
        "sigma",
        "language"
    ],
    "criteria":{
        "order,<":2
    }
}
```

**Response** :

```json
{
    "data":{
        "list":[
            {
                "key": 143,
                "name": "在住宾客",
                "code": "OnGoing",
                "type": "traveler.status",
                "order": 1,
                "createTime": "2018-02-07T12:09:32"
            },
            ......,
            {
                "key": 38,
                "name": "现金",
                "code": "Cash",
                "type": "pay.type",
                "order": 1,
                "createTime": "2018-02-07T12:09:32"
            }
        ],
        "count":38
    }
}
```

## 4. Summary

From this chapter you should know how to do common searching with zero system Jooq.

* `sorter, pager, projection, criteria` are specific parameters if you use body directly;
* `sorter` and `criteria` support SQL column usage directly if you do not use `pojo` argument;
* `projection` could remove some returned fields, if you use `pojo` up.god.file, projection field names should be
  configured in `mapping` node.
* `criteria` support zero filter syntax in previous tutorial introduced and all the connector is `AND` in SQL;
* The returned data format is fixed as you see in examples.



