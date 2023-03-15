#!/usr/bin/env bash
java -classpath jooq-3.17.3.jar:jooq-meta-3.17.3.jar:jooq-codegen-3.17.3.jar:mysql-connector-java-8.0.30.jar:reactive-streams-1.0.4.jar:vertx-jooq-generate-6.5.5.jar:vertx-jooq-shared-6.5.5.jar:vertx-jooq-classic-6.5.5.jar:jakarta.xml.bind-api-4.0.0.jar:r2dbc-spi-1.0.0.RELEASE.jar:vertx-core-4.3.3.jar \
  org.jooq.codegen.GenerationTool ./config/zero-jooq.xml