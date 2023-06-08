# Getting Start

This article belong to new tutorials of Vert.x Zero Up Framework for developer, you can refer all the articles to know
how to use it in your real project. At first you must know what is Vert.x Zero Up Framework ? It's a framework in
service mesh environment to prevent developers to know details of micro services, but the developers could go through
all the business development in this framework.

_All examples will be put in _`vertx-zeus`_ sub projects._

This chapter described how to start up zero.

## 1. Standalone Zero

If you used zero up only in standalone environment, you can put following simple code in your `main` entry of java.

```java
package up.god;

import io.vertx.up.VertxApplication;
import io.vertx.up.annotations.Up;

@Up
public class io.god.Anchor {
    public static void main(final String[] args) {
        VertxApplication.run(io.god.Anchor.class);
    }
}
```

Then you can run this program as Java Application, you'll see some logs in your console:

```shell
......
[ ZERO ] ( Http Server ) ZeroHttpAgent Http Server has been started successfully. \n
    Endpoint: http://172.20.16.41:6083/.
......
```

The default http server port is **6083**.

## 2. Micro Zero

Except about standalone mode, zero also support another mode \( Micro \) to deploy all nodes in micro service
environment, please be careful about micro environment, there need some additional configuration for zero to support
micro services environment, please refer following tutorial to do the preparing works first. The demo projects are
following in `vertx-zeus`,

* **Api Gateway - 6100** : `vertx-athena`
* **Service - 6002**: `vertx-uranus`

### 2.1. Api Gateway Configuration

In your resources folder, you should prepare following configuration files:

```properties
src/main/resources/vertx.yml
src/main/resources/vertx-etcd3.yml
src/main/resources/vertx-server.yml
```

#### vertx.yml

`vertx.yml`up.god.file must contains extend lime node as following:

```yaml
zero:
  lime: etcd3      # This configuration is required for microservice environment
  vertx:
    instance:
    - name: vx-zero
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

#### vertx-server.yml

`vertx-server.yml` up.god.file must define another server type for Api Gateway

```yaml
server:
- name: gateway-athena
  type: api        # This configuration type is different from service default.
  config:
    port: 6000
    host: 0.0.0.0
```

#### vertx-etcd3.yml

`vertx-etcd3.yml` up.god.file contains etcd3 registry center configuration information:

```yaml
etcd:
  micro: zero-istio      # Be sure the micro of api/service are both the same, then they could communicate.
  nodes:
  - host: localhost
    port: 6181
  - host: localhost
    port: 6180
  - host: localhost
    port: 6179
  timeout: 2
```

In zero framework, the etcd3 used e3w, please refer the link to check how to preparing e3w to start etcd3 registry
center. [https://github.com/soyking/e3w](https://github.com/soyking/e3w) , you can install the tool etcd3-viewer to
manage etcd3 data from Web
Client: [https://github.com/nikfoundas/etcd-viewer](https://github.com/nikfoundas/etcd-viewer). In zero, the default
ports of e3w have been modified to `6181, 6180, 6179`.

### 2.2. Api Gateway

Api Gateway source code is the same as standalone service as following:

```java
package up.god;

import io.vertx.up.VertxApplication;
import io.vertx.up.annotations.Up;

@Up
public class io.god.Anchor {

    public static void main(final String[] args) {
        VertxApplication.run(io.god.Anchor.class);
    }
}
```

### 2.3. Service Configuration

Then you can prepare following service node of zero to support micro environment and communicate with api gateway.

```properties
src/main/resources/vertx.yml
src/main/resources/vertx-etcd3.yml
src/main/resources/vertx-server.yml
```

#### vertx.yml

`vertx.yml` up.god.file is the same as api gateway, be sure instance name are the same as api gateway.

```yaml
zero:
  lime: etcd3
  vertx:
    instance:
    - name: vx-zero
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

#### vertx-server.yml

`vertx-server.yml` up.god.file should be default configuration, you can set port for this micro service node.

```yaml
server:
- name: up-uranus
  type: http
  config:
    port: 6002
    host: 0.0.0.0
```

Here ignore `vertx-etcd3.yml` up.god.file content because they are the same. If you put all service node in one
environment, all the service nodes configuration of etcd3 must be the same.

### 2.4. Service

Service source code is the same as Standalone mode here.

```java
package up.god;

import io.vertx.up.VertxApplication;
import io.vertx.up.annotations.Up;

@Up
public class io.god.Anchor {

    public static void main(final String[] args) {
        VertxApplication.run(io.god.Anchor.class);
    }
}
```

## 3. Console in Micro Zero

Once you have finished micro zero configurations, you can start micro zero. Because api gateway will look up all the
service nodes, it's no impact for all service node start up sequence. You can start api gateway first and then service
node, or you also could start up service node first and then api gateway.

### 3.1. Service Node

You should see following output in your console:

```shell
......
    [ Up Micro ] <Application Name> = "zero-istio",
    [ Up Micro ] Configuration Path = /zero/zero-istio/endpoint/routes/up-uranus:172.20.16.41:6002, 
    [ Up Micro ] Service Name = up-uranus,
    [ Up Micro ] EndPoint = http://172.20.16.41:6002
    [ Up Micro ] Route Uris = 
    [ Up Micro ]     
    [ Up Micro ] √ Successfully to registered Routes, wait for discovery......SUCCESS √
......
```

### 3.2. Api Gateway

You should see following logs in api gateway instead of information of standalone mode:

```shell
......
[ ZERO ] ( Api Gateway ) ZeroApiAgent (id = ae815cba-4016-43f5-9be2-71a4ee23247c) has deployed on 6000.
[ ZERO ] ( Api Gateway ) ZeroApiAgent has been started successfully. Endpoint: http://0.0.0.0:6000/.
......
```

Then you should see following logs every 3 seconds

```shell
......
[ ZERO ] ( Discovery ) Records ( added = 0, updated = 1, deleted = 0 ) have been refreshed!
[ ZERO ] ( Discovery ) Records ( added = 0, updated = 1, deleted = 0 ) have been refreshed!
......
```

Here are some reports of api gateway to discovery service counting.

## 4. Summary

Here are getting start for you to start zero, in total zero support two modes: **Standalone & Micro**. The micro mode is
a little complex to start up, for standalone mode, it's very simple to start up. Now once you have finish current
tutorial you should know:

* How to write launcher code in standalone / micro modes
* How to configure micro mode for api gateway / service node
* How to write launcher code for api gateway



