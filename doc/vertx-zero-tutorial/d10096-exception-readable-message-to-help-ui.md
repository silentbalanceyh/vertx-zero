# D10096 - Exception, Readable message to help UI

Once you defined your error in zero system, this is a situation that you need to provide readable message to client UI.

## 1. Source Code

### 1.1. Configuration

At first you should create following two files:

```shell
src/resources/vertx-error.yml
src/resources/vertx-readible.yml
```

The content of two files are as following:

**vertx-error.yml**

```yaml
E90001: "[404] The user {0} does not existing in database"
E90002: "[401] The user''s ( username = {0} ) password that you provided is wrong"
```

**vertx-readible.yml**

```yaml
90001: "登陆失败，您提供的用户名（或ID）找不到！"
90002: "登陆失败，您提供的用户信息和密码不匹配！"
```

### 1.2. Exception Definition

```java
package com.htl.exception;

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

## 2. Summary

Once you have finished above configuration and exception define, you should see the response information contains `info` node to say: `登陆失败，您提供的用户名（或ID）找不到！`, it could help UI client to extract normalized information and provide to UI to show to customers.



