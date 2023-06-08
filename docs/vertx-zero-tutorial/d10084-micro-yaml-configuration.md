# Micro, Configuration

From this chapter we'll focus on RPC usage in zero system, because they are micro environment, we need to prepare more
service nodes here.

Demo Projects and environment

| Http Port | Ipc Port | Ipc Service Name | Project       | Role           |
|:----------|:---------|:-----------------|:--------------|:---------------|
| 6100      | --       | --               | up-athena     | Api Gateway    |
| 6201      | --       | --               | up-atlas      | Common Service |
| 6301      | 6311     | ipc-epimetheus   | up-epimetheus | Originator     |
| 6401      | 6411     | ipc-coeus        | up-coeus      | Coordinator A  |
| 6402      | 6412     | ipc-crius        | up-crius      | Coordinator B  |
| 6403      | 6413     | ipc-cronus       | up-cronus     | Coordinator C  |
| 6501      | 6511     | ipc-hecate       | up-hecate     | Terminator     |

Why we need so many projects ? Because in micro service tutorials we'll focus on Service Registry, Discovery and
Communication, we consider use more projects to describe different scenarios and let developers know how to develop
micro services in zero framework.

## 1. Etcd Configuration

You can refer [D10074 - Configuration, vertx-etcd3.yml](d10074-configuration-vertx-etcd3yml.md) tutorials for etcd
configuration for zero node. Because of our examples here we listed following different configurations

* Api Gateway Configuration
* Common Service \( None Rpc Server \)
* Service Node \( With Rpc Server, Originator/Coordinator/Terminator \)

As we introduced in previous tutorials, here all the configuration for etcd3 should be the same as following, in my demo
we used around 3 nodes of etcd `6181, 6180, 6179`, you can correct the configuration info based on your own environment
setting. The last thing is that the `micro` for all of our zero instances are `zero-istio`:

**vertx-etcd3.yml**

```yaml
etcd:
  micro: zero-istio
  nodes:
  - host: localhost
    port: 6181
  - host: localhost
    port: 6180
  - host: localhost
    port: 6179
  timeout: 2
```

## 2. Api Gateway \( up-athena \)

### 2.1. Configuration

The first thing is that to configure api gateway, the whole `src/main/resources` folder structure is as following:

![](/doc/image/d10084-1.png)

Api gateway will scan etcd service center to see the registered services here,

**vertx.yml**

```yaml
zero:
  lime: etcd3
  vertx:
    instance:
    - name: zero-micro
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

**vertx-server.yml**

```yaml
server:
- name: up-athena
  type: api
  config:
    port: 6100
    host: 0.0.0.0
```

> Be careful of the `type` property in vertx-server.yml, this property in api gateway must be `api` instead of `http`.
> When your zero instance will run as Api Gateway, you must set unique `api` configuration in `vertx-server.yml`
> up.god.file.

### 2.2. Console

When you start zero api gateway, you should see following output message.

```shell
......
[ ZERO ] ( node = server, type = API ) filtered configuration port set = [6100].
......
[ ZERO ] ( Etcd Center ) Etcd configuration center is enabled !
[ ZERO ] ( node = server, type = API ) filtered configuration port set = [6100].
[ ZERO ] ( Etcd Center: zero-istio ) Etcd Client timeout = "2s" with nodes = 3
[ ZERO ] ( Etcd Center ) Etcd network checking has been passed successfully !
Setting up Etcd4j Netty client
[ ZERO ] The raw data ( node = circuit, type = Circuit ) before validation is {}.
......
[ ZERO ] ( Api Gateway ) ZeroApiAgent has been started successfully. Endpoint: http://0.0.0.0:6100/.
......
```

Then you can wait for some seconds, there should be some additional logs output as following every 3 seconds.

```shell
vert.x-worker-thread-2 ...... \
    [ ZERO ] ( Discovery ) Records ( added = 0, updated = 5, deleted = 0 ) have been refreshed! 
vert.x-worker-thread-3 ...... \
    [ ZERO ] ( Discovery ) Records ( added = 0, updated = 5, deleted = 0 ) have been refreshed!
```

In zero api gateway there are two threads scanned etcd service center every 2 seconds, when you start new service and
registry into etcd service center, these two threads will scan the added new threads and put into service discovery in
api gateway. In this kind of situation you can implement service up/down at any time.

## 3. Common \( up-atlas \)

### 3.1. Configuration

In the example we also started a common service, this service does not expose `Rpc` port but it will use `RpcClient`
instead.

![](/doc/image/d10084-2.png)

**vertx.yml**

```yaml
zero:
  lime: etcd3, rpc
  vertx:
    instance:
    - name: zero-micro
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

**vertx-server.yml**

```yaml
server:
- name: up-atlas
  type: http
  config:
    port: 6201
    host: 0.0.0.0
```

### 3.2. Rpc Client

There are some new configuration that should be mentioned is that if you want to use `RpcClient` to call remote rpc
services, you must do some additional configuration.

> From vert.x 3.5.1, the api of Rpc `useSsl` has been changed and Rpc Server could use `HttpServerOptions` instead of
> the old configuration, be careful.

**vertx-rpc.yml**

```yaml
rpc:
  ssl: false
  uniform:
    type: PEM
```

**vertx-inject.yml**

```yaml
rpc: io.vertx.mod.plugin.rpc.RpcInfix
```

Above Rpc configuration is required.

### 3.3. Console

