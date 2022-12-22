# Enable Eventbus

Vert.x provide event but to process async request workflow, Zero also support standard Event Bus

## 1. Source Code

### 1.1. Sender

The sender will send the data processed to EventBus

```java
package org.exmaple;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import jakarta.ws.rs.BodyParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/up/example")
@EndPoint
public class ZeroSender {

    @Path("/event")
    @POST
    @Address("ZERO://EVENT")
    public JsonObject sayEvent(
            @BodyParam final JsonObject data) {
        return data;
    }
}
```

### 1.2. Consumer

```java
package org.exmaple;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;

@Queue
public class ZeroConsumer {

    @Address("ZERO://EVENT")
    public Envelop reply(final Envelop message) {
    	 // JsonObject could be extract directly, not needed to pass T.class
        final JsonObject data = message.data();
        return Envelop.success(data);
    }
}
```

### 1.3. Console

```
...
Vert.x zero has found 3 incoming address from the system. Incoming address list as below: 
        Addr : ZERO://EVENT
...
[ ZERO ] ( Uri Register ) "/up/example/event" has been deployed by ZeroHttpAgent, ...
```

### 1.4. Curl

```
curl -H "Content-Type:application/json" -X POST --data '{"name":"Lang","email":"silentbalanceyh@126.com"}' \
	http://localhost:6083/up/example/event
{"brief":"OK","status":200,"data":{"name":"Lang","email":"silentbalanceyh@126.com"}}
```

### 1.5. Hints

* The address of Sender/Consumer must be one to one matching.
* The consumer class only support following two method signature:

  ```java
  // Java Style
  public Envelop reply(final Envelop message)
  
  // Vert.x Style
  public void async(final Message<Envelop> message)
  ```
