#!/usr/bin/env bash
img_name="consul"
container_name=up_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f zero-consul .
docker run -d -p 6500:8500 -p 6300:8300 --name ${container_name} ${img_name}