When you started this service, you should see more logs output that you haven't seen before:

```shell
......
[ ZERO ] ( Etcd Center ) Etcd configuration center is enabled !
[ ZERO ] ( Etcd Center: zero-istio ) Etcd Client timeout = "2s" with nodes = 3
[ ZERO ] ( Etcd Center ) Etcd network checking has been passed successfully !
Setting up Etcd4j Netty client
HV000001: Hibernate Validator 6.0.7.Final
......
[ ZERO ] ---> ZERO://MICRO/REGISTRY/START ( Http Server ) ZeroHttpAgent ( name = up-atlas ) is sending data to internal address.
[ ZERO ] ( Etcd Center ) The status up-atlas of service RUNNING has been registried to /zero/zero-istio/endpoint/services/up-atlas:10.0.0.7:6201
[ ZERO ] ( Etcd Catalog ) The following routes has been push to:
    [ Up Micro ] <Application Name> = "zero-istio",
    [ Up Micro ] Configuration Path = /zero/zero-istio/endpoint/routes/up-atlas:10.0.0.7:6201, 
    [ Up Micro ] Service Name = up-atlas,
    [ Up Micro ] EndPoint = http://10.0.0.7:6201
    [ Up Micro ] Route Uris = 
    [ Up Micro ]     
    [ Up Micro ] √ Successfully to registered Routes, wait for discovery......SUCCESS √
[ ZERO ] ZERO://MICRO/REGISTRY/START <--- ( Micro Worker ) ZeroHttpRegistry ( name = up-atlas ) get data from internal address.
```

From above logs we could see the configuration path on `etcd` .

## 4. Service Node

The last configuration is service node configuration: `up-coeus, up-crius, up-cronus, up-epimetheus, up-hecate` , except
service name, port, other configuration is the same.

### 4.1. Configuration

Here we only list the project `up-crius` , the folder structure of this project is as following:

![](/doc/image/d10084-4.png)

**vertx.yml**

```yaml
zero:
  lime: etcd3, rpc
  vertx:
    instance:
    - name: zero-micro
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

**vertx-rpc.yml**

```yaml
rpc:
  ssl: false
  uniform:
    type: PEM
```

**vertx-server.yml**

```yaml
server:
- name: up-crius
  type: http
  config:
    port: 6402
    host: 0.0.0.0
- name: ipc-crius
  type: ipc
  config:
    port: 6412
    host: 0.0.0.0
```

### 4.2. Console

Then when you start this node here, you should see following message:

```shell
......
[ ZERO ] ( Etcd Center ) Etcd configuration center is enabled !
[ ZERO ] ( Etcd Center: zero-istio ) Etcd Client timeout = "2s" with nodes = 3
[ ZERO ] ( Etcd Center ) Etcd network checking has been passed successfully !
Setting up Etcd4j Netty client
......

# Http Endpoint part, it's the same as Common
[ ZERO ] ---> ZERO://MICRO/REGISTRY/START ( Http Server ) ZeroHttpAgent ( name = up-crius ) is sending data to internal address.
[ ZERO ] ( Etcd Center ) The status up-crius of service RUNNING has been registried to /zero/zero-istio/endpoint/services/up-crius:10.0.0.7:6402
[ ZERO ] ( Etcd Catalog ) The following routes has been push to:
    [ Up Micro ] <Application Name> = "zero-istio",
    [ Up Micro ] Configuration Path = /zero/zero-istio/endpoint/routes/up-crius:10.0.0.7:6402, 
    [ Up Micro ] Service Name = up-crius,
    [ Up Micro ] EndPoint = http://10.0.0.7:6402
    [ Up Micro ] Route Uris = 
    [ Up Micro ]     
    [ Up Micro ] √ Successfully to registered Routes, wait for discovery......SUCCESS √
[ ZERO ] ZERO://MICRO/REGISTRY/START <--- ( Micro Worker ) ZeroHttpRegistry ( name = up-crius ) get data from internal address.

# Rpc Service part
[ ZERO ] ---> ZERO://MICRO/IPC/START ( Rpc Server ) ZeroRpcAgent ( name = ipc-crius ) is sending data to internal address.
[ ZERO ] ( Etcd Center ) The status ipc-crius of service RUNNING has been registried to /zero/zero-istio/ipc/services/ipc-crius:10.0.0.7:6412
[ ZERO ] ( Etcd Catalog ) The following routes has been push to:
    [ Up Rpc   ] <Application Name> = "zero-istio",
    [ Up Rpc   ] Configuration Rpc Point = /zero/zero-istio/ipc/routes/ipc-crius:10.0.0.7:6412, 
    [ Up Rpc   ] Service Name = ipc-crius,
    [ Up Rpc   ] Ipc Channel = grpc://10.0.0.7:6412
    [ Up Rpc   ] Ipc Address = 
    [ Up Rpc   ] √ Successfully to registered IPCs, wait for community......SUCCESS √
[ ZERO ] ZERO://MICRO/IPC/START <--- ( Micro Worker ) ZeroRpcRegistry ( name = ipc-crius ) get data from internal address.
```

> You should repeat the configuration steps of 4 on `up-coeus, up-crius, up-cronus, up-epimetheus, up-hecate`

## 5. Summary

When you have finished all the configuration, your zero system micro environment has been finished, in this kind of
situation you can continue the further tutorial for all the micro service demos \( Especially for Rpc demos \).

