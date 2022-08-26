#!/usr/bin/env bash
java -classpath jooq-3.16.7.jar:jooq-meta-3.16.7.jar:jooq-codegen-3.16.7.jar:mysql-connector-java-8.0.11.jar:reactive-streams-1.0.4.jar:vertx-jooq-generate-6.5.3.jar:vertx-jooq-shared-6.5.3.jar:vertx-jooq-classic-6.5.3.jar:jakarta.xml.bind-api-4.0.0.jar:r2dbc-spi-1.0.0.RELEASE.jar:vertx-core-4.3.1.jar \
  org.jooq.codegen.GenerationTool ./config/zero-jooq.xml