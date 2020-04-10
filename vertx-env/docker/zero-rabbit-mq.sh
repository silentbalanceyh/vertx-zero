#!/usr/bin/env bash
img_name="rabbit_mq"
container_name=up_${img_name}

docker stop ${container_name}
docker rm ${container_name}
docker rmi ${img_name}:latest

docker build -t ${img_name}:latest -f zero-rabbit-mq .
docker run -d \
 --publish 6571:5671 \
 --publish 6572:5672 \
 --publish 6369:4369 \
 --publish 6272:25672 \
 --publish 6671:15671 \
 --publish 6672:15672 \
 --name ${container_name} ${img_name}


