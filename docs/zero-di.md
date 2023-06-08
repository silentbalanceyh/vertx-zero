# Dependency Injection ( JSR 330 )

To avoid object relationship complex, zero also support Simple dependency injection based on JSR 330.

## Vert.x Way

All the vert.x specific object could be inject as following way.

```java
import jakarta.inject.infix.Mongo;
import jakarta.inject.infix.MySql;
import jakarta.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
public class InjectApi {

    @MySql
    private transient SQLClient sqlClient;
    
    @Mongo
    private transient MongoClient mongo;
    // ......
}
```

The following object is supported by current zero version

* [x] `javax.inject.infix.@Mongo`: `io.vertx.ext.mongo.MongoClient`
* [x] `javax.inject.infix.@MySql`: `io.vertx.ext.sql.SQLClient`

## Common Way ( JSR 330 )

### 1. Class implementation

**Target Class**

```java
import io.vertx.ext.mongo.MongoClient;
import io.vertx.up.commune.Envelop;

import jakarta.inject.infix.Mongo;

public class InjectDao {

    @Mongo
    private transient MongoClient client;

    public void async(final Envelop envelop) {
        System.out.println(this.client);
    }
}
```

**Inject `InjectDao`**

```java
import jakarta.inject.Inject;

@Queue
public class InjectWorker {

    @Inject
    private transient InjectDao dao;
    // ......
}
```

### 2. Interface with Unique implementation

**Interface Definition**

```java
public interface InjectStub {

}
```

**Implementation Class**

```java
public class InjectInstance implements InjectStub {

}
```

**Inject `InjectStub -> InjectInstance`**

```java
import jakarta.inject.Inject;

@Queue
public class InjectWorker {

    @Inject
    private transient InjectStub stub;
    // ......
}
```

*: One limitation for this situation is that there are only one implementation of interface InjectStub.

### 3. Interface with Multi implementations

**Interface Definition**

```java
public interface InjectA {
}
```

**Implementation Class**

InjectB

```java
import import jakarta.inject.Named;

@Named("NameInjectA")
public class InjectB implements InjectA {
}
```

InjectC

```java
@Named
public class InjectC implements InjectA {
}
```

**Inject `InjectA -> InjectB`**

```java
import io.vertx.up.annotations.Qualifier;
import jakarta.inject.Inject;

@Queue
public class InjectWorker {

    @Inject
    @Qualifier("NameInjectA")
    private transient InjectA injectA;
```

You can use `@Qualifier` to set which implementation should be inject.

## Rules

* All this kind of classes are initialized with `singleton` mode, you shouldn't inject Value Object, POJO.
* All the injection points are based on some part of `JSR330` but not all.