# Fling Function

* **Fling** is similiar with throw out action, it means that the exception must be throw out in these actions, it could
  simply the code flow to throw out specific exception.

## 1. Definition

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

## 2. Example

### 2.1. flingZero

```java
        final JsonObject data = this.NODE.read();

        Fn.flingZero(null == data || !data.containsKey(Key.SERVER), LOGGER,
                ServerConfigException.class,
                getClass(), null == data ? null : data.encode());
        
        return visit(data.getJsonArray(Key.SERVER));
```

**Code Flow**

1. This function verify the condition `null == data || !data.containsKey(Key.SERVER)`;
2. Once the condition is checked correct, it means that the exception will be throw out;
3. Then zero system will build exception `ServerConfigException` with arguments `Class<?>, String`;
4. Then zero system record the exception information with `LOGGER` first. Refer [Home Page](index.md) to check Logging
   initialization.
5. Then the function throw out the exception and the programming terminaled.

### 2.2. flingUp

The method `flingUp` code flow is the same as `flingZero` except the exception type.

* `flingZero`: Throw out `io.horizon.exception.ProgramException` ( Checked )
* `flingUp`：Throw out `io.horizon.exception.AbstractException` ( Runtime )
