#!/usr/bin/env bash
cd vertx-ifx
mvn clean package install -DskipTests=true -Dmaven.compile.fork=true -T 1C
cd ..