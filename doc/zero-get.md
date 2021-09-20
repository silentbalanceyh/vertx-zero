# Get Function

* **get** is the function to execute and return to generic T object reference.

## 1. Definition

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

## 2. Example

### 2.1. getJvm

```java
        return Fn.getJvm(() -> {
            final BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(in, Values.ENCODING)
                    );
            // Character stream
            String line;
            while (null != (line = reader.readLine())) {
                buffer.append(line);
            }
            reader.close();
            return buffer.toString();
        }, in);
```

**Code Flow**

1. The system checked the `in` variable first ( If null );
2. If it's not null, the function body will execute;
3. The function body will throw `java.lang.Exception`, once it occurs it will be caught;

### 2.2. get

This function `get` code flow is the same as `getJvm`, but there is no exception in function body. Be careful the last
argument could support more than one object reference checking.

```java
	Fn.getJvm(() -> {
		// ... Do something
	}, arg1, arg2, arg3);
```

### 2.3. getSemi

```java
    return Fn.getSemi(null == config, LOGGER,
            ClusterOptions::new,
            () -> new ClusterOptions(config));
```

This code flow is a little same as `safeSemi`, but there should be value returned.

**Code Flow**

1. If config is null, the first function will execute;
2. Otherwise, the second function will execute;
3. Once met the exception, LOGGER will record the exception details.

## 3. Summary

Each `getX` function has a version to provide default value as following:

```java
    public static <T> T get(
            final T defaultValue,
            final Supplier<T> supplier,
            final Object... input
    )
```

In this situation, it means that once each element of input are all null, return `defaultValue` instead of return null.


