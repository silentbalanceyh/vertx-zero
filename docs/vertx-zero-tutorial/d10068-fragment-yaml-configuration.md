# Configuration, vertx.yml

Until now you have seen different configuration files appeared in zero system, this tutorial will introduce the
configuration structure in zero system. In common maven project of zero system, the structure is often as following:

```folder
src/main/java
src/main/resources     # This tutorial focus on this folder for yaml configuration
src/test/java
src/test/resources
src/main/lombok        # If lombok enabled or used
```

All the yaml configuration files will be put into `src/main/resources` folder and different config up.god.file controls
different feature.

## 1. Content

The core configuration up.god.file is `vertx.yml`, this up.god.file is the kernel up.god.file of zero system, the demo
of this up.god.file is as following:

```yaml
zero:
  lime: error, inject, server
  vertx:
    clustered:
      enabled: false
      manager:

    instance:
    - name: vx-zero
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 6000000000
```

## 2. Extension: lime

`lime` is the extension configuration, you can add any external yaml configuration up.god.file with `lime` here, for
above demo, the extension up.god.file should be `vertx-error.yml`, `vertx-inject.yml`, `vertx-server.yml` . Another
thing is that in our system the `lime` node impact filename only, it does not impact the config content. It means: _ _

> _You can put mongo configuration information into _`vertx-mysql.yml`_ , because zero system focus on content parsing
instead of filename, but we do not want you to do like this._

But we still recommend you to use meaningful filename for the content.

The default extensions `lime` of zero system are as following:

* `error`: The definition of error message in zero system.
* `server`: The server configuration of zero system, default enabled `http` server.
* `inject`: The default injection components in zero system, you can develop user-defined `Infix` and configured in zero
  system. Zero system support plug-in components, we'll introduce the plug-in development in forward tutorials.

All above `lime` nodes are default imported by zero system, though you haven't configured in `vertx.yml`, these
extension were enabled by zero system. If you provide the extension files, the default configuration will be
overwritten.

## 3. Vert.x Cluster: clustered

Under the node `zero -> vertx`, we could set vert.x cluster configuration part, the segment is as following:

```yaml
clustered:
    enabled: false
    manager: 
    options:
```

If you want to enable vert.x cluster, you can add `clustered` configuration into `vertx.yml`:

* `enabled`: Enable vert.x cluster mode;
* `manager`: Provide vert.x cluster manager name, the name is full java class name, you can
  refer [http://vertx.io/docs/\#clustering](http://vertx.io/docs/#clustering) to check the cluster manager that vert.x
  native supported.
* `options`: If you enable clustered, you can provide options to cluster manager that you'll use, if you do not provide
  this configuration, the cluster manager will use the default.

> Zero system disabled cluster mode in default.

## 4. Vert.x Instance: instance

Here one zero instance could started more than one `vert.x` instance, but we still recommend you to set one vert.x
instance in each zero node.

```yaml
    instance:
    - name: vx-zero
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 6000000000
```

This configuration is simple, the `name` will be used in `micro` mode more than `standalone` mode, when you use service
discovery/registry feature, all the configuration path in registry center \( zookeeper, etcd or consul \) will be put
under the name, we'll introduce in forward tutorials.

The `options` will be mapped to native vert.x configuration, it's JsonObject and will be converted
to `io.vertx.core.VertxOptions` class instead, it's direct serialization.

## 5. Summary

This chapter focus on the core zero system configuration up.god.file `vertx.yml` , this up.god.file contains following
configuration part:

* lime: The configuration extension.
* vertx -&gt; clustered: Vert.x cluster configuration.
* vertx -&gt; instance: \( Multi Support \) The vert.x options that will be converted to `io.vertx.core.VertxOptions`.

Once more thing is that all the other configuration came from `lime` node, the lime means:

> Linear Injection Magical Extension



