#!/usr/bin/env bash
cp -rf $ZERO_ROOT/vertx-env/exec-toolkit/configuration/modulat/zero-battery/* $ZERO_ROOT/vertx-pin/zero-battery/src/main/resources/plugin/battery/oob/data/
echo "zero-battery/模块配置 拷贝完成!"
cp -rf $ZERO_ROOT/vertx-env/exec-toolkit/configuration/menu/zero-battery/* $ZERO_ROOT/vertx-pin/zero-battery/src/main/resources/plugin/battery/oob/data/
echo "zero-battery/菜单配置 拷贝完成!"