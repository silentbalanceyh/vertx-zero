#!/usr/bin/env bash
/usr/local/mysql/bin/mysql -u root -P 3306 -h 127.0.0.1 -p < category-reinit.sql
echo "[OX] 待确认删除完成!";
