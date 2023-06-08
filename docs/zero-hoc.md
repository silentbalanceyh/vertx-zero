# Function Programming

## 1. Function interface

Zero system extend function interface system based on java8.

* `java.util.function.Consumer<T>`：`void accept(T t)`；
* `java.util.function.BiConsumer<T, U>`：`void accept(T t, U u)`；
* `java.util.function.Predicate<T>`：`boolean test(T t)`;
* `java.util.function.Supplier<T>`：`T get()`；
* `java.util.function.Function<T, R>`：`R apply(T t)`；

Extension by zero for specifical usage.

* `io.horizon.fn.Actuator`：`void execute()`；
* `io.horizon.fn.ExceptionSupplier<T>`：`T get() throws Exception`；
* `io.horizon.fn.ExceptionActuator`：`void execute() throws Exception`；
* `io.horizon.fn.ProgramActuator`：`void execute() throws ZeroException`；
* `io.horizon.fn.ProgramSupplier<T>`：`T get() throws ZeroException`；
* `io.horizon.fn.ProgramBiConsumer<T,R>`: `void accept(T input, R second) throws ZeroException`

## 2. Fn

In Zero system, there defined a supper static class for function abstract to simply the coding, this class
is `io.vertx.up.fn.Fn`, You also could use following function in your coding.

```java
// Zero Logger initialized, connect to vert.x logging system directly but uniform managed by zero.
import io.horizon.log.Annal;

// Then in your class
public final class Statute {

    private static final Annal LOGGER = Annal.get(Statute.class);
    ......
}
```

### 2.1. Fling

* **Fling** is similiar with throw out action, it means that the exception must be throw out in these actions.

```java

/**
 *  1. If the condition is true ( arg 0 ), Zero system will build the target Exception 
 *  with Class<?> ( arg 2 ) and arguments ( arg 3 ).
 *  2. Once exception build successfully, the Annal ( arg 1 ) will record the exception first
 *  3. Then the exception will be throw out.
 **/
public static void flingZero(
            final boolean condition,
            final Annal logger,
            final Class<? extends ZeroException> zeroClass,
            final Object... args
    ) throws ZeroException

public static void flingUp(
            final boolean condition,
            final Annal logger,
            final Class<? extends ZeroRunException> upClass,
            final Object... args
    )

```

### 2.2. Safe Running

* **safe** is be sure in any situation the code could execute correctly.

```java
    /**
 *  Once the actuator executing met JVM exception ( inherit from Throwable ), the logger will record the error message.
 **/
public static void safeJvm(
final JvmActuator actuator,
final Annal logger
    )
public static<T> T safeJvm(
final JvmSupplier<T> supplier,
final Annal logger
    )
/**
 *  Once the actuator executing met JVM exception ( inherit from ZeroException ), the logger will record the error message.
 **/
public static void safeZero(
final ZeroActuator actuator,
final Annal logger
    )
public static<T> T safeZero(
final ZeroSupplier<T> supplier,
final Annal logger
    )
/**
 * Once all the input objects are null, the Actuator function will not execute, be sure no null dot object input into actuator.
 **/
public static void safeNull(
final Actuator actuator,
final Object...input
    )
public static<T> void safeNull(
final Consumer<T> consumer,
final T input
    )
/**
 * If condition is true, execute tSupplier, otherwise execute fSupplier.
 */
public static void safeSemi(
final boolean condition,
final Annal logger,
final Actuator tSupplier,
final Actuator fSupplier
    )
```

### 2.3. Trans/Shunt

* **trans** means there exist exception converter from Throwable to ZeroException
* **shunt** means there may throw out exception ( ZeroException / ZeroRunException ) only

```java
    public static <T> T transRun(
            final Supplier<T> supplier,
            final Class<? extends ZeroRunException> runCls,
            final Object... args
    )
    public static void shuntRun(
            final Actuator actuator,
            final Annal logger
    )
    public static void shuntZero(
            final ZeroActuator actuator,
            final Object... input
    ) throws ZeroException
```

### 2.4. Get

```java
    /**
     * If supplier throw out exception, set return value to defaultValue
     * If all input objects are null, set return value to defaultValue
     **/
    public static <T> T getJvm(
            final T defaultValue,
            final JvmSupplier<T> supplier,
            final Object... input
    )
    /**
     * default value is null
     **/
    public static <T> T getJvm(
            final JvmSupplier<T> supplier,
            final Object... input
    )
    public static <T> T get(
            final Supplier<T> supplier,
            final Object... input
    ) 
    public static <T> T get(
            final T defaultValue,
            final Supplier<T> supplier,
            final Object... input
    )
    public static <T> T getSemi(
            final boolean condition,
            final Annal logger,
            final Supplier<T> tSupplier,
            final Supplier<T> fSupplier
    )
    public static <T> T getSemi(
            final boolean condition,
            final ZeroSupplier<T> tSupplier,
            final ZeroSupplier<T> fSupplier
    ) throws ZeroException
```

### 2.5. Collection

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

### 2.6. Pool/Group/Zipper

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

### 2.7. NullFlow

```java
    public static <T, F> T nullFlow(final F reference,
                                    final Function<F, T> tranFn,
                                    final Supplier<T> supplier)
```

## 3. Summary

Provide all these function could simply the code and write more defence code in your programming, it could avoid some
common exception such as NullPointer, IndexOf etc. Also it could simple some collection iterator and caculation.

Example 1:

```java
    public static ConcurrentMap<String, Annotation> get(final Class<?> clazz) {
        return Fn.get(() -> {
            final Annotation[] annotationes = clazz.getDeclaredAnnotations();
            // Zipper
            return Fn.zipper(annotationes,
                    (item) -> item.annotationType().getName(),
                    (item) -> item);
        }, clazz);
    }
    // This function workflow is as following
    // 1. Check whether clazz is null, if null return directly.
    // 2. Get annotation array from clazz definition
    // 3. Generate map ( key = annotation name, value = Annotation )
```

Example 2:

```java
        final JsonObject config = dyanmic.read();
        
        // If the config does not contain getKey() value, the exception will be throw out. 
        // avoid config.getJsonObject cause ClassCastException, NullPointerException here.
        Fn.flingUp(!config.containsKey(getKey()), getLogger(),
                ConfigKeyMissingException.class, getClass(), getKey());
        
        return config.getJsonObject(this.getKey());
```