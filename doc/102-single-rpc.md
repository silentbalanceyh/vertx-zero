# Rpc Basic in Zero

In zero system, the developer could focus on code logical instead of Rpc internally, this example will describe Rpc
development for micro service communication. For environment preparing, please refer following document

* [2.3 - Micro Service Environment](23-micro-service-environment.md)
* [10.1 - Rpc Configuration](101-rpc-configuration.md)

Then you can write your code in zero service.

## 1. Rpc Workflow

In zero system, it only support three rpc node:

* Originator
* Coordinator
* Terminator

Here are some different code.

### 1.1. Originator

```java
    @Ipc(to = "IPC://EVENT/ADDR", name = "ipc-coeus")
    public JsonObject ipc(@BodyParam final JsonObject data) ...
```

### 1.2. Coordinator

```java
    @Ipc(value = "IPC://EVENT/ADDR",
            name = "ipc-crius", to = "IPC://EVENT/FINAL")
    public String send(final Envelop envelop) ...
```

### 1.3. Terminator

```java
    @Ipc(value = "IPC://EVENT/FINAL")
    public String send(final Envelop envelop) ...
```

### 1.4. Workflow

As above describe, in one request, there should be following roles \( Here are two Rpc request flow \):

![](/doc/image/rpc-workflow.png)

* **Originator** and **Terminator** must be only one in IPC workflow.
* **Coordinator** could contains one or more.

## 2. Limitation

Because the** Originator** offten send request from Agent class, the signature of method is freedom. But for **
Coordinator** & **Terminator**, the method signature must obey following rules:

* This method must contains return value;
* If there are some async workflow, you can return Future&lt;T&gt; as result;
* The argument must be length = 1 and type = Envelop; \( Zero defined \)



