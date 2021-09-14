#!/usr/bin/env bash
cd vertx-pin
mvn clean package install -DskipTests=true -Dmaven.compile.fork=true -T 1C
cd ..