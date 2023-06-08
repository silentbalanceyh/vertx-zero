# Utility X, JsonArray/Unique Extract

This chapter we'll focus on JsonArray processing with Utility X. There are 6 major apis in `Ux` focus on
list/collection/array processing.

* `static <T> JsonArray toArray(List<T> list)`
* `static <T> JsonArray toArray(List<T> list, String pojo)`
* `static <T> JsonArray toArrayFun(List<T> list, Function<JsonObject,JsonObject> convert)`
* `static <T> JsonObject toUnique(List<T> list)`
* `static <T> JsonObject toUnique(List<T> list, String pojo)`
* `static <T> JsonObject toUnique(JsonArray array)`

Here we won't introduce the details for each one, but pick up some core points.

1. For the 1st - 3rd methods, it's the same as JsonObject processing, the difference is that for JsonObject processing
   the method callee is JsonObject, but current tutorial callee is JsonArray instead.
2. For the `unique` extracting methods, it refer to some specific scenarios, we'll introduce in current tutorials.

## 1. JsonArray

Here are the test cases for `Ux.toArray` method:

```java
package io.vertx.up.unity;

import com.fasterxml.jackson.core.type.TypeReference;
import io.vertx.core.json.JsonArray;
import io.vertx.quiz.TestBase;
import io.vertx.up.util.Jackson;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class D10051Tc extends TestBase {

    @Test
    public void testToJarray() {
        final JsonArray data = this.getArray("d10051.json");
        final List<D10051Obj> obj = Jackson.deserialize(data, new TypeReference<List<D10051Obj>>() {
        });
        // Convert
        final JsonArray ret = Ux.toArray(obj, "d10051");
        System.err.println(ret.encode());
        Assert.assertEquals(2, ret.size());
    }
}
```

Also in this test case you could see following output in console:

```json
[{"username":"Lang","workEmail":"lang.yu@hpe.com","age":32,"male":true},\
{"username":"Lang1","workEmail":"lang.yu1@hpe.com","age":36,"male":false}]
```

## 2. ToUnique

This method is very useful when you met following situations:

Sometimes you got the data from database or other services, the return value is collection such
as `List, Set, Collection or JsonArray` etc, but you have known that there is only one element in these collections, to
resolve this kind of situation, we often write following code:

```java
    .... 
    final List<T> list = ...;
    return null != list && !list.isEmpty() ? list.get(0): new ArrayList<>();
```

Then you can feel that above code is messing and to resolve this kind of issue \( We call it issue \), zero system
provide the tools to extract the first element of the collection as the return value and did the pre-condition checking
for you. You can add following code segment to your test case:

```java
    @Test
    public void testToUnique() {
        final JsonArray data = this.getArray("d10051.json");
        final List<D10051Obj> obj = Jackson.deserialize(data, new TypeReference<List<D10051Obj>>() {
        });
        // Convert
        final JsonObject ret = Ux.toUnique(obj, "d10051");
        Assert.assertNotNull(ret);
        System.err.println(ret.encode());
        // Null returned
        final List<D10051Obj> nullfirst = new ArrayList<>();
        final JsonObject set = Ux.toUnique(nullfirst);
        Assert.assertNotNull(set);
        System.err.println(ret.encode());
    }
```

Then you'll found that there is no `NullPointerException` as well. For code defense you could put the code anywhere to
do things.

## 3. Summary

In real projects above three scenarios are very frequently and the small change could help developer be sure focus on
business requirement only. It's the Utility X existing reason here.

* To be sure your code is strong.
* To be sure your code could execute in any exception situations with logs.
* To be sure your code has the testes of defense style.

Zero system only focus on how to improve the code and fixed style, our style is not the best but it could help you save
time.



