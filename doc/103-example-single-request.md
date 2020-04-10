# Example: Single Request

This document describe the simplest Rpc workflow in zero system. please refer following pictures:

## ![](/doc/image/exp1-rpc.png)1. Originator

In your originator, you can write code as following:

```java
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.annotations.Ipc;

import javax.ws.rs.BodyParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@EndPoint
@Path("/cronus")
public class SpeakApi {

    @Path("/forward")
    @POST
    @Ipc(to = "IPC://EVENT/ADDR", name = "ipc-coeus")
    public JsonObject ipc(@BodyParam final JsonObject data) {
        System.out.println(data);
        return data;
    }
}
```

## 2. Terminator

You must be sure current terminator name is "ipc-coeus" in `vertx-server.yml` as following:

```yaml
- name: ipc-coeus
  type: ipc
  config:
    port: 6884
    host: 0.0.0.0
```

Then you can write the code as following:

```java
import io.vertx.up.annotations.Ipc;
import io.vertx.up.commune.Envelop;

public class SpeakWorker {

    @Ipc(value = "IPC://EVENT/ADDR")
    public String send(final Envelop envelop) {
        final JsonObject data = envelop.data(JsonObject.class);
        data.put("role", "Terminator");
        return data.encode();
    }
}
```

**\*: Be sure the "to" attribute in Originator is the same as "value" in Terminator**

## 3. Start Up

In the coeus console you should see following:

```
    [ Up Rpc   ] <Application Name> = "zero-istio",
    [ Up Rpc   ] Configuration Rpc Point = /zero/zero-istio/ipc/routes/ipc-coeus:10.0.0.7:6884, 
    [ Up Rpc   ] Service Name = ipc-coeus,
    [ Up Rpc   ] Ipc Channel = grpc://10.0.0.7:6884
    [ Up Rpc   ] Ipc Address = 
    [ Up Rpc √ ]     IPC://EVENT/ADDR
    [ Up Rpc   ] √ Successfully to registered IPCs, wait for community......SUCCESS √
```

Then in cronus console you should see following:

```
    [ Up Micro ] <Application Name> = "zero-istio",
    [ Up Micro ] Configuration Path = /zero/zero-istio/endpoint/routes/up-cronus:10.0.0.7:6083, 
    [ Up Micro ] Service Name = up-cronus,
    [ Up Micro ] EndPoint = http://10.0.0.7:6083
    [ Up Micro ] Route Uris = 
    [ Up Micro ]     /cronus/direct
    [ Up Micro ]     /cronus/forward
    [ Up Micro ] √ Successfully to registered Routes, wait for discovery......SUCCESS √
```

## 4. Send request

Send request to [http://10.0.0.7:6083/cronus/forward](http://10.0.0.7:6083/cronus/forward), you should see following output

**Request Body:**

```json
{
  "username":"lang.yu",
  "password":"173AFAD5992A3F73A472FC09B05B1FB7"
}
```

**Response Body:**

```json
{
    "data": {
        "username": "lang.yu",
        "password": "173AFAD5992A3F73A472FC09B05B1FB7",
        "role": "Terminator"
    }
}
```

In this way, these two services communicated and the second service put the "Terminator" value in "role" key, the request flow in zero should be as following \( up-coeus / up-cronus \) are both two different micro services:

```
1. Client Request -> ( Origanitor ) Service = up-cronus
2. ( Origanitor ) -> ( Terminator ) Service = up-coeus, Ipc = ipc-coeus
3. ( Terminator ) -> Call method to put role=Terminator into data
4. ( Terminator ) -> Generate new response -> Client Request
```



