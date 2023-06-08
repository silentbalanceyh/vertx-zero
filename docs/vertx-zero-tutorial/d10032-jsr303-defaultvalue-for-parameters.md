# JSR303, @DefaultValue for parameters

There is another annotation that will be useful to be sure the default value for parameters. For example, you have
defined a interface to get two parameters:

* **page**: The page index that you want to use in pagination
* **size**: The size of each page list.

Sometimes, we must be sure these parameters have values to prevent broking the lower service code logical, in this kind
of situation, if you haven't provide the values for both parameters, you can set the default values for these two
parameters instead. This chapter will describe how to use default value for your application in zero system.

Demo projects:

* **Standalone - 6083**: `up-rhea`

**Rules**:

1. JSR303 is only supported in Agent component in zero system, it means that after you have send the message to event
   bus, the JSR303 will be effectiveness.
2. When you write the code with the Interface Style \( Will introduce in forward tutorials \), JSR303 will not support
   this kind of situation.
3. For @BodyParam, it also impact Agent component only, but could support Interface Style instead of JSR303 and could
   provide more useful validations.

## 1. Source Code

```java
package up.god.micro.validation;

import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@EndPoint
@Path("/api/jsr303")
public class DefaultValueActor {

    @Path("notnull/default")
    @GET
    public String sayDefault(
            @DefaultValue("1")
            @QueryParam("page") final Integer page,
            @DefaultValue("20")
            @QueryParam("size") final Integer size
    ) {
        return "Hi, your default page = " + page + ", size = " + size;
    }
}
```

## 2. Testing

**URL** : http://localhost:6083/api/jsr303/notnull/default

**Method** : GET

**Response** :

```json
{
    "data": "Hi, your default page = 1, size = 20"
}
```

**URL** : http://localhost:6083/api/jsr303/notnull/default?page=2

**Method** : GET

**Response** :

```json
{
    "data": "Hi, your default page = 2, size = 20"
}
```

**URL** : http://localhost:6083/api/jsr303/notnull/default?page=2&size=15

**Method** : GET

**Response** :

```json
{
    "data": "Hi, your default page = 2, size = 15"
}
```

## 3. Summary

From above examples, you can set default value for different parameters, this default values will be used when the
parameters are not provided by client request.



