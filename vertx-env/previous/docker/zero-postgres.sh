#!/usr/bin/env bash
img_name="pgsql"
container_name=up_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f zero-postgres .
docker run \
  -d -e POSTGRES_PASSWORD=zero \
  -d -p 6432:5432 \
  --name ${container_name} ${img_name}
