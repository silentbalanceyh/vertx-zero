#!/usr/bin/env bash
mvn install -DskipTests=true
mvn liquibase:update
echo "数据库初始化完成！"
