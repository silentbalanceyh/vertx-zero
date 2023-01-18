#!/usr/bin/env bash
mvn install:install-file  -Dfile=ojdbc7-12.1.0.2.jar  -DgroupId=com.oracle  -DartifactId=ojdbc7 -Dversion=12.1.0.2 -Dpackaging=jar
