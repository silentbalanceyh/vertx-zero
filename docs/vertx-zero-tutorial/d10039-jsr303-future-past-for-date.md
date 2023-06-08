# JSR303, @Future/@Past for date

This chapter still belong to JSR303, we'll introduce the annotations to limit the date time range here.

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

import javax.validation.constraints.Future;
import javax.validation.constraints.Past;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.Date;

@EndPoint
@Path("/api/jsr303")
public class DateActor {

    @Path("date")
    @GET
    public String sayDate(
            @Future
            @QueryParam("to") final Date future,
            @Past
            @QueryParam("from") final Date past
    ) {
        return "Hi, Future = " + future + ", Past = " + past;
    }
}
```

## 2. Testing

**URL** : [http://localhost:6083/api/jsr303/date?to=2017-09-11](http://localhost:6083/api/jsr303/date?to=2017-09-11)

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.DateActor, method = public java.lang.String up.god.micro.validation.DateActor.sayDate(java.util.Date,java.util.Date), message = must be a future date.",
    "info": "must be a future date"
}
```

**URL** : http://localhost:6083/api/jsr303/date?to=2018-09-11&from=2018-04-01

**Method** : GET

**Response** :

```json
{
    "code": -60000,
    "message": "[ERR-60000] (Validator) Web Exception occus: (400) - Request validation handler, class = class up.god.micro.validation.DateActor, method = public java.lang.String up.god.micro.validation.DateActor.sayDate(java.util.Date,java.util.Date), message = must be a past date.",
    "info": "must be a past date"
}
```

## 3. Summary

Then if you provide correct date values such as:

http://localhost:6083/api/jsr303/date?to=2018-09-11&from=2017-04-01

You should get following correct response:

```json
{
    "data": "Hi, Future = Tue Sep 11 00:00:00 CST 2018, Past = Sat Apr 01 00:00:00 CST 2017"
}
```



