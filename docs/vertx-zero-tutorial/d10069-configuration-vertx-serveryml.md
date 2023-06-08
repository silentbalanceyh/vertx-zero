# Configuration, vertx-server.yml

`server` in `lime` node is extend by default, if your did not provide `vertx-server.yml`, zero system will use the
default configuration as following:

```yaml
server:
- name: zero-ws
  type: http
  config:
    port: 6083
    host: 0.0.0.0
```

## 1. name

The `name` node described the service name in current zero instance, this instance will be mapped to prefix of
configuration path as following:

![](/doc/image/d10069-1.png)

Here you could see the `key` column, the prefix `up-hecate, up-hyperion, up-lapetus` is the name of current
configuration. In micro mode, we recommended you to set the name of each node, when the node use vert.x cluster, each
name could contain different suffix of `host:port` configuration information.

## 2. type

Here the type described supported servers in zero system, current version it supports following values:

* **http**: Http Server
* **sock**: Websock Server
* **rx**: Rx Http Server **\( In development Progress, design finished only \)**
* **ipc**: Rpc Server
* **api**: Api Gateway \( Micro mode only \)

Each zero instance support one instance of each type, if you defined new `http` server, your configuration will
overwrite the default.

## 3. config

This config node contains the options of each server type, the option will be mapped to native vert.x
class: `io.vertx.core.http.HttpServerOptions` , when you set each server type, the `port` and `host` keys are **
required** under config node.

## 4. Summary

This chapter we focus on `vertx-server.yml` to set configuration of each zero instance, you should know how to configure
the server in zero.

