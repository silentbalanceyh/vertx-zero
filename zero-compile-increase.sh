#!/usr/bin/env bash
mvnd install -DskipTests=true -Dmaven.javadoc.skip=true -Dmaven.compile.fork=true -T 1C
