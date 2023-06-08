# Jooq/GRUD, Read Operation

The first example of jooq should be `CRUD` - Read Operation, we could process read data by primary key \( id \) first,
the demo of current tutorial we'll use interface style to prepare.

Demo projects:

* **Standalone - 6093** : `up-thea`

## 1. Source Code

### 1.1. Api

```java
package up.god.micro.tabular;

import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@EndPoint
@Path("/api")
public interface TabularIrApi {

    @Path("tabular/{id}")
    @GET
    @Address("ZERO://QUEUE/TABULAR/ID")
    Long get(@PathParam("id") Long id);
}
```

### 1.2. Consumer

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
        return this.stub.fetchOne(id);
    }
}
```

### 1.3. Stub \( Interface \)

```java
package up.god.micro.tabular;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface TabularStub {

    Future<JsonObject> fetchOne(Long id);
}
```

### 1.4. Service \( Implementation Class \)

```java
package up.god.micro.tabular;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import up.god.domain.tables.daos.SysTabularDao;

public class TabularService implements TabularStub {

    @Override
    public Future<JsonObject> fetchOne(final Long id) {
        return Ux.Jooq.on(SysTabularDao.class).fetchOneByID(id);
    }
}
```

## 2. Utility X of Jooq

In this example we used an api of `Ux.Jooq` class, this class is provided by zero system, once you have finished the
configuration of Jooq, you can use `Ux.Jooq` tool in any place of zero system code. When you used `Ux.Jooq` class, you
must call `on(Class<?>)` first and returned `io.vertx.up.uca.jooq.UxJooq` reference, once you get this reference you can
call some common api now.

Current example, we used `fetchOnneByID` method, you can check this method signature:

```java
public <T> Future<T> findByIdAsync(final Object id)
```

_Be sure you have generated the required _`Dao`_ class and then you can pass the _`Dao`_ class into _`Ux.Jooq.on`_ api
to initialize the database accessor first._

When you test the request with following, you can see the response data \( Please ignore the data content \):

**URL** : [http://localhost:6093/api/tabular/2](http://localhost:6093/api/tabular/2)

**Method** : GET

**Response** :

```java
{
    "data": {
        "pkId": 2,
        "isActive": true,
        "zsigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
        "zlanguage": "cn",
        "zcreateTime": "2018-02-07T12:09:32",
        "scode": "Single",
        "stype": "code.pricecat",
        "iorder": 2,
        "sname": "散客执行价"
    }
}
```

Here we could see that the data object has been returned, but for some real business scenario, we need to normalize
response data and here you can refer chapter 3 to do it.

## 3. Normalized

Firstly, create new up.god.file named `tabular.yml` under pojo folder `src/main/resources/pojo`, the up.god.file content
could be as following:

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
```

If you configured the mapping of pojo as above, you can modify the service code as following:

```java
package up.god.micro.tabular;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.unity.Ux;
import up.god.domain.tables.daos.SysTabularDao;
import up.god.domain.tables.pojos.SysTabular;

public class TabularService implements TabularStub {

    @Override
    public Future<JsonObject> fetchOne(final Long id) {
        return Ux.Jooq.on(SysTabularDao.class)
                .<SysTabular>findByIdAsync(id)
                .compose(item -> Ux.thenJsonOne(item, "tabular"));
    }
}
```

Here `tabular` is the configuration up.god.file name that you created, then if you re-send the request you should get
following response:

```json
{
    "data": {
        "key": 2,
        "active": true,
        "name": "散客执行价",
        "code": "Single",
        "type": "code.pricecat",
        "order": 2,
        "sigma": "ENhwBAJPZuSgIAE5EDakR6yrIQbOoOPq",
        "language": "cn",
        "createTime": "2018-02-07T12:09:32"
    }
}
```

## 4. Summary

Because our system came from old hotel system migration, that's why we need the mapping up.god.file to normalize
response. If you focus on new system you can do this normalize in the pojo definition. But you still may met the
situation that need you to set the mapping rule, at that time it's helpful for you to continue the works.

