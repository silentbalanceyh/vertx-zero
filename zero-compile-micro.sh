#!/usr/bin/env bash
cd vertx-istio
mvnd clean package install -DskipTests=true -Dmaven.javadoc.skip=true -Dmaven.compile.fork=true -T 1C
cd ..