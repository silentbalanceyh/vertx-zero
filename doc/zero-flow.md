# nullFlow Function

## 1. Definition

```java
    public static <T, F> T nullFlow(final F reference,
                                    final Function<F, T> tranFn,
                                    final Supplier<T> supplier)
```

## 2. Example

### 2.1. nullFlow

```java
    final LocalDateTime datetime = toFull(literal);
    return Fn.nullFlow(datetime,
            (ref) -> Date.from(ref.atZone(ZoneId.systemDefault()).toInstant()),
            () -> {
                // Date parsing
                final LocalDate date = toDate(literal);
                return Fn.nullFlow(date,
                        (ref) -> Date.from(ref.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()),
                        () -> {
                                    // Time parsing
                                    final LocalTime time = toTime(literal);
                                    return null == time ? null : parse(time);
                                });
                    });
```

**Code Flow**

1. If the first argument `datetime` is not null, execute first function `tranFn`;
2. If the first argument `datetime` is null, execute the next function `supplier` direct.
3. This function could build function chain.