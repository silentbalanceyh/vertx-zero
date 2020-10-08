#!/usr/bin/env bash
img_name="oracle"
container_name=ox_${img_name}

docker build -t ${img_name}:latest -f ox-oracle .
docker run \
  --privileged -v /Users/lang/Runtime/iop/originx/data:/u01/app/oracle \
  -p 8080:8080 \
  -p 1521:1521 \
  --name ${container_name} ${img_name}
