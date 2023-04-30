# Resource Model

## 1. Envelop

Envelop word refer from SOAP web service, and it contains following field:

1. status: `io.horizon.eon.em.web.HttpStatusCode` - Http Status Enum data.
2. headers: `io.vertx.core.MultiMap` - Http Headers ( Direct from Vert.x )
3. error: `io.horizon.exception.WebException` - Abstract Web Error defined by Zero
4. data: `io.vertx.core.json.JsonObject` - Data Part in current Envelop
5. user: `io.vertx.ext.auth.User` - User Info ( Direct from Vert.x )

## 2. Common Api

```java
// Extract data from Envelop
final JsonObject data = message.data();

// Extract T from Envelop
final Demo user = message.data(Demo.class);

// Extract data from Message<Envelop>, static ( Be careful )
final Demo user = Envelop.data(message, Demo.class);

// Build Successful Envelop
final Envelop envelop = Envelop.ok();

// Build Successful Envelop with data
Demo user = ...
final Envelop envelop = Envelop.success( user );

// Build Error Envelop with WebException
WebException exception = ...
final Envelop error = Envelop.failure(exception);

```

## 4. Data Format

Success:

```json
{
    "data": "Different data format for this field"
}


```

Failure

```json
{
    "message": "Error message description",
    "code": -40013
}
```

* brief: Http Status
* status: Http Status Code
* data: Data part ( Successful only )
* code: Zero Error Code ( Negative number )
* message: Zero Message ( Describe the number meaning )
