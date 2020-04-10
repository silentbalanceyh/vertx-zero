#!/usr/bin/env bash
img_name="etcd2"
container_name=up_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f zero-etcd2 .
docker run -d \
  -v /usr/share/ca-certificates/:/etc/ssl/certs \
  -p 6179:2379 \
  -p 6180:2380 \
  -p 6181:4001 \
  --name ${container_name} ${img_name} \
  --initial-cluster ${ETCD_NAME}=http://${ETCD_HOST}:2380 \
  --initial-advertise-peer-urls http://${ETCD_HOST}:2380 \
  --data-dir=/etcd-data --name ${ETCD_NAME} \
  --listen-peer-urls http://${ETCD_HOST}:2380 \
  --listen-client-urls http://${ETCD_HOST}:2379 \
  --advertise-client-urls http://${ETCD_HOST}:2379
