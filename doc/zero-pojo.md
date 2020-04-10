# Set POJO as parameters

## 1. Source Code

**Demo.java**: ( Implemented with Lombox )

```java
package org.exmaple;

import lombok.Data;

@Data
public class Demo {
    private String name;
    private String email;
}
```

EndPoint code

```java
package org.exmaple;

import javax.ws.rs.*;

@Path("/up/example")
@EndPoint
public class ZeroExpApi {
    // ... Other part

    @POST
    @Path("/body/pojo")
    public Demo sayPojo(
            @BodyParam final Demo data
    ) {
        return data;
    }
}
```

## 2. Console

```
[ ZERO ] ( Uri Register ) "/up/example/body/pojo" has been deployed by ZeroHttpAgent ...
```

## 3. Curl Testing

```
curl -H "Content-Type:application/json" -X POST --data '{"name":"Lang","email":"silentbalanceyh@126.com"}' \
	http://localhost:6083/up/example/body/pojo
{"brief":"OK","status":200,"data":{"name":"Lang","email":"silentbalanceyh@126.com"}}
```
