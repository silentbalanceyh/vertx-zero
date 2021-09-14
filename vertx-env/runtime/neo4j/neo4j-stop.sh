#!/usr/bin/env bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/zulu-14.jdk/Contents/Home
export NEO4J_HOME=$PWD/server
$NEO4J_HOME/bin/neo4j stop
