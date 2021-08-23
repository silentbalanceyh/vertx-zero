# D10050 - Utility X, Errors

In zero system, error definition is very easy, you can define your own `WebException` or other `UpException`, then with
Utility X tool you can normalize web exceptions. For exception definition tutorial please refer forward chapters.
Current chapter describe the two frequently scenarios:

* `static WebException toError(Class<? extends WebException> clazz, final Object...args)`
* `static WebException toError(Class<?> clazz, final Throwable error)`

In zero system all the user-defined exceptions should inherit from `WebException`, the first argument is `Class<?>`, it
means that which class build/throw out this exception, it could help developer to trace the real exception points in
real projects.From second argument, the rested will put into `WebException` constructor for different types.

Another usage of above api is that you can transfer JVM exception to `WebException` and then normalized it and throw out
to client as standard exception response.

## 1. Source Code

> Came from hotel micro service platform

### 1.1. UserNotFoundException

Please ignore the details of this exception definition

```java
package com.needee.exception;

import io.vertx.core.http.HttpStatusCode;
import io.vertx.up.exception.WebException;

public class UserNotFoundException extends WebException {

    public UserNotFoundException(final Class<?> clazz,
                                 final String username) {
        super(clazz, username);
    }

    @Override
    public int getCode() {
        return -90001;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.RETRY_WITH;
    }
}
```

One thing you should know is that:

* This exception inherited from `WebException`
* The first argument of constructor of this exception is `Class<?>`
* The zero system error code is `-90001`
* The returned http code is not `400 BadRequest`, but `449 Retry With` instead.

### 1.2. Build normalized exception

Here are the usage of this exception with Utility X tool

```java
    public Function<SecUser, Future<JsonObject>> response(
            final String username,
            final String password,
            final Function<SecUser, Future<JsonObject>> future) {
        return pojo -> {
            if (null == pojo) {
                // The user does not exist in database.
                this.logger.info("[ Auth ] username/id = {0} does not exist.", username);
            
                // The usage of the exception in current tutorial.
                return Future.failedFuture(Ux.toError(UserNotFoundException.class, this.clazz, username));
            } else if (!password.equals(pojo.getSPassword())) {
                // The password is wrong
                this.logger.info("[ Auth ] username/id = {0}, password is wrong {1}.", username, password);
                return Future.failedFuture(Ux.toError(PasswordWrongException.class, this.clazz, username));
            } else {
                // Success.
                this.logger.info("[ Auth ] User ( username/id = {0} ) login successfully.", username);
                return future.apply(pojo);
            }
        };
    }
```

You can see that here we call `Ux.toError` api to build this exception and fired by `Future.failedFuture`, this action
will be captured by zero system and replied standard error response.

## 2. Summary

Above tutorial showed that zero system has defined completely error process flow in web request, another point is that
be careful that this exception is only build but not throw out. Actually you can throw out the exception instead
of `fire`, but we recommend you to fire the exception because the code won't trigger `try-catch` flow, but common java
logical flow to process this exception.





