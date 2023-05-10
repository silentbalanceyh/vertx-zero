# Utility X, thenScatterJson

This function is standalone and also very useful when you met some strong business requirements. This api is as
following definition:

```java
static Future<JsonArray> thenScatterJson(
    final Future<JsonArray> source, 
    final Function<JsonObject, Future<JsonArray>> generateFun, 
    final BiFunction<JsonObject, JsonArray, JsonObject> mergeFun)
```

## 1. Source Code

This function is used in Video App, the source code is as following:

```java
    @Override
    public Future<JsonArray> query(
            final String userId,
            final JsonObject filter,
            final FindOptions options,
            final boolean video) {
        final Future<JsonArray> topicFuture = Ux.Mongo.findWithOptions(Topic.TABLE, filter, options,
                // Join another table to visit subscription table
                Subscription.TABLE, "targetId", Extractor.getAdditional(userId, TargetType.TOPIC),
                // Return merged.
                Extractor.setSubscribed(userId));
        return (video) ? Ux.thenScatterJson(topicFuture,
                this::findVideos, (source, result) -> source.put("videoItems", result))
                .compose(item -> Future.succeededFuture(
                        Uarr.create(item)
                                .convert(ID.DB_KEY, ID.UI_KEY)
                                .to()
                )) : topicFuture
                .compose(item -> Future.succeededFuture(
                        Uarr.create(item)
                                .convert(ID.DB_KEY, ID.UI_KEY)
                                .to()));
    }
```

In above code, we used scatter method to do secondary query from mongo db.

## 2. Workflow

The code logical is as following:

## ![](/doc/image/D10059.png)3. Summary

Actually this code flow is the same as thenParallel, but the operated object is fixed type. JO means JsonObject, JA
means JsonArray. There are some histories of this function because it's born earlier than `thenParallel`, this function
is more useful because in vert.x the JsonArray and JsonObject is frequently used. That's why we still keep this function
in Utility X tool. Based on our experience, this function is more frequently used than `thenParallel`, so we recommend
the developer use this function first as parallel in priority.





