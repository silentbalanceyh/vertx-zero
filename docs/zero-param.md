# @BodyParam

`javax.ws.rs.@BodyParam` does not belong to JSR311 but extend by Zero.

## 1. Source Code

```java
package org.exmaple;

import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.*;

@Path("/up/example")
@EndPoint
public class ZeroExpApi {
    // Other part
    
    @POST
    @Path("/body/json")
    public JsonObject sayBody(
            @BodyParam final JsonObject data
    ) {
        return data;
    }
}
```

## 2. Console

```
[ ZERO ] ( Uri Register ) "/up/example/body/json" has been deployed by ZeroHttpAgent ...
```

## 3. Curl Testing

```shell
curl -H "Content-Type:application/json" -X POST --data '{"message": "lang Zero"}' \
	http://localhost:6083/up/example/body/json
{"brief":"OK","status":200,"data":{"message":"lang Zero"}}
```

## 4. Hints

* The `@BodyParam` will be serialized with Jackson, but it's extend by zero to support more types such as
    * `io.vertx.core.buffer.Buffer`
    * `io.vertx.core.json.JsonObject`
    * `io.vertx.core.json.JsonArray`
* For POJO type, it will be also serialized with Jackson
* Be careful about the method return type, if we changed `JsonObject` to `String` by default configuration, the response
  will be as following:

  	```
  	{"brief":"OK","status":200,"data":"{\"message\":\"lang Zero\"}"}
  	```
