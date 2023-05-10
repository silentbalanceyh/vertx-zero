# Configuration, vertx-etcd3.yml

When zero system is running in micro service mode, you should configure Registry Center \( Etcd3 in zero \), you can
refer current tutorials to check etcd3 configuration.

For etcd3 configuration, you can refer following three links

* etcd3: [https://github.com/coreos/etcd](https://github.com/coreos/etcd)
* e3w: \( Recommend Standalone \) [https://github.com/soyking/e3w](https://github.com/soyking/e3w)
* etcd-viewer: [https://github.com/nikfoundas/etcd-viewer](https://github.com/nikfoundas/etcd-viewer)

Above three etcd github address could help you to build etcd environment, we'll introduce some more details in forward
tutorials as well, this chapter we focus on zero etcd configuration only.

> Etcd configuration is enabled in micro service only.

## 1. Configuration

### 1.1. vertx.yml

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

### 1.2. vertx-etcd3.yml

Because etcd3 act as Service Registry Center only and it's invisible to developers, there is no `Infix` to let you to
visit etcd directly, the second part of etcd3 configuration is as following:

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

Above configuration described `etcd` client configuration, there are three nodes `6181, 6180, 6179`, you can configure
these information by yourself. Once thing you should be careful is the `micro` key, it's the micro-service cluster name,
it looks like a configuration root for all your micro services, be sure all the micro services that belong to one
Application connect to the same `micro` etcd. Different `micro` in zero could not share the resources.

![](/doc/image/d10074-1.png)

As above screen shot show, `zero-istio` is running more than one micro services, but it's completed an application, for
other projects or other application such as `zero-hotel`, they may still run zero micro services or other applications.
All the zero micro services that belong to `zero-istio` configuration have been put under the path `/zero/zero-istio` in
etcd.

## 2. Summary

Above all are the configuration part of etcd in micro service mode, once you have configure this part, the micro service
mode will be enabled. zero system detected your application by `lime` to check whether you enabled etcd configuration.

* `etcd` enabled: Your zero app will run in micro service mode.
* `etcd` disabled: Your zero app will run in standalone.

As we known, the same roles of `etcd` could be implemented by `zookeeper, consul` , but zero system will run inner `k8s`
and `istio`, `k8s` used etcd as default, we have bind this component with `k8s` for future usage.

