# Utility X, JsonObject Processing

The first batch methods in Utility X is for Json Processing, these methods start with `to` .

* `static <T> JsonObject toJson(T entity)`
* `static <T> JsonObject toJson(T entity, String pojo)`
* `static <T> JsonObject toJsonFun(T entity, Function<JsonObject,JsonObject> convert)`
* `static <T> T fromJson(JsonObject data, Class<T> clazz)`
* `static <T> T fromJson(JsonObject data, Class<T> clazz, String pojo)`
* `static JsonObject fromJson(JsonObjec data, String pojo)`

Above three methods could process the data type conversion of `io.vertx.core.json.JsonObject`. this chapter focus on
these three methods usage. Before you do this testing, we'll create new Data Object for the type `T`.

```java
package io.vertx.up.unity;

import java.io.Serializable;

public class D10047Obj implements Serializable {

    private String name;
    private String email;
    private Integer age;
    private boolean male;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public Integer getAge() {
        return this.age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public boolean isMale() {
        return this.male;
    }

    public void setMale(final boolean male) {
        this.male = male;
    }
}
```

Then create the resource up.god.file `d10047.json`, the up.god.file content should be as following:

```json
{
    "name": "Lang",
    "email": "lang.yu@hpe.com",
    "age": 32,
    "male": true
}
```

## 1. Direct To

This chapter focus on `toJson(T)` method for conversion between Data Object and Json Object. The testing code should be
as following:

```java
package io.vertx.up.unity;

import io.vertx.core.json.JsonObject;
import io.vertx.quiz.TestBase;
import io.vertx.up.util.Jackson;
import org.junit.Assert;
import org.junit.Test;

public class D10047Tc extends TestBase {

    @Test
    public void testToJson() {
        final JsonObject data = this.getJson("d10047.json");
        final D10047Obj obj = Jackson.deserialize(data, D10047Obj.class);
        // Convert
        final JsonObject result = Ux.toJson(obj);
        System.out.println(result.encodePrettily());
        Assert.assertEquals(4, result.fieldNames().size());
    }
}
```

Here we could see that `Ux.toJson` method could convert a data object to JsonObject, it's useful when you want to do
this conversion, because `JsonObject` is native data type that vert.x provided, zero system provide a way to do this
conversion when you want to use your own data object. But we still recommend you use `JsonObject` instead of Data
Object.

## 2. Mapping To

This chapter focus on `toJson(T,String)` method for conversion between Data Object and Json Object based on
configuration files. Firstly you should create new configuration up.god.file:

```yaml
type: "io.vertx.up.unity.D10047Obj"
mapping:
  name: username
```

Above up.god.file should be  `src/test/resources/pojo/d10047.yml`, the filename is freedom. The configuration
up.god.file contains two important attributes:

* **type**: This attribute should be full java class name, zero system will verify whether this class could be loaded.
* **mapping**: This attribute should be `key = value` for each line, it describes `from -> to` conversion.

Once you have finished the configuration, you could write following code:

```java
    @Test
    public void testToJsonMapping() {

        final JsonObject data = this.getJson("d10047.json");
        final D10047Obj obj = Jackson.deserialize(data, D10047Obj.class);
        // Convert
        final JsonObject result = Ux.toJson(obj, "d10047");
        System.out.println(result.encodePrettily());
        Assert.assertEquals(4, result.fieldNames().size());
        Assert.assertEquals("Lang", result.getString("username"));
    }
```

Here you could see the console output:

```json
{
  "username" : "Lang",
  "email" : "lang.yu@hpe.com",
  "age" : 32,
  "male" : true
}
```

The field `name` has been converted to `username` instead of original and `name` field has been removed from generated
JsonObject. Please be careful about this operation, it's single direction that you couldn't convert back to Data Object
because some fields have been updated.

## 3. Mapping Dynamic

This chapter focus on the last method `toJsonFun(T,Function)`, it's for conversion between Data Object and Json Object
based on the function that you provided, it's for developer to provide an interface to do this conversion by
user-defined.

```java
    @Test
    public void testToJsonFun() {
        final JsonObject data = this.getJson("d10047.json");
        final D10047Obj obj = Jackson.deserialize(data, D10047Obj.class);
        final JsonObject result = Ux.toJsonFun(obj,
                (from) -> from.put("username", from.getString("email")));
        Assert.assertEquals(result.getString("username"),
                result.getString("email"));
    }
```

Here you could provide a function `Function<JsonObject,JsonObject>`, this function could help you to convert the
original JsonObject to new one, you can define your own rules.

## 4. Direct From

This chapter focus on `fromJson(JsonObject,Class<T>)`  method for conversion between Data Object and Json Object. The
testing code should be as following:

```java
    @Test
    public void testFromJson() {
        final JsonObject data = this.getJson("d10047.json");
        final D10047Obj obj = Ux.fromJson(data, D10047Obj.class);
        System.out.println(obj);
        Assert.assertNotNull(obj);
    }
```

This method is related to `toJson(T)`, the conversion direction is different only, here you should see following output
in console

```shell
D10047Obj{name='Lang', email='lang.yu@hpe.com', age=32, male=true}
```

## 5. Mapping From

This method will involve the same mapping up.god.file:

```yaml
type: "io.vertx.up.unity.D10047Obj"
mapping:
  name: username
```

It's related to `toJson(T,String)`, also will use pojo up.god.file to do this conversion

```java
    @Test
    public void testFromJsonMapping() {
        final JsonObject data = this.getJson("d10047-mapping.json");
        final D10047Obj obj = Ux.fromJson(data, D10047Obj.class, "d10047");
        System.out.println(obj);
        Assert.assertNotNull(obj);
    }
```

The same output will be seen in console as following:

```shell
D10047Obj{name='Lang', email='lang.yu@hpe.com', age=32, male=true}
```

## 6. Summary

These three Apis are provided for following scenarios:

* If you used some ORM framework or some lightweight Mapping framework such as Hibernate, Mybatis, Jooq etc, you'll get
  response data object from data access layer such Dao, Jdbc, but you want to provide normalized response to your
  client, you can do this conversion by these three Apis.
* If you have an old system and you want to migrate to current framework, you can write some Adapter codes here to let
  the Data Object could work in zero system, so you can migrate from original system to Micro Architecture more smartly.
* You can write some connector codes to do data mapping by these Apis.

These three Apis came from real project of Video App that has been hosted by zero system, because we found it's widely
used and just like re-using functions, then we extract these three apis from the projects and put into standard zero
system.

