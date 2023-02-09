#!/usr/bin/env bash
cd vertx-semper
mvnd clean package install -Dquickly -DskipTests=true -Dmaven.javadoc.skip=true -Dmaven.compile.fork=true -T 1C
cd ..