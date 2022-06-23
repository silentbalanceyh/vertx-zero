#!/usr/bin/env bash
java -classpath jooq-3.15.3.jar:jooq-meta-3.15.3.jar:jooq-codegen-3.15.3.jar:mysql-connector-java-8.0.11.jar:reactive-streams-1.0.3.jar:vertx-jooq-generate-6.3.0.jar:vertx-jooq-shared-6.3.0.jar:vertx-jooq-classic-6.3.0.jar:jaxb-api-2.4.0-b180830.0359.jar:r2dbc-spi-0.8.6.RELEASE.jar:vertx-core-4.1.4.jar \
  org.jooq.codegen.GenerationTool ./config/zero-jooq.xml