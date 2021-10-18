# Pool/Packet/Zipper

## 1. Definition

```java
    public static <K, V> V pool(
            final ConcurrentMap<K, V> pool,
            final K key,
            final Supplier<V> poolFn)
    public static <V> V poolThread(
            final ConcurrentMap<String, V> pool,
            final Supplier<V> poolFn)
    public static <K, V, E> ConcurrentMap<K, List<V>> packet(
            final Collection<E> object,
            final Function<E, K> keyFn,
            final Function<E, V> valueFn
    )
    public static <K, V, E> ConcurrentMap<K, V> zipper(
            final E[] object,
            final Function<E, K> keyFn,
            final Function<E, V> valueFn)
    public static <K, V, E> ConcurrentMap<K, V> zipper(
            final Collection<E> object,
            final Function<E, K> keyFn,
            final Function<E, V> valueFn
    )
```

## 2. Example

### 2.1. pool/poolThread

```java
    private static final ConcurrentMap<String, JsonObject> RULE_MAP =
            new ConcurrentHashMap<>();
    // ......
    return Fn.pool(RULE_MAP, filename, () -> IO.getYaml(filename));
```

**Code Flow**

1. Input `filename` as key of `RULE_MAP`;
2. Check whether this key exsiting in RULE_MAP;
3. If existing, return the `RULE_MAP.get(key)` directly;
4. Otherwise call the function body to create new insteance and put into `RULE_MAP`;
5. Finally return valid object;

*: poolThread only consider `Thread.currentThread().getName()` as key instead of user-defined.

### 2.2. packet

```java
    final Set<Class<?>> agents =
            classes.stream()
                    .filter((item) -> item.isAnnotationPresent(Agent.class))
                    .collect(Collectors.toSet());
    return Fn.packet(agents,
            ZeroHelper::getAgentKey,
            (item) -> item); 
            // Return type: ConcurrentMap<ServerType, List<Class<?>>>
```

**Code Flow**

1. The collection will be iterated and extract ( key, value ) to group the map structure;
2. `ZeroHelper::getAgentKey` result should be ServerType;
3. `item -> item` result should be `List<Class<?>>` item type `Class<?>`, the `List` container is appent to each group
   directly.

### 2.3. zipper

```java
        final Annotation[] annotationes = clazz.getDeclaredAnnotations();
        // Zapper
        return Fn.zipper(annotationes,
                (item) -> item.annotationType().getName(),
                (item) -> item);
        // Return Type: ConcurrentMap<String, Annotation>
```

**Code Flow**

The collection will be iterated and then extract the `key = value` based on the collection, convert the collection to
map directly.