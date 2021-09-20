# Docker Environment

## 0. Port List

* neo4j:
    * 6074 ( http://localhost:6074, neo4j:neo4j )
    * 6073
    * 6075 ( bolt://localhost:6075, Database Links )
* consul
    * 6500 ( http://localhost:6500 )
    * 6300
* mongo
    * 6017
* mysql
    * 6036 ( root:zero )
* postgres
    * 6432 ( postgres:zero )
* rabbit-mq
    * 6369
    * 6571
    * 6572
    * 6671
    * 6672 ( http://localhost:6672, guest:guest )
    * 6272
* redis
    * 6379
* etcd2
    * 6179
    * 6180
    * 6181
* etcd3
    * 6189
    * 6190
    * 6191

## 1. Setup

### 1.1. neo4j

```
docker pull neo4j
shell/zero-neo4j.sh
```

### 1.2. consul

```
docker pull consul:1.0.1
shell/zero-consul.sh
```

### 1.3. mongo

```
docker pull mongo:3.5.13
shell/zero-mongo.sh
```

### 1.4. mysql

```
docker pull mysql:5.7
shell/zero-mysql.sh
```

### 1.5. postgres

```
docker pull postgres:latest
shell/zero-postgres.sh
```

### 1.6. rabbitmq

```
docker pull rabbitmq:management
shell/zero-rabbit-mq.sh
```

### 1.7. redis

```
docker pull redis:4.0.2
shell/zero-redis.sh
```

### 1.8. etcd ( 2 & 3 )

```
# etcd 3
docker pull quay.io/coreos/etcd:latest
shell/zero-etcd3-run.sh

# etcd 2
docker pull quay.io/coreos/etcd:v2.3.8
shell/zero-etcd2-run.sh

# etcd ui
FROM nikfoundas/etcd-viewer

```

You can modify the environment variable in `zero-etcd-run.sh` as following:

```
# etcd 2
export DATA_DIR=/Users/lang/Runtime/service-mesh/etcd/data
export ETCD_NAME=up.zero2
export ETCD_HOST=127.0.0.1
./zero-etcd2.sh

# etcd 3
#!/usr/bin/env bash
export DATA_DIR=/Users/lang/Runtime/service-mesh/etcd/data
export ETCD_NAME=up.zero3
export ETCD_HOST=127.0.0.1
./zero-etcd3.sh
```

* DATA_DIR: Data folder to store etcd data;
* ETCD_NAME: The name for current node here ( default up.zero )
* ETCD_HOST: The host to run etcd here ( default is 127.0.0.1 )