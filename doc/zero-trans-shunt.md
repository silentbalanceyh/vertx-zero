# Trans/Shunt Function

* **trans** means there exist exception converter from Throwable to ZeroException
* **shunt** means there may throw out exception ( ZeroException / ZeroRunException ) only

## 1. Definition

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

## 2. Example

### 2.1. transRun

```java
        return Fn.transRun(() -> new JsonArray(getString(filename)),
                JsonFormatException.class, filename);
```

**Code Flow**

1. The code body executed first.
2. Once the executing met the issue of `java.lang.Throwable`, it will be caught.
3. Then the system will convert `java.lang.Throwable` to `JsonFormatException` exception instead of Jvm exception.
4. `JsonFormatException` must has a constructor that last argument is `Throwable` such as following:

   ```java
   public JsonFormatException(final String filename, final Throwable ex) {
       super(MessageFormat.format(Info.JSON_MSG, filename, ex.getCause()));
   }
   ```

### 2.2. shuntRun

```java
        Fn.shuntRun(() -> {
            // Run vertx application.
            new VertxApplication(clazz).run(args);
        }, LOGGER);
```        

**Code Flow**

1. The function body executed first.
2. Once the body met exception, the system will record with `LOGGER` and catch.
3. But when the body met `io.horizon.exception.AbstractException`, this kind of exception will throw out.

### 2.3. shuntZero

The code flow is the same as `shuntRun`, but exception type is `io.horizon.exception.ProgramException` ( Checked ).

