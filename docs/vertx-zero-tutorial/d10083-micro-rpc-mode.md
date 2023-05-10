# Micro, Rpc Mode

In zero system, it support two modes of Rpc:

* Stream Mode
* Direct Mode

Because rpc communication is used in zero different nodes only, we call it `IPC` -- Internal Process Communication, this
channel could help us to do many works in micro service environment.

## 1. Roles

In zero system, it support three rpc nodes, all these nodes contain different roles:

* Originator
* Coordinator
* Terminator

All these nodes should be configured as `rpc server` instead of others.

### 1.1. Configuration

Above three roles could be defined by different attributes, zero system will detect them on your method, another thing
is that these roles focus on **method** instead of service, it means that your method function has the role attribute,
but one service could contain `Originator, Coordinator, Terminator` etc. In the service you can do this configuration as
following:

```java
    // Originator 
    @Ipc(to = "IPC://EVENT/ADDR", name = "ipc-coeus")
    public JsonObject ipc(@BodyParam final JsonObject data) ...

    // Coordinator
    @Ipc(value = "IPC://EVENT/ADDR",
            name = "ipc-crius", to = "IPC://EVENT/FINAL")
    public String send(final Envelop envelop) ...

    // Terminator
    @Ipc(value = "IPC://EVENT/FINAL")
    public String send(final Envelop envelop) ...

    // Terminator -> JVM Specification, single value could be ignored.
    @Ipc("IPC://EVENT/FINAL")
    public String send(final Envelop envelop) ...
```

From above java code you should know the role definition focus on method, when your service contains many classes with
different methods, these methods could be different roles.

## 2. Stream Mode

In stream mode, all the data flow should be single direction, it could not be reverted, the data flow is as following:

![](/doc/image/d10083-1.png)

Above pictures contains three ipc data flow:

1. `Service A -> Service B`
2. `Service C -> Service D -> Service B`
3. `Service C -> Service D -> Service E -> Service F -> Service B`

Based on above ipc workflow you should know:

1. `Service A & Service C` are originator, it means that this role could receive the request from client
2. `Service B` is the terminator, it could reply the final response to client.
3. Other nodes `Service F, Service E, Service D` are coordinators, they could receive from previous service nodes and
   continue to process the method and send the result to the next nodes.

## 3. Direct Mode

This mode is frequently used in our micro service business requirements, we call Rpc Service directly with rpc client,
but in this kind of situation, there is only one role that often be used: **Terminator/Coordinator**, here the Consumer
act as Originator instead, in this kind of situation, it's not needed to provide Originator role.

![](/doc/image/d10083-3.png)

> This mode is used frequently in zero micro service project.

## 4. Summary

Actually stream mode and direct mode are not conflicts, but for different usage here.

* Direct Mode just like the bridge between `Consumer` and `Rpc Node Tier`.
* Stream Mode describes the internal `Rpc Node Tier` structure for stream data flow use.

You can consider the real business scenario and select the usage.

