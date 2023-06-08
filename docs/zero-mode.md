# Request Mode

Zero system support five request modes as following ( Please ignore the name of the mode, it's for distinguish
differences only )

## 1. Mode 1: Sync Mode

This mode is used for request response mode in vert.x, you can define your method as following:

```java
    @GET
    @Path("/sync/string")
    public String syncRequest(
            @QueryParam("email") final String email
    ) {
        final String response = "Testing finished";
        return response;
    }
```

**Rule**:

1. The return type of method type mustn't be `void`;
2. Do not use `@Address` annotation on this method;

**Workflow**

![Mode1](image/request-mode1.png)

## 2. Mode 2: Block Mode

This mode is used for request only mode in vert.x, you cand define your method as following:

```java
    @GET
    @Path("/block/{name}")
    public void blockRequest(
            @PathParam("name") final String name) {
        System.out.println("block request");
    }
```

**Rule**

1. The return type of method must be `void`;
2. Do not want to get any data from response, this kind of mode only provide status of this job

**Workflow**

![Model](image/request-mode2.png)

## 3. Mode 3: One Way Mode

This mode is samiliar with Mode2, but the data will send to event bus to execute async jobs. ( Will be removed )

**Sender**

```java
    @POST
    @Path("/one-way/user")
    @Address("ZERO://USER")
    public String sendNotify(
            @BodyParam final User user) {
        final String response = Jackson.serialize(user);
        return response;
    }
```

**Consumer**

```java
    @Address("ZERO://USER")
    public void reply(final Envelop message) {
        final User user = message.data(User.class);
        // Do somethings
    }
```

**Rule**

1. The methods of `@EndPoint` and `@Queue` must be annotated with `@Address` and they are the same between sender and
   consumer
2. The return type of method in `@EndPoint` mustn't be `void`
3. You must be define the consumer method signature to `public void xxx(Envelop)`

**Workflow**

![Model 3](image/request-mode3.png)

## 4. Mode 4: Async Mode ( Java Style )

This mode is async request response mode between consumer and sender on event bus.

**Sender**

```java
    @POST
    @Path("/async/user")
    @Address("ZERO://ROLE")
    public String sendAsync(
            @BodyParam final User user) {
        final String response = Jackson.serialize(user);
        return response;
    }
```

**Consumer**

```java
    @Address("ZERO://USER")
    public Envelop reply(final Envelop message) {
        final User user = message.data(User.class);
        final WebException error = new TestRequestException(getClass(),
                "Lang", "Detail");
        return Envelop.failure(error);
    }
```

**Rule**

1. The methods of `@EndPoint` and `@Queue` must be annotated with `@Address` and they are the same between sender and
   consumer
2. The return type of method in `@EndPoint` mustn't be `void`
3. You must be define the consumer method signature to `public Envelop xxx(Envelop)`

**Workflow**

![Model 4](image/request-mode4.png)

## 4. Mode 5: Vert.x Async Mode ( Vert.x Style )

This mode is supported for some vert.x component use in service layer of the system such as MongoClient, SQLClient etc.

**Sender** ( The same as mode 4 )

```java
    @Path("/event")
    @POST
    @Address("ZERO://EVENT")
    public JsonObject sayEvent(
            @BodyParam final JsonObject data) {
        return data;
    }
```

**Consumer**

```java
    @Mongo
    private transient MongoClient client;
    
    @Address("ZERO://ROLE")
    public void async(final Message<Envelop> message) {
        final User user = Envelop.data(message, User.class);
        final JsonObject userData = new JsonObject(Jackson.serialize(user));
        this.client.save("DB_USER", userData, res -> {
            if (res.succeeded()) {
                message.reply(Envelop.success("Hello World"));
            } else {
                res.cause().printStackTrace();
            }
        });
    }
```

**Rule**

1. The methods of `@EndPoint` and `@Queue` must be annotated with `@Address` and they are the same between sender and
   consumer
2. The return type of method in `@EndPoint` mustn't be `void`
3. You must be define the consumer method signature to `public void xxx(Message<Envelop>)`
4. Don't forget call `reply(Envelop` in call back on `Message<Envelop>`

**Workflow**

![Mode 5](image/request-mode5.png)

All above request mode could describe different usage, but we recomment to use Mode 4 & Mode 5. 