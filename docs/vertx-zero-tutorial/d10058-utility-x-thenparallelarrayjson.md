# Utility X, thenParallelArray/Json

We have known that Utility X provide the api `thenParallel` for parallel usage, this chapter we'll introduce some fixed
data type such as JsonArray and JsonObject, both data types are frequently used in vert.x

The api definition is as following:

```java
static Future<JsonArray> thenParallelArray(
    final Future<JsonArray> source, 
    final Function<JsonObject, Future<JsonObject>> generateFun, 
    final BinaryOperator<JsonObject> operatorFun)

static Future<JsonObject> thenParallelArray(
    final Future<JsonArray>... sources)

static Future<JsonObject> thenParallelJson(
    final Future<JsonObject> source, 
    final Function<JsonObject, List<Future>> generateFun, 
    final BiConsumer<JsonObject, JsonObject>... operatorFun)

static Future<JsonObject> thenParallelJson(
    final JsonObject source, 
    final Function<JsonObject, List<Future>> generateFun, 
    final BiConsumer<JsonObject, JsonObject>... operatorFun)
```

There are four new apis for parallel Future building, now we'll explain the usage with real codes

## 1. Source Code

> The codes came from real project of Micro Hotel Platform

### 1.1. thenParallelArray

This function is the same as `thenParallel` that we met in previous tutorial, but it's not generic java type, but
JsonObject and JsonArray instead:

```java
    public Future<JsonArray> payterms(final String sigma) {
        return Ux.thenParallelArray(
                this.findPayterms(sigma),
                this.findCats(sigma)
        ).compose(item -> {
            // Two table data.
            final JsonArray payterms = item.getJsonArray("0");
            final JsonArray categories = item.getJsonArray("1");
            // Zip two json array
            final JsonArray result = Uarr.create(payterms)
                    .zip(categories, "category", "key")
                    .to();
            return Future.succeededFuture(result);
        });
    }
```

Here you should know one thing is that the result of `Future<JsonObject>` should contains different `JsonArray`, you can
refer each JsonArray by index such as the code `item.getJsonArray("0")`.

### 1.2. thenParallelJson

The secondary function is `thenParallelJson`, you can refer following code segments to check the usage:

```java
    public Future<JsonObject> findById(final Long id) {
        return Ux.Jooq.on(HtlHotelDao.class)
                .<HtlHotel>fetchOneAsync("PK_ID", id)
                .compose(hotel -> Ux.thenJsonOne(hotel, "hotel"))
                .compose(item -> Ux.thenParallelJson(item,
                        (hotel) -> Arrays.asList(this.findLocation(hotel)),
                        // Calculation Function
                        (target, source) -> target.put("locationId", source)
                ));
    }
```

Here we get the object of `Hotel` as item and then use the item to find the related records `Location`, finally we put
the Location data into `hotel` and set to `locationId`. The whole logical should be as following:

![](/doc/image/D10058-4.png)

## 2. Summary

From above workflow explain, you should know that this function is very helpful when you need more additional database
access here. In our project, we removed foreign key and used Jooq, to avoid complex duplicated codes, zero system
provide the api for you to resolve this kind situation.

