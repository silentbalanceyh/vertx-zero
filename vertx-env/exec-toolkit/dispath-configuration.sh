#!/usr/bin/env bash
export ZERO_ROOT_MENU=vertx-env/exec-toolkit/configuration/menu
export ZERO_ROOT_MOD=vertx-env/exec-toolkit/configuration/modulat
export ZERO_REST_OOB=src/main/resources

export RUN_PRO=zero-ambient
echo "zero-ambient 执行……"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/ambient/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/ambient/oob/modulat/
echo "zero-ambient/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/ambient/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/ambient/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/ambient/oob
echo "zero-ambient/菜单配置 拷贝完成!"

echo "zero-battery 执行……"
export RUN_PRO=zero-battery
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/battery/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/battery/oob/modulat/
echo "zero-battery/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/battery/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/battery/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/battery/oob
echo "zero-battery/菜单配置 拷贝完成!"

echo "zero-atom 执行……"
export RUN_PRO=zero-atom
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/atom/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/atom/oob/modulat/
echo "zero-atom/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/atom/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/atom/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/atom/oob
echo "zero-atom/菜单配置 拷贝完成!"

echo "zero-erp 执行……"
export RUN_PRO=zero-erp
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/erp/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/erp/oob/modulat/
echo "zero-erp/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/erp/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/erp/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/erp/oob
echo "zero-erp/菜单配置 拷贝完成!"

echo "zero-fm 执行……"
export RUN_PRO=zero-fm
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/fm/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/fm/oob/modulat/
echo "zero-fm/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/fm/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/fm/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/fm/oob
echo "zero-fm/菜单配置 拷贝完成!"

echo "zero-graphic 执行……"
export RUN_PRO=zero-graphic
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/graphic/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/graphic/oob/modulat/
echo "zero-graphic/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/graphic/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/graphic/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/graphic/oob
echo "zero-graphic/菜单配置 拷贝完成!"

echo "zero-is 执行……"
export RUN_PRO=zero-is
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/is/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/is/oob/modulat/
echo "zero-is/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/is/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/is/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/is/oob
echo "zero-is/菜单配置 拷贝完成!"


echo "zero-jet 执行……"
export RUN_PRO=zero-jet
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/jet/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/jet/oob/modulat/
echo "zero-jet/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/jet/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/jet/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/jet/oob
echo "zero-jet/菜单配置 拷贝完成!"

echo "zero-psi 执行……"
export RUN_PRO=zero-psi
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/psi/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/psi/oob/modulat/
echo "zero-psi/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/psi/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/psi/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/psi/oob
echo "zero-psi/菜单配置 拷贝完成!"

echo "zero-rbac 执行……"
export RUN_PRO=zero-rbac
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/rbac/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/rbac/oob/modulat/
echo "zero-rbac/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/rbac/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/rbac/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/rbac/oob
echo "zero-rbac/菜单配置 拷贝完成!"

echo "zero-ui 执行……"
export RUN_PRO=zero-ui
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/ui/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/ui/oob/modulat/
echo "zero-ui/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/ui/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/ui/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/ui/oob
echo "zero-ui/菜单配置 拷贝完成!"

echo "zero-wf 执行……"
export RUN_PRO=zero-wf
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/wf/oob/modulat/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/wf/oob/modulat/
echo "zero-wf/模块配置 拷贝完成!"
mkdir -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/wf/oob/menu/
cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$RUN_PRO/* $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/wf/oob/menu/
aj jmod -p $ZERO_ROOT/vertx-pin/$RUN_PRO/$ZERO_REST_OOB/plugin/wf/oob
echo "zero-wf/菜单配置 拷贝完成!"