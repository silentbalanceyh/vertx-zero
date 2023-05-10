# Micro, Environment Preparing

This chapter we focus on zero system micro environment preparing tutorials. Because zero system standard environment is
k8s and k8s used Etcd from background, in this kind of situation, zero system also used Etcd as service registry center.

## 1. Etcd Docker Environment

Files: `zero-etcd3, zero-etcd3.sh, zero-etcd3-run.sh`

### 1.1. Dockerfile \( zero-etcd3 \)

Firstly you can prepare a dockerfile as following:

```dockerfile
FROM quay.io/coreos/etcd:latest
```

### 1.2. Build Image \( zero-etcd3.sh \)

Then you can build new docker image with this shell

```shell
## zero-etcd3.sh
#!/usr/bin/env bash
img_name="etcd3"
container_name=up_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f zero-etcd3 .
docker run -d \
  -p 6179:2379 \
  -p 6180:2380 \
  --volume=${DATA_DIR}:/etcd-data \
  --name ${container_name} ${img_name} \
  /usr/local/bin/etcd \
  --initial-cluster ${ETCD_NAME}=http://${ETCD_HOST}:2380 \
  --initial-advertise-peer-urls http://${ETCD_HOST}:2380 \
  --data-dir=/etcd-data --name ${ETCD_NAME} \
  --listen-peer-urls http://${ETCD_HOST}:2380 \
  --listen-client-urls http://${ETCD_HOST}:2379 \
  --advertise-client-urls http://${ETCD_HOST}:2379
```

### 1.3. Run the image

The last shell is provided to you to run the image.

```
#!/usr/bin/env bash
export DATA_DIR=/Users/lang/Runtime/service-mesh/etcd/data
export ETCD_NAME=up.zero3
export ETCD_HOST=0.0.0.0
./zero-etcd3.sh
```

Another option is that you can refer `https://github.com/soyking/e3w` to check **e3w** project, this project could help
you to run the mini etcd3 cluster in docker environment instead of single one.

## 2. Start Up

Once you have run the etcd3, you can see following output:

```shell
etcd_1  | 07:33:39 etcd2 | 2018-02-12 07:33:39.746291 I | raft: raft.node: 9aaf2895bbe4c2bf elected leader aaa18a72e501fa3c at term 92
etcd_1  | 07:33:39 etcd1 | 2018-02-12 07:33:39.746145 I | raft: raft.node: ab243b72cddf9103 elected leader aaa18a72e501fa3c at term 92
etcd_1  | 07:33:39 etcd3 | 2018-02-12 07:33:39.753086 I | etcdserver: published {Name:infra3 ClientURLs:[http://etcd:32379]} to cluster 479a0ce20b62cfb4
etcd_1  | 07:33:39 etcd3 | 2018-02-12 07:33:39.753674 I | embed: ready to serve client requests
etcd_1  | 07:33:39 etcd2 | 2018-02-12 07:33:39.754368 I | etcdserver: published {Name:infra2 ClientURLs:[http://etcd:22379]} to cluster 479a0ce20b62cfb4
etcd_1  | 07:33:39 etcd2 | 2018-02-12 07:33:39.754401 I | embed: ready to serve client requests
etcd_1  | 07:33:39 etcd1 | 2018-02-12 07:33:39.756067 I | embed: ready to serve client requests
etcd_1  | 07:33:39 etcd1 | 2018-02-12 07:33:39.756437 I | etcdserver: published {Name:infra1 ClientURLs:[http://etcd:2379]} to cluster 479a0ce20b62cfb4
etcd_1  | 07:33:39 etcd1 | 2018-02-12 07:33:39.759668 N | embed: serving insecure client requests on [::]:2379, this is strongly discouraged!
etcd_1  | 07:33:39 etcd2 | 2018-02-12 07:33:39.760191 N | embed: serving insecure client requests on [::]:22379, this is strongly discouraged!
etcd_1  | 07:33:39 etcd3 | 2018-02-12 07:33:39.761128 N | embed: serving insecure client requests on [::]:32379, this is strongly discouraged!
```

Here you can see `etcd1, etcd2, etcd3` three threads, and then you can setup etcd-viewer to check etcd3
status: [https://github.com/nikfoundas/etcd-viewer](https://github.com/nikfoundas/etcd-viewer). Once thing should be
mentioned is that when you use etcd-viewer, you should set local network ip address instead of `localhost` such as
following:

![](/doc/image/d10082-1.png)Here when you connect etcd with etcd-viewer, you should use `192.168.30.25` instead
of `localhost`, and you could see `zero-istio` here, it's demo micro apps.

## 3. Architecture

In zero system, we could configure different zero app \( contains many micro service nodes \) in one etcd, the top
configuration path is the name of zero node. You can
refer [D10074 - Configuration, vertx-etcd3.yml](d10074-configuration-vertx-etcd3yml.md) to check etcd3 configuration in
zero. Here you could see following configuration information of `vertx-etcd3.yml` :

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

Be careful about the `micro` property, it means that you can run multi **Applications** on one etcd environment. The
micro describes the name for all micro services in one application, different micro name won't be communicated
internally in zero system. You must set all your micro services of zero instance with one unique name, the whole
architecture is as following:

![](/doc/image/micro-group.png)The `micro` property just like application **namespace** concept, as above pictures there
are two applications: **zero-istio** and **app-tlk**, these two applications shared one Etcd3 instance as registry
center but these two applications are standalone, could not communicate with gRpc.

## 4. Summary

This chapter focus on micro environment preparing in zero system, you can do as tutorials explained to setup your micro
service environment.



