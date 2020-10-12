#!/usr/bin/env bash
cd vertx-gaia
mvn clean package install -pl vertx-co,vertx-tp,vertx-up,vertx-rx -DskipTests=true
cd ..
