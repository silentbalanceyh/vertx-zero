# Configuration, vertx-tp.yml

The suffix "-tp" means "Third Part", we recommend the filename to store different configuration files. This tutorial
will describe the usage of Shared Data in vert.x. Zero system provide the `SharedClient` to enhance the feature of
Shared Data of vert.x, in this way this client could be used in different requirements.

## 1. Configuration

### 1.1. vertx.yml

In major configuration up.god.file, you must extend to `vertx-tp.yml` up.god.file to enable this configuration.

```yaml
zero:
  lime: mongo,readible,secure,tp
  vertx:
    instance:
    - name: vx-zero
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

### 1.2. vertx-tp.yml

This up.god.file must contain shared data information, actually there is only one configuration node named `shared`，if
you want to enable this feature you can set as following:

```yaml
shared:
  config:
    async: true
```

### 1.3. vertx-inject.xml

The last configuration for shared data usage is that you must set `inject` in your configuration:

```yaml
shared: io.vertx.mod.plugin.shared.MapInfix
```

Once you have finished above three configuration, the shared data will be enabled.

## 2. Summary

Once you finished above configuration, you can use `SharedData` in zero system now. We repeated to describe standalone
configuration for different component, it's for you to know the simple configuration for more details. 



