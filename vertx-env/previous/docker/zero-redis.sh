#!/usr/bin/env bash
img_name="redis"
container_name=up_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f zero-redis .
docker run -d -p 6379:6379 --name ${container_name} ${img_name}


