# Exception, Defined your Errors

Zero system provide simple error define for user.

## 1. Configuration

In your class path, create a new up.god.file named `vertx-error.yml`，the format is as following:

```yaml
E100001: User defined Error {0}, details = {1}
```

The detail implementation used MessageFormat, do not support named parameters.

## 2. Create Class

Create new class that you wanted as following

```java
import io.horizon.exception.WebException;

public class TestRequestException extends
        WebException {
    public TestRequestException(final Class<?> clazz,
                                final String name,
                                final String detail) {
        super(clazz, name, detail);
    }

    @Override
    public int getCode() {
        return -100001;
    }
}
```

* Please ignore the first parameter `Class<?>`, it's for Zero to detect the error happened class position;
* The rest parameters could be pass any length, you could call `super(clazz, arg0, arg1, ...)`
* Be sure to write the `getCode()` method, this method must be match with the `vertx-error.yml` prefix: `E<code>` as
  message key
* If you want to change HttpStatusCode, you could overwrite `getStatus()` as following:

  ```java
        public HttpStatusCode getStatus() {
            // Default exception for 400
                return HttpStatusCode.BAD_REQUEST;
        }
  ```

## 3. Build Envelop with Error

Then in your code, write following code:

```java
    @Address("ZERO://USER")
    public Envelop reply(final Envelop message) {
        final User user = message.data(User.class);
        final WebException error = new TestRequestException(getClass(),
                "Lang", "Detail");
        return Envelop.failure(error);
    }
```

## 4. Running the App

You'll see following error response:

```json
{
    "code": -100001,
    "message": "[ERR-100001] (UserWorker) ZeroException occurs: User defined Error Lang, details = Detail."
}
```



