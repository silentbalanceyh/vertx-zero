#!/usr/bin/env bash
cd vertx-gaia/vertx-ifx
mvn clean package install -DskipTests=true
cd ..
cd ..