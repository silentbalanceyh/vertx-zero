# It Function

* **It** means iteration on collections instead of java iteration

## 1. Definition

```java
    public static <K, V> void itMap(
            final ConcurrentMap<K, V> map,
            final BiConsumer<K, V> fnEach)
    )
    public static <V> void itSet(
            final Set<V> set,
            final BiConsumer<V, Integer> fnEach
    )
    public static <V> void itList(
            final List<V> list,
            final BiConsumer<V, Integer> fnEach
    )
    public static <T> void itJObject(
            final JsonObject data,
            final BiConsumer<T, String> fnEach
    )
    public static <T> void etJObject(
            final JsonObject data,
            final ZeroBiConsumer<T, String> fnIt
    ) throws ZeroException
    public static <T> void itJArray(
            final JsonArray array,
            final Class<T> clazz,
            final BiConsumer<T, Integer> fnEach
    )
    public static <T> void etJArray(
            final JsonArray dataArray,
            final Class<T> clazz,
            final ZeroBiConsumer<T, Integer> fnIt
    ) throws ZeroException
    public static <T> void etJArray(
            final JsonArray dataArray,
            final ZeroBiConsumer<T, String> fnIt
    ) throws ZeroException
```

## 2. Example

### 2.1. One Point

This article will not record some common example for collection iteration, instead, please refer following point:

1. Zero will check the element type to see whether it's null, these functions will ignore all the null element ( null
   key, null value );
2. The `it` prefix will do iteration without any exception throw out;
3. The `et` prefix will do iteration with `ZeroException` out instead of no exception;
4. `List, Set` will append `index` for iteration as the second argument;
5. Specific Iteration for JsonArray provided ( `1st level -> 2nd level` );

### 2.2. Specific Iteration

This function is a little different from other:

```java
    public static <T> void etJArray(
            final JsonArray dataArray,
            final ZeroBiConsumer<T, String> fnIt
    ) throws ZeroException 
```

This function will iterate each element inner JsonObject,

1. The function iterate the `JsonArray` as 1st level;
2. Then the function iterate the `JsonObject` ( Each array element must be JsonObject) as 2nd level;
3. The function body only focus on the `JsonObject` ( key = value );