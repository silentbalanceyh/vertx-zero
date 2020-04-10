#!/usr/bin/env bash
img_name="mongo"
container_name=up_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f zero-mongo .
docker run -d -p 6017:27017 --name ${container_name} ${img_name} --auth
