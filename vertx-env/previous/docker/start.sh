#!/usr/bin/env bash

docker-compose -f docker/tidb.yml up -d
docker-compose -f docker/neo4j.yml up -d
docker-compose -f docker/elasticsearch.yml up -d
