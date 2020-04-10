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
