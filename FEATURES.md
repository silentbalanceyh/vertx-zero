# Support Feature List

I recommend to download the latest zero framework from <https://gitee.com/silentbalanceyh/vertx-zero.git>, so you can use the whole features.

* **Micro** means "Micro Service Environment"
* **Aeon Environment** is Native Cloud Environment. 

## 1. JSR

| Name              | Comment                                                                                                                                           |
|-------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| **JSR311**        | RESTful specification supported, you can use most of JSR311 annotations.                                                                          |
| Extend **JSR311** | Defined new annotations `@BodyParam`, `@StreamParam`, `@Codex` to process web request.                                                            |
| **JSR330**        | Dependency injection specification supported, this feature is implemented with [Google Guice](https://github.com/google/guice) in latest version. |
| **JSR303**        | Bean validation specification supported.                                                                                                          |
| Extend **JSR303** | Provide validation based on rule configuration ( yaml definition ) to verify the data in json format `JsonObject/JsonArray`.                      |
| **JSR340**        | Web-filter specification supported, defined new annotation `@WebFilter` in Zero.                                                                  |

## 2. Common

| Keyword           | Comment                                                                                                                                                                                                                                                                                                                                                                        |
|-------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Re-Actor**      | This design patterns splitted zero components into `Agent` and `Worker` instead of multi-tier structure ( `Service/Dao/Model` ), the communication between above two are designed by name address instead of java `interface`, they are not coupling.                                                                                                                          |
| **Non-Blocking**  | All the components in zero framework are **asychronous** and the container process web request very smartly without any blocking, it require you do more FP style programming in Zero instead of traditional style.                                                                                                                                                            |
| **Yaml**          | All critical configuration files are formatted with yaml syntax, and it's similar with spring framework.                                                                                                                                                                                                                                                                       |
| **Jooq**          | Zero framework access different SQL database via Jooq as standard way, and I provide user-defined **Query Engine** ( json syntax ) to support complex querying such as pagination, sorting, grouping, joining and advanced searching.                                                                                                                                          |
| **MongoDb**       | Support mongo db toolkit to access mongo database very cool, as the Query Engine syntax is the same as SQL database, they shared uniform API in programming.                                                                                                                                                                                                                   |
| **AOP Security**  | Zero Authorization support the whole mode that Vert.x supported ( **Basic**, **OAuth** etc) , you can provide your own implementation to process code logical details. All the authoentication(401)/authorization(403) components are designed in **AOP** part, it could be plug-in/plug-out smartly and all the security components and business components are not coupling. |
| **Exception**     | Zero provides a dynamic mgaic exception system to catch different errors that you needed in enterprise application, You can write very less code to implement zero exception and throw out, it will be captured by container.                                                                                                                                                  |
| **Data Contract** | Zero designed uniform data format between front-end / back-end communication, there is another project [zero ui](http://www.vertxui.cn), it's frant-end application.                                                                                                                                                                                                           |
| **Query Engine**  | Zero provides query engine interface to face three situations: SQL database, excel table, mongo database, it uses uniform syntax ( Json Format ) to support different data querying.                                                                                                                                                                                           |
| **Job**           | Zero provides background scheduler/jobs sub-system to support task processing, the default task manager is implemented with `vert.x`, you can provide other implementations.                                                                                                                                                                                                   |
| **Plugin**        | All the zero components are mixed by **Infix Architecture**, you can write extensions in zero framework by yourselves.                                                                                                                                                                                                                                                         |
| **History**       | Zero provide default history system to record all the data histories in historical database ( The secondary database here ), you can do any changes in your application and rollback the data to fixed version that you needed.                                                                                                                                                |
| **Multi-**        | Zero could support following multi environments, they could run in uniform environment such as platform here: **Multi-Application, Multi-Language, Multi-Tenant**.                                                                                                                                                                                                             |
| **WebSocket**     | Zero provides websocket feature in the latest version, it's implemented based on Stomp.                                                                                                                                                                                                                                                                                        |
| **EMF**           | Zero support modeling method via Java class and the table of database, also it support dynamic modeling based on configuration, the dynamic modeling feature support Eclipse Modeling Framework specification.                                                                                                                                                                 |
| **Monad**         | Most components in Zero were designed based on FP programming, the framework provides **Ux/Ut/Fn** supervise toolkit API to help developers to implement monad development smartly.                                                                                                                                                                                            |

## 3. Micro Service

| Keyword     | Comment                                                                                                                                                                                                                                        |
|-------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **gRPC**    | Zero used gRPC as default communication equipments, it hides the details of gRPC, the developers could write code with common programming-style. This feature resolves service communication between different micor-services.                 |
| **etcd3**   | When you want to implement registry center in micro environment, zero use `etcd3` as default ( Also you can provide other implementations ). It let zero could run on **k8s** environment very soon.                                           |
| **istio**   | zero-micro implementation uses **k8s / istio / etcd3** as default architecture, all the applications could run on above platform directly, running on **istio** is experimental in progress.                                                   |
| **mesh**    | Zero-micro provides new architecture based on service mesh concept, it defined `originator/coordinator/terminator` to mark each node or service, for data-communication, developers are not needed to know the details with common-programing. |
| **gateway** | Zero provides **Api Gateway** to connect different micro-services, it implement perfect web request forwarding and monitoring.                                                                                                                 |
| **K8S**     | Aeon Environment is running on K8S environment, it built Native Cloud system for users.                                                                                                                                                        |

### 4. Business Components

All the business part that you can refer `vertx-pin` project ( zero extensions ).

### 5. Integration Clients

> Below are all supported clients to connect ( Infix Architecture )

* **Radis**
* **Feign**
* **ElasticSearch**
* **MySql**
* **Mongo DB**
* **Neo4j**: Graphic Database
* **Jooq**
* **Etcd3**
* **FTP**
* **Git**: Low-Code back-end
* **gRPC**: Zero micro environment
* **Apache Poi**: Excel Importing / Exporting
* **Liquibase**: SQL Auto Deployment
* **Shell Framework**: DevOps shell administration
* **Stomp**: Websocket Enabled.
* **爱奇艺视频客户端**
* **阿里云短信客户端**



