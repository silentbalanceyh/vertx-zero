# Utility X, Sorter Processing

Another feature in zero system is that it provide the object `io.horizon.uca.qr.Sorter` to store sorting message in
querying or searching operations. This class contains following useful apis:

* `<T> JsonObject toJson(Function<Boolean,T> function)`
* `Sorter add(String field, Boolean asc)`
* `Sorter clear()`

## 1. Introduction

For example, in some situations you'll write the code logical in SQL such as:

```sql
SELECT S_NAME, S_CODE FROM SEC_USER WHERE I_AGE > 33 ORDER BY S_NAME DESC, S_EMAIL ASC
```

Here the segment from `S_NAME` means the sorting operations. Sorting operations contain two elements that you should
know:

* **mode**: `ASC/DESC`, the sorting mode that for each field.
* **priority**: In the segment the priority of sorting fields should be in sequence, above SQL statement, `S_NAME` has
  high priority.

Zero system utility provide normalized method to process sorter as folllowing:

* `static Sorter toSorter(String field, boolean asc)`
* `static Sorter toSorter(String field, int mode)`

There are some flags that you should know, for boolean parameter `asc`, it's very clear that no comments also make you
understand this api, but for the int parameter `mode`, you should know:

* **ASC mode**: asc = true or mode &gt; 0 \( exclude 0 \)
* **DESC mode**: asc = false or mode &lt;= 0 \( include 0 \)

## 2. Source Code

This chapter codes also came from real projects.

```java
    @Address(Addr.TOPICS_SUBSCRIBE)
    public void subscribe(final Message<Envelop> message) {
        final JsonObject pager = Ux.getBody(message);
        final JsonObject filter = new JsonObject().put("userId", Extractor.getUserId(message));
        this.subscribe.query(TargetType.TOPIC, filter, MongoReadOpts.toFull(
                Ux.toPager(pager), 
                Ux.toSorter("subscribeTime", false) // Here order by subscribeTime DESC mode.
        )).setHandler(Ux.toHandler(message));
    }
```

## 3. Summary

From above demo you'll see `MongoReadOpts` class appeared, it's mongo db only, we'll introduce the `Mongo` usage in zero
system in forward tutorials. Here you should know that following code impact the sorting operations:

```java
Ux.toSorter("subscribeTime", false)
```



