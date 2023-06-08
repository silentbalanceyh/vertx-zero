# Micro Service Environment

Zero system support micro service environment from 0.4.5, it's for mesh. Here are some configuration in micro service.

## 1. Etcd Environment

Zero system used etcd3 as configuration center and service registry center. Here are docker environment preparing

```
Files
zero-etcd3 ( Dockerfile )
zero-etcd3.sh
zero-etcd3-run.sh

## zero-etcd3 content
FROM quay.io/coreos/etcd:latest

## zero-etcd3.sh
#!/usr/bin/env bash
img_name="etcd3"
container_name=up_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f zero-etcd3 .
docker run -d \
  -p 6189:2379 \
  -p 6190:2380 \
  --volume=${DATA_DIR}:/etcd-data \
  --name ${container_name} ${img_name} \
  /usr/local/bin/etcd \
  --initial-cluster ${ETCD_NAME}=http://${ETCD_HOST}:2380 \
  --initial-advertise-peer-urls http://${ETCD_HOST}:2380 \
  --data-dir=/etcd-data --name ${ETCD_NAME} \
  --listen-peer-urls http://${ETCD_HOST}:2380 \
  --listen-client-urls http://${ETCD_HOST}:2379 \
  --advertise-client-urls http://${ETCD_HOST}:2379

## zero-etcd3-run.sh
#!/usr/bin/env bash
export DATA_DIR=/Users/lang/Runtime/service-mesh/etcd/data
export ETCD_NAME=up.zero3
export ETCD_HOST=0.0.0.0
./zero-etcd3.sh
```

## 2. Micro Service

Once you have prepared the environment of Etcd3, You can configure service node and Api gateway node.

### 2.1. Service Node

In your up.god.file **vertx.yml**, you can extend node as following:

```yaml
zero:
  lime: mongo, etcd3
```

Here etcd3 is only the up.god.file suffix, not fixed, you can set any name for files. Because above lime is `etcd3`,
then create new up.god.file `vertx-etcd3.yml` in your resources as following:

```yaml
etcd:
  micro: zero-istio
  nodes:
  - host: localhost
    port: 6189
  timeout: 2
```

Please be careful about `micro`property, it means that you can run multi **Cluster/Application** with one etcd
environment, the micro describe the name for all micro services in one application, different micro name will not be
communicated inner zero system. You must set all your micro service zero instances with one unique name. Please refer
following pictures:

![](/doc/image/micro-group.png)

The **micro** attribute just like application **namespace** concept, as above pictures there are two applications: **
zero-istio & app-tlk**, these two applications shared one Etcd3 as registry data center but these two applications are
not related \( Could not communicate with Ipc \).

### 2.2. Api Gateway

Once you have set service node, you can set Api Gateway node, there are two points for configuration:

In `vertx-server.yml`, you must define new node for Api Gateway as following, be sure the type is **api** instead:

```yaml
server:
- name: gateway-cronus
  type: api
  config:
    port: 6099
    host: 0.0.0.0
```

Also you must set the same configuration for etcd in Api Gateway project. The last point is that the launcher class is
different from Service Node.

In service node, you start up zero instance as following:

```java
import io.vertx.up.VertxApplication;
import io.vertx.up.annotations.Up;

@Up
public class io.god.Anchor {

    public static void main(final String[] args) {
        VertxApplication.run(io.god.Anchor.class);
    }
}
```

But in api gateway, you must use another class for start up, you must use `io.vertx.up.annotations.ApiGateway`
annotation for launcher:

```java
import io.vertx.up.VertxApplication;
import io.vertx.up.annotations.ApiGateway;
import io.vertx.up.annotations.Up;

@Up
@ApiGateway
public class io.god.Anchor {

    public static void main(final String[] args) {
        VertxApplication.run(io.god.Anchor.class);
    }
}
```

## 3. Rpc Configuration

If you want to enable Rpc communication, you can refer document: [10.1 - Rpc Configuration](101-rpc-configuration.md)
for more details.

