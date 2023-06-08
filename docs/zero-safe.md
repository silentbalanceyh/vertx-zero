# Safe Function

* **Safe** is be sure in any situation the code could be execute correctly.

## 1. Definition

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

## 2. Example

### 2.1. safeJvm

```java
            Fn.safeJvm(() -> {
                final Field field = instance.getClass().getDeclaredField(name);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(instance, value);
            }, LOGGER);
            
```

**Code Flow**

1. The function body will throw out exception: `java.lang.NoSuchFieldException` ( Checked Jvm Exception )
2. In `safeJvm`, once the body throw out the exception, the `LOGGER` will record the exception first ( null != LOGGER )
3. If there is no exception occurs, the code executed normally.

### 2.2. safeZero

```java
        Fn.safeZero(() -> {
            // Init for VertxOptions, ClusterOptions Visit Vertx
            if (VX_OPTS.isEmpty() || null == CLUSTER) {
                final NodeVisitor visitor =
                        Ut.singleton(VertxVisitor.class);
                VX_OPTS.putAll(visitor.visit());
                
                // Must after visit
                CLUSTER = visitor.getCluster();
            }
            // Init for HttpServerOptions
            if (SERVER_OPTS.isEmpty()) {
                final ServerVisitor<HttpServerOptions> visitor =
                        Ut.singleton(HttpServerVisitor.class);
                SERVER_OPTS.putAll(visitor.visit());
            }
            // Init for all plugin options.
            ZeroAmbient.init();
        }, LOGGER);
```

The `safeZero` code flow is also the same as `safeJvm`, but the exception type is different, this function body
exception type is `io.horizon.exception.ProgramException` ( Checked )

### 2.3. safeNull

```java
        Fn.safeNull(() -> {
            final Class<?> clazz = Instance.clazz(managerObj.toString());
            Fn.safeNull(() -> {
                // If null, keep default
                final ClusterManager manager = Instance.instance(clazz);
                obj.setManager(manager);
            }, clazz);
        }, managerObj);
```

**Code Flow**

1. Zero system detect the input objects to check whether it's null.
2. If it's not null, the function body will execute, the last argument support more than one, and you can check many
   input source.
3. This function is provided to avoid unexpected `NullPointerException`.

### 2.4. safeSemi

```java
        Fn.safeSemi(clazz.isAnnotationPresent(Path.class), LOGGER,
                () -> {
                    // 3.1. Append Root Path
                    final Path path = ZeroHelper.getPath(clazz);
                    assert null != path : "Path should not be null.";
                    result.addAll(extract(clazz, PathResolver.resolve(path)));
                },
                () -> {
                    // 3.2. Use method Path directly
                    result.addAll(extract(clazz, null));
                });
```

**Code Flow**

1. Zero system check the first argument condition.
2. Once it's true, the first function will execute, otherwize the second function will execute.
3. The LOGGER is reserved for future use ( Now it's only provided to avoid overloading ).