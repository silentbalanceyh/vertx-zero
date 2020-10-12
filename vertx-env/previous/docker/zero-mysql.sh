#!/usr/bin/env bash
img_name="mysql"
container_name=up_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f zero-mysql .
docker run \
  -d -e MYSQL_ROOT_PASSWORD=zero \
  -d -p 6036:3306 \
  --name ${container_name} ${img_name} \
  --character-set-server=utf8 \
  --collation-server=utf8_general_ci
