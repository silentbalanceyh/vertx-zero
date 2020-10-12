#!/usr/bin/env bash
/usr/local/mysql/bin/mysql -u root -P 3306 -h 127.0.0.1 -p < ci-clean.sql
echo "[OX] 配置项删除完成!";
