# Utility X Turn-On

From this chapter our tutorial will focus on the tool box that zero system provided, we call it **Utility X**, and in
your code the class name is `io.vertx.up.unity.Ux`. The package name word `aiki` is Hausa language and it means "Work",
this class will help the developers to process some duplicated works those are not related to business workflow. Here
are three core parts in Utility X:

* `io.vertx.up.unity.Ux`: The uniform tool class, it contains static method only. \( There is no object \)
* `io.vertx.up.atom.unity.Uson`: **\( Data Structure \) **The json container to store JsonObject and process it in "
  Stream Mode", please refer `Rxjava` to know the "Stream Mode", it will process the JsonObject with multi Fluent
  methods.
* `io.vertx.up.atom.unity.Uarr`: **\( Data Structure \) **Another json container to store JsonArray and process it in "
  Stream Mode".

From this chapter we'll study Ux first because almost all the business codes will cover `Ux` style codes because it's
more smartly and helpful. Now we'll move project to `vertx-up` and write some example codes in Unit Test Cases instead
of major codes, but for some request flow codes we'll move to `up-thea`, it's under `vertx-zeus` project. You can know
the rules for following tutorial:

1. All the test cases should belong to `vertx-up`;
2. Other testing codes of Utility X usage should belong to `up-thea`include some zero codes, this kind of codes
   require **Postman** tool that you used in previous tutorials.

## 1. Uson

> Hello world and hello utility x, this is code segments for some usage and you can ignore it first, we'll introduce all
> the utility x tool apis to be sure every developer could do less works in zero system.

First create the input source up.god.file `d10046.json` in your testing resource
folder `src/test/resources/test/io.vertx.up.unity`.

```json
{
    "username": "lang.yu",
    "password": "111111",
    "email": "silentbalanceyh@126.com"
}
```

Then create test case `D10046FirstTc`in your junit folders, in zero system all the test cases must contain valid
suffix. **Tc**: means test case, **Te**: means test exception, you can configure your own name for testing cases but we
still recommend to use `Tc/Te` as testing case name suffix.

```java
package io.vertx.up.unity;

import io.vertx.core.json.JsonObject;
import io.vertx.quiz.TestBase;
import org.junit.Assert;
import org.junit.Test;

public class D10046FirstTc extends TestBase {
    @Test
    public void testInput() {
        final JsonObject input = this.getJson("d10046.json");
        // Uson usage
        final JsonObject ret = Uson.create(input)
                .convert("password", "updated").to();
        System.err.println(ret.encodePrettily());
        Assert.assertEquals("111111", ret.getString("updated"));
    }
}
```

This test case is very simple, here are some points that we need to refer:

1. When you `extends` from `TestBase`, you can call the method `getJson/getArray` to read the resource files, this files
   must be put in `src/test/resources/test/<package-name>` folder, these two methods are for `JsonObject/JsonArray`
   reading, please check your data format first.
2. We created a `Uson` object and then called `convert` api, finally we convert Uson back to JsonObject by `to()`
   method.

If you run this test case you should see following output, the field `password` has been converted to `updated`:

```json
{
  "username" : "lang.yu",
  "email" : "silentbalanceyh@126.com",
  "updated" : "111111"
}
```

## 2. Uarr

Create new input up.god.file named `d10046-arr.json`, put into the same folder in chapter 1.

```json
[
    {
        "username": "lang.yu",
        "password": "111111",
        "email": "silentbalanceyh@126.com"
    },
    {
        "username": "lang.yu2",
        "password": "222222",
        "email": "silentbalanceyh@126.com"
    }
]
```

Then add new test method in `D10046FirstTc` class:

```java
    @Test
    public void testInputArr() {
        final JsonArray input = this.getArray("d10046-arr.json");
        // Uson usage
        final JsonArray ret = Uarr.create(input)
                .convert("password", "updated").to();
        System.err.println(ret.encodePrettily());
        Assert.assertEquals("111111", ret.getJsonObject(0).getString("updated"));
        Assert.assertEquals("222222", ret.getJsonObject(1).getString("updated"));
    }
```

Then you should see another message in your console as following:

```json
[ {
  "username" : "lang.yu",
  "email" : "silentbalanceyh@126.com",
  "updated" : "111111"
}, {
  "username" : "lang.yu2",
  "email" : "silentbalanceyh@126.com",
  "updated" : "222222"
} ]
```

## 3. Ux

The last examples should be Ux usage, it's not needed to create new files, we'll re-use `d10046.json` up.god.file:

```java
    @Test
    public void testUx() {
        final JsonObject input = this.getJson("d10046.json");
        final Envelop envelop = Ux.to(input);
        Assert.assertNotNull(envelop.data());
        System.err.println(envelop.data(JsonObject.class));
    }
```

Here you could see the response in console:

```shell
{"username":"lang.yu","password":"111111","mobile":"15922611447","email":"silentbalanceyh@126.com"}
```

## 4. Summary

The last example we'll convert the `JsonObject` to an Envelop object, why ? In zero system, envelop is uniform resource
model and it could be used in many places such as:

* The communication between Agent \( Sender \) and Worker \( Consumer \)
* Rpc communication between Service 1 and Service 2, this point will be introduces in Micro Part Tutorial.
* Third part tools communication
* The error response building and mount the normalized response data to your messages.



