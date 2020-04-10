#!/usr/bin/env bash
img_name="neo4j"
container_name=up_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f zero-neo4j .
docker run \
  -d -p 6074:7474 \
  -d -p 6073:7473 \
  -d -p 6075:7687 \
  --name ${container_name} ${img_name}


