#!/usr/bin/env bash
mysql -u root -P 3306 -h ox.engine.cn < database-reinit.sql
echo "[OX] 重建 DB_ETERNAL 数据库成功!";