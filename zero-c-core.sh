#!/usr/bin/env bash
cd vertx-gaia
mvn clean package install -DskipTests=true -Dmaven.compile.fork=true -T 1C
cd ..