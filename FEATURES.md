# Support Feature List

Here are smart introduction of snapshots to see zero feature in `Code Style`:

* [Snapshots of Zero](FEATURE-POINTS.md) ( Pending )
  All the features are as below:

## 1. Supports

> Micro means "Micro Service Environment"

### 1.1. JSR

* **JSR311** RESTful specification supported, you can use JSR311 annotations in Zero.
* Extend **JSR311** to define new annotations `@BodyParam`, `@StreamParam`, `@Codex` to process web request.
* Support different programming-styles that `vert.x` recommend ( Callback, Future, Rx ) etc.
* **JSR330** dependency injection specification supported, this feature is for `singleton` instance of business
  components, the limitation is that it could not be used on VO.
* **JSR303** bean validation specification supported.
* Extend **JSR303** to provide validation based on rule configuration ( yaml file ), also it provides validation
  architecture for the data with json format. ( `JsonObject/JsonArray` ).
* **JSR340** web-filter specification supported, you can use `@WebFilter` annotation in Zero.

### 2. Common

* `Re-Actor` design patterns to split Zero components into `Agent` and `Worker` instead of multi-tier
  structure ( `Service/Dao/Model` ), the communication between `Agent` and `Worker` are designed by name address instead
  of java `interface`, they are not coupling.
* All the components in Zero are **asychronous** and it process web request very fast without any blocking, you also
  could do more FP programing in Zero instead of traditional style.
* **Yaml** configuration file supported, you can use yaml syntax to write configuration files in zero framework, it's
  similar with spring framework.
* **Jooq** supported, in zero framework you can use jooq to access different database more convenient, it provides
  user-defined **Query Engine** to support complex querying such as pagination, sorting, grouping, joining and advanced
  searching.
* **MongoDb** supported, it provide mongo db tool-kit to access mongo database very cool, also the Query Engine syntax
  is the same as SQL database, they shared uniform API in programming.
* **Native Client** supported, you can use some `XClient` those are in `vert.x` directly, here I defined those kinds of
  clients **Native**. It means that if you don't want to use `Jooq/Mongo` in zero, you can implement your code logical
  with nativa clients such as `MySqlClient, MongoClient, RedisClient, SessionClient, SharedClient` etc, these clients
  are widely used in `vert.x` framework.
* **Security**: zero provides two authorization mode nested supported: **Basic** / **OAuth** ( Include **Jwt** ) , you
  can provide your own implementation to process code logical details. All the security components are designed in **
  AOP**, it could be plug-in/plug-out smartly. All the security components and business components are also not
  coupling.
* **Exception**: zero provides a dynamic exception system to catch all the errors that you needed in enterprise
  application. You can write very less code to implement zero exceptions and throw out in web flow
  request. `Future.successedFuture / Future.failedFuture` uniform returned.
* **Data Contract**: zero provides uniform data format between front-end / back-end communication, there is another
  project [zero ui](http://www.vertxui.cn), it's frant-end application.
* **Query Engine**: zero provides query engine interface to face three situations: SQL database, excel table, mongo
  database, it uses uniform syntax ( Json Format ) to support different data querying.
* **Job**: zero provides background scheduler/jobs sub-system to support task processing, the default task manager is
  implemented with `vert.x`, you can provide other implementations.
* **Plugin**: all the zero components are mixed by plugin architecture, you can write extensions in zero framework by
  yourselves.
* **History**: zero provide default history system to record all the data histories in historical database ( The
  secondary database here ), you can do any changes in your application and rollback the data to fixed version that you
  needed.
* **Multi-**: Zero could support following multi environments, they could run in uniform environment such as platform
  here:
    * **multi database**
    * **multi language**
    * **multi tenants**

### 3. Micro Service

* (Micro) **gRPC** supported, zero used gRPC as default communication equipments, it hides the details of gRPC, the
  developers could write code with common programming-style. This feature resolves service communication between
  different micor-services.
* (Micro) **etcd3** supported, when you want to implement registry center in micro environment, zero use `etcd3` as
  default ( Also you can provide other implementations ). It let zero could run on **k8s** environment very soon.
* (Micro) **istio** supported, zero-micro implementation uses **k8s / istio / etcd3** as default architecture, all the
  applications could run on above platform directly, running on **istio** is experimental in progress.
* (Micro) **mesh** supported, zero-micro provides new architecture based on service mesh concept, it
  defined `originator/coordinator/terminator` to mark each node or service, for data-communication, developers are not
  needed to know the details with common-programing.
* (Micro) **gateway** supported, zero provides **Api Gateway** to connect different micro-services, it implement perfect
  web request forwarding and monitoring.

### 4. Business Components

All the business part that you can refer `vertx-pin` project ( zero extensions ).

### 5. Integration Clients

> Below are all supported clients to connect.

* **Radis**
* **Feign**
* **ElasticSearch**
* **MySql**
* **Mongo DB**
* **Neo4j**: Graphic Database
* **Jooq**
* **Etcd3**
* **gRPC**: Zero micro environment
* **Apache Poi**: Excel Importing / Exporting
* **爱奇艺视频客户端**
* **阿里云短信客户端**

## 2. Objective

* The `programming-style` is the same as spring framework, it's better for some **spring developers** to move on zero
  framework very smartly.
* In enterprise application, it's CRUD but not only CRUD, in this kind of situation, Zero provides enhancement tools and
  extension modules to help developers to focus requirements more than technical details.
* For beginners of `vert.x`, this tookit is a lttle complex but it's high performance, Zero could help developers to
  use `vert.x` smartly.
* I also recommend to use FP ( funcional programming ) to do more things in your projects, most of Zero core components
  are designed based on **lambda** ( JDK 1.8 ) with FP.
* Zero provide plug-in structure to help developers to implement different plug-ins and extensions so that you can face
  more business scenarios. 



