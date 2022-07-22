#!/usr/bin/env bash
mvn protobuf:compile
rm -rf src/main/java/io/vertx/tp/ipc/*
cp -r target/generated-sources/protobuf/java/io/vertx/tp/ipc/* src/main/java/io/vertx/tp/ipc/