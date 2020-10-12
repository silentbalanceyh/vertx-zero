#!/usr/bin/env bash
img_name="tidb"
container_name=ox_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f ox-tidb .
docker run \
  -p 4000:4000 \
  -v /Users/lang/Runtime/ox-engine/data:/tmp/tidb \
  --name ${container_name} ${img_name}