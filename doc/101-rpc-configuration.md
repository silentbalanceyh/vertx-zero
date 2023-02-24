# Rpc Configuration

This document is for rpc configuration in zero system. You can configure current zero instance as two Rpc roles: **
Server/Client**, Rpc require micro service environment instead of standalone, it means that you must read this document
first: [2.3 - Micro Service Environment](/doc/23-micro-service-environment.md) .

## 1. Rpc Server

You can configure rpc server in `vertx-server.yml` directly, it's not needed to create new configuration files.

```yaml
- name: ipc-cronus
  type: ipc
  config:
    port: 6883
    host: 0.0.0.0
    ssl: false
    type: PEM
```

You can add new node of `type=ipc` IPC \( Internal Process Calling \) instead of `http`, Here are some additional
configuration info of rpc to enable SSL.

* **ssl**: Whether enable SSL communication with Rpc
* **type**: Here are three types of SSL, please refer gRpc/vertx document to see more details of the types:
    * JKS
    * PKCS12
    * PEM

## 2. Rpc Client

Rpc client configuration must be configured standalone. Here are the comments:

In your `vertx.yml` configuration up.god.file, you must extend another `lime` node as following:

```yaml
zero:
  lime: etcd3, rpc
```

Then you must create new up.god.file `vertx-rpc.yml` with following content:

```yaml
rpc:
  ssl: false
  uniform:
    type: PEM
  extension:
    {name}:
      type: XXX
```

Please be careful about the attributes `uniform` and `extension`it's the same except following:

* The system will scan all client configuration under extension by name setted first. For example the target name
  is `up-ceous`, you must set {name} to `up-ceous`, then when current service communicate with** up-ceous **service, the
  client configuration will use it under **extension** instead of **uniform**.
* If there is no configuration under extension, the uniform configuration will be used. uniform just like default client
  configuration.

This configuration up.god.file is only used in **SSL** mode.

## 3. SSL Configuraiton \( Examples \)

Here are some examples of Rpc SSL configuration.

```yaml
## vertx-server.yml
# Rpc Server with PEM ( OpenSSLEngine )
- name: ipc-cronus
  type: ipc
  config:
    port: 6883
    host: 0.0.0.0
    ssl: true
    type: PEM
    cert: tlk/server-cert.pem
    key: tlk/server-key.pem

# Rpc Server with Jsk ( JSKEngine )
- name: ipc-cronus
  type: ipc
  config:
    port: 6883
    host: 0.0.0.0
    ssl: true
    type: JKS
    jsk: tlk/server-key.jks
    password: wibble
```
```yaml
## vertx-rpc.yml ( The name depend on lime )
# Rpc Client use default
rpc:
  ssl: true
  uniform:
    type: PEM
    cert: tlk/server-cert.pem
    key: tlk/server-key.pem
```
```yaml
# Rpc Client with spec service

rpc:
  ssl: true
  uniform:
    type: PEM
    cert: tlk/server-cert.pem
    key: tlk/server-key.pem
  extension:
    up-cerous:
      type: JKS
      jsk: tlk/server-key.jks
      password: wibble
```



