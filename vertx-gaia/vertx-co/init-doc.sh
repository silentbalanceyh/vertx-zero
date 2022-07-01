#!/usr/bin/env bash
mvn install -DskipTests=true -Dmaven.compile.fork=true -T 1C
cp doclet/zero.css document/stylesheet.css