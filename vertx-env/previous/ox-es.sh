#!/usr/bin/env bash
img_name="es"
container_name=ox_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f ox-es .
docker run \
  -p 9200:9200 \
  -p 9300:9300 \
  -e "discovery.type=single-node" \
  --name ${container_name} ${img_name}


