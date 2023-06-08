# Zero Programming Styles

Until now we focus on code of zero system, this chapter will introduce some specifications that will be used in zero
system include

* The roles in zero system.
* The code structure in zero system project.
* The name specifications in zero system.

Demo projects:

* **Standalone - 6083**: `up-rhea`

## 1. Roles

In zero system, we focus on three code areas:

* Agent Area \( Event Loop of vert.x \): You should develop the component **Sender**.
* Worker Area \( Worker Pool of vert.x \): You should develop the component **Consumer**.
* Service Layer: You should define service interface and implementation classes, actually this role should be ignored by
  zero system because it's **out of zero system managed scope**.

Http Request flow will go through zero system as following:

```shell
Api/IrApi -> Sender ( Agent Area ) -> ( Event Bus ) -> Consumer ( Worker Area ) 
    -> Stub/Service ( Service Layer )
```

## 2. Code Structure

Here we introduce some code structures in zero real projects for developer/reader to do works.

### 2.1. Demo Structure

This style is not recommend in production environment because this style provide demo for you to study.

* **XxxActor**: The sender of zero system.
* **XxxWorker**: The consumer of zero system.

### 2.2. Standard Style

This style is for you to do some common Crud works only, it should not contain complex query or search operations.

* **XxxApi**: The sender interface of zero system.
* **XxxActor**: The sender implementations of zero system.
* **XxxWorker**: The consumer of zero system.
* **XxxStub/XxxService**: The service interface & implementation classes in service layer.

### 2.3. Splitting Style

This style is for you to do read/write splitting in your real projects, it should contain following role and operations:

* **XxxIrApi**: The sender interface of zero system that contain `query/search/read` operations;
* **XxxIrActor**: The sender implementations of zero system that inherit from **XxxIrApi**;
* **XxxApi**: The sender interface of zero system that contain `insert/update/delete/approval` operations;
* **XxxActor** : The sender implementations of zero system that inherit from **XxxApi**;
* **XxxWorker** : The consumer for two actors of zero system \( IrActor/Actor \);
* **XxxStub/XxxService** : The service interface & implementation classes in service layer.

### 2.4. Interface Style

This style is often used and it's fast mode, this style ignored `Actor` in the codes and it's connected between Api and
Worker only.

* **XxxIrApi/XxxApi**: The sender interface of zero system.
* **XxxWorker**: The consumer for two actors of zero system \( IrApi/Api \).
* **XxxStub/XxxService** : The service interface & implementation classes in service layer.

## 3. Name Specification

This specification is provided to developers to do standard develop works in zero system, if you obey this rules that
defined in current tutorial, you'll very luck to do this things more simpler.

If your project name is `kys`, we could define the root package named `com.kys`, then you should provide following
sub-packages for different usage:

* **up.god.cv**: Constant Value packages.
* **exception**: User-defined exception packages.
* **micro**: Your system modules for each micro business, all the subfolder should contains one style code files.
* **domain** : Domain model classes that defined in current business.

The final package structure \( include codes \) should be as following examples:

```shell
com.kys.up.god.cv
    - Addr.java
com.kys.exception
com.micro.a
    - XxxApi
    - XxxActor
    - XxxWorker
    - XxxStub
    - XxxService
com.domain
    - Xxx
```

The code name should be:

* **Actor**: The filename should end with **Actor**; the actor could inherit from api that end with **Api** or **IrApi**
  , in interface style you can ignore Actor class component.
* **Worker**: The filename should end with **Worker**, this is standalone component.
* **Stub/Service**:  The Stub is interface and Service is implementation class.



