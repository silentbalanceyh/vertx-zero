# Configuration, vertx-rpc.yml

In micro service mode, zero system has implemented simple rpc communication between different services, when you want to
connect other services, you should set the `rpc` configuration.

> Rpc Client is enabled in micro service mode only.

## 1. Configuration

### 1.1. vertx.yml

```yaml
zero:
  lime: rpc
  vertx:
    instance:
    - name: vx-zero
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

### 1.2. vertx-rpc.yml

```yaml
rpc:
  ssl: false
  uniform:
    type: PEM
  extension:
    {name}:
      type: XXX
```

Please be careful about the attributes`uniform`and`extension`it's the same except following:

* The system will scan all client configuration under extension by name setted first. For example the target name is
  `up-ceous`, you must set {name} to`up-ceous`, then when current service communicate with **up-ceous** service, the
  client configuration will use it under **extension **instead of **uniform **.
* If there is no configuration under extension, the uniform configuration will be used. uniform just like default client
  configuration.

> This configuration up.god.file is only used in **SSL **mode.

## 2. Full Examples

You can ignore the details of the example, this chapter focus on rpc client only, we'll introduce rpc server
configuration in forward.

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

## 3. Summary

Rpc client is a critical role in zero system to finish service communicating works, you should know how to configure
different roles of rpc.

* Rpc Server
* Rpc Client

Once you have configured above two parts in different micro services, these services could communicate internally
directly in zero system. You can say goodbye to old Http EndPoint communication internally.

