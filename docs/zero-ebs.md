# Event Bus only version

In most situation, the sender is not needed, zero also support another sample version to transfer the data to event bus
directly.

## 1. Source Code

### 1.1. Sender Interface Only

In this kind of version, the request will go through based on interface and transfer the data to worker directly. Be
careful this class is **interface** instead of class.

```java
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import jakarta.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/cronus")
public interface SpeakAddrApi {

    @Path("/direct")
    @POST
    @Address("ZUES://DIRECT")
    JsonObject speak(@BodyParam JsonObject data);
}
```

### 1.2. Consumer

```java
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class SpeakAddrWorker {

    @Address("ZUES://DIRECT")
    public void direct(final Message<Envelop> data) {
        final JsonObject replied = (data.body().data(0, JsonObject.class));
        data.reply(Envelop.success(replied));
    }
}
```

This version will skip no code logical agent and send the data to worker directly.

### 1.3. Argument Mapping

Be careful about the data method of Envelop in above code:

```java
public <T> T data(final Integer argIndex, final Class<T> clazz)
```

Actually, the message will transfer the parameters from interface definition by index as JsonObject key to extract the
data from EventBus. Other rules will be useful to apply all the type here, you also could use POJO type as the second
argument.

### 1.4. Limitation

*
    1. Hibernate Validation will be failure because there is no implementation class, but advanced validation could be
       used `@Codex`.
*
    2. The worker method must be a specification under zero definition.
