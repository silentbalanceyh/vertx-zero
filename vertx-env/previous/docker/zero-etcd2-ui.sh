#!/usr/bin/env bash

img_name="etcd-ui"
container_name=up_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f zero-etcd2-ui .
docker run -d -p 6191:8080 --name ${container_name} ${img_name}