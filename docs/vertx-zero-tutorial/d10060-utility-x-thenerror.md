# Utility X, thenError

Zero system provide normalized way to build `WebException` Future, you can use the apis in current tutorial to process
the response. The apis definition should be as following:

```java
static <T, R> Future<R> thenError(
    final Future<Boolean> condition, 
    final Supplier<Future<T>> trueFuture, 
    final Function<T, R> trueFun, 
    final Class<? extends WebException> clazz, 
    final Object... args)
static Future<JsonObject> thenError(
    final Future<Boolean> condition, 
    final Supplier<Future<JsonObject>> trueFuture, 
    final Class<? extends WebException> clazz, 
    final Object... args)
static <T> Future<T> thenError(
    final Class<? extends WebException> clazz, 
    final Object... args)
```

## 1. Source Code

> Source Code came from Video App

```java
    public Function<SecUser, Future<JsonObject>> response(
            final String username,
            final String password,
            final Function<SecUser, Future<JsonObject>> future) {
        return pojo -> {
            if (null == pojo) {
                // User does not exist
                this.logger.info("[ Auth ] username/id = {0} does not exist.", username);
                return Ux.thenError(UserNotFoundException.class, this.clazz, username);
            } else if (!password.equals(pojo.getSPassword())) {
                // Password wrong
                this.logger.info("[ Auth ] username/id = {0}, password is wrong {1}.", username, password);
                return Ux.thenError(PasswordWrongException.class, this.clazz, username);
            } else {
                // Continue because of success
                this.logger.info("[ Auth ] User ( username/id = {0} ) login successfully.", username);
                return future.apply(pojo);
            }
        };
    }
```

Above function finished the task as:

* If returned `SecUser` pojo is null, reply to client `UserNotFoundException` Future.
* If the password is not match, reply to client `PasswordWrongException` Future.

## 2. Summary

Except direct to build standard `WebException` Future as example showed, there left two other apis to build conditional
error Future, based on the arguments, you should know following workflow:

![](/doc/image/D10060.png)

