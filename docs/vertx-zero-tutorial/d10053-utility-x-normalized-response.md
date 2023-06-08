# Utility X, Normalized Response

This chapter we'll go through frequently used methods:

* `static <T> Handler<AsyncResult<T>> toHandler(Message<Envelop> message)`
* `static <T> Handler<AsyncResult<T>> toHandler(Message<Envelop> message, JsonObject data)`

## 1. Design Principle

Why we designed above two method in zero system, because we often met following situation:

```java
public void worker(Message<Envelop> message){
    // MongoClient used
    client.findOne("DB_TEST",filters,null, res -> {
        // Check result
        if(res.success()){
            // Null checking
            final JsonObject one = res.result();
            if(null != one){
                message.reply(Envelop.success(one));
            }else{
                // Exceptions
            }
        }else{
            // Exceptions
        }
    });
}
```

If you used future mode, you still will met this kind of situations:

```java
    @Address(Addr.TOPIC_ADD)
    public void add(final Message<Envelop> message) {
        final Topic topic = Ux.getT(message, Topic.class);
        topic.setOwnerId(Ux.getUserUUID(message, ID.DB_KEY));
        topic.setId(UUID.randomUUID());
        topic.setTitle(topic.getBrief());
        topic.setAuditTime(new Date());

        this.stub.create(topic).setHandler(handler -> {
            if(handler.success()){
                // Correct  
            }else{
                // Exception 
            }
        });
    }
```

Above two segments are not difficult, but we could know that the business logical only take one line, other lines are
checking and validation response.

## 2. Luck of Envelop

We consider how to resolve this issue for many times, especially for developers, one thing is that may be our design
will brake your freedom, but it's not conflicts. Utility X is the tool box provided by zero system and it's optional,
not require you use it in your project.

If you used the api methods in current chapter, you can modified your code as following:

```java
    @Address(Addr.TOPIC_ADD)
    public void add(final Message<Envelop> message) {
        final Topic topic = Ux.getT(message, Topic.class);
        topic.setOwnerId(Ux.getUserUUID(message, ID.DB_KEY));
        topic.setId(UUID.randomUUID());
        topic.setTitle(topic.getBrief());
        topic.setAuditTime(new Date());

        this.stub.create(topic).setHandler(Ux.toHandler(message));
    }
```

Here the method `Ux.toHandler` will process as following:

1. Check whether success, if failed zero system will convert your Throwable object into `WebException` and replied to
   client with normalized response format.
2. Check the result of business, prevent the `NullPointerException` with the method.

## 3. Summary

Above two apis are provided to developer to build normalized rest response instead of complex data structure, it may
limit the developer to do some user-defined works, but it could let your system developed more faster and smartly to
process errors. Base on our experience in Video App and Hotel Micro System, it saved much time to let us to code
some `if-else` to be sure business correction.





