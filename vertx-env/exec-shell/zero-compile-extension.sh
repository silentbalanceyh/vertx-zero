#!/usr/bin/env bash
cd vertx-pin
mvnd clean package install -Dquickly -DskipTests=true -Dmaven.compile.fork=true -T 1C
cd ..