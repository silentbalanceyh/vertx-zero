# Utility X, User Data

This chapter we'll discuss the user data in zero system, it used `vertx-auth-common` framework and the user data are
stored into `User` object, our Utility X help you to extract data from `User` object instead you typed some duplicated
code to get the data especially extract user id/config etc.

Utility X provide following methods for you to get data from `User` object:

* `static String getUserID(Message<Envelop> message, String field)`
* `static UUID getUserUUID(Message<Envelop> message, String field)`
* `static Object getSession(Message<Envelop> message, String field)`
* `static String getUserID(Envelop envelop, String field)`
* `static UUID getUserUUID(Envelop envelop, String field)`
* `static Object getSession(Envelop envelop, String field)`

Above methods could help you to extract data from the object of `User`. Here should be some comments to explain the
method `getUserID`, this method is messing because the method name is not matching the feature that this method
provided, but because of some reasons on existing system, we'll rename in future plans.

Another thing is that about the first argument type: `Envelop` and `Message<Envelop`, it's for different style that you
used, the core points is that the pure type worker class could not use Utility X to get the data here, such as
following:

```java
@Address("ZERO://ASYNC/VERTX/FUTURE_T")
public Future<JsonObject> sayFutureT(final JsonObject data) {
    final String string = data.encode();
    System.out.println(string);
    data.put("result", "Perfect");
    return Future.succeededFuture(data);
}
```

Because above code could not get the uniform resource model `Envelop`, in this kind of situation you could not get the
user information with above style, it's also why we recommend you to use uniform resource model instead of some
user-defined types. There is one resolution for this situation:

1. Write the code with `non-interface`  style and put the user information in your **Sender**.
2. Then let Sender put the user data into the `Envelop` 's data part instead of extract information from `User`
   directly.

## 1. Source Code

> Because the Video App is before zero system, most of this app service used `void xxx(Message<Envelop>)` style instead
> of the latest Future style, but if you read current tutorials, it's very simple for you to convert to Future style.

```java
public class Extractor{
    // .....
    public static String getUserId(
            final Message<Envelop> message
    ) {
        String userId = Ux.getUserID(message, ID.DB_KEY);
        if (StringUtil.isNil(userId)) {
            final Object uid = Ux.getSession(message, "user");
            userId = (null == uid) ? null : uid.toString();
        }
        return userId;
    }
    // .....
}
```

Above code is the method in Video app to extract user id from web request, here are the workflow:

1. Try to get data from `Envelop`, if you enabled Authorization the user data came from http headers, because it's
   running in mongo db, here `ID.DB_KEY` constant value is the mongo common id:
   ```java
   String DB_KEY = "_id";
   ```
2. Then if you could not get the user id, try to extract user id from session for secondary extracting.

All the video app used above code to get user code as following:

```java
    private void execute(final Message<Envelop> message,
                         final TargetType type) {
        final JsonObject request = Ux.getJson(message);
        // Call the method to extract user id.
        final String userId = Extractor.getUserId(message);
        final JsonObject filter = new JsonObject();
        {
            filter.put("targetId", request.getValue("targetId"));
            filter.put("userId", userId);
            filter.put("targetType", type);
        }
        final JsonObject data = applyData(message, type);
        final Future<JsonObject> future = this.revertStub.comment(filter, data, true);
        future.setHandler(Ux.toHandler(message));
    }
```

## 2. UUID for specific

Another fast method provided by zero system Utility X tool is that the UUID converted from String such as following:

```java
    @Address(Addr.TOPIC_ADD)
    public void add(final Message<Envelop> message) {
        final Topic topic = Ux.getT(message, Topic.class);
        topic.setOwnerId(Ux.getUserUUID(message, ID.DB_KEY));
        topic.setId(UUID.randomUUID());
        topic.setTitle(topic.getBrief());
        topic.setAuditTime(new Date());
        // May contains exception.
        this.stub.create(topic).setHandler(Ux.toHandler(message));
    }
```

## 3. Summary

This chapter describes the user id extracting operations, it's very useful in many app, platform or application, it
could identity the web request user's information from background instead of pass the user data in query string, body or
other parts. It also could store user's status in restful application, that's why zero system designed this component.
The user's information in `User` is JsonObject, it means that you could store many information into this object and it
contains good extension.



