#!/usr/bin/env bash
mysql -u zero -P 3306 -h ox.engine.cn < script/database/database-reinit.sql
mvn install -DskipTests=true -Dmaven.javadoc.skip=true
mvn liquibase:update
echo "数据库初始化完成！"
