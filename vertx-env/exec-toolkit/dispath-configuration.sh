#!/usr/bin/env bash
export ZERO_ROOT_MENU=vertx-env/exec-toolkit/configuration/menu
export ZERO_ROOT_MOD=vertx-env/exec-toolkit/configuration/modulat
export ZERO_REST_OOB=src/main/resources

modules=(
  "ambient"
  "atom"
  "battery"
  "erp"
  "fm"
  "graphic"
  "is"
  "jet"
  "psi"
  "rbac"
  "ui"
  "wf"
)
module_index=0;
module_count=${#modules[*]}
while [ $module_index -lt $module_count ];
do
   mod=${modules[$module_index]}
   mod_run="zero-$mod"
   echo "============== Start"
   echo "zero-$mod 执行……"
   rm -rf $ZERO_ROOT/vertx-pin/$mod_run/$ZERO_REST_OOB/plugin/$mod/oob/modulat/
   mkdir -p $ZERO_ROOT/vertx-pin/$mod_run/$ZERO_REST_OOB/plugin/$mod/oob/modulat/
   cp -rf $ZERO_ROOT/$ZERO_ROOT_MOD/$mod_run/* $ZERO_ROOT/vertx-pin/$mod_run/$ZERO_REST_OOB/plugin/$mod/oob/modulat/
   echo "zero-$mod/模块配置 拷贝完成!"
   rm -rf $ZERO_ROOT/vertx-pin/$mod_run/$ZERO_REST_OOB/plugin/$mod/oob/menu/
   mkdir -p $ZERO_ROOT/vertx-pin/$mod_run/$ZERO_REST_OOB/plugin/$mod/oob/menu/
   cp -rf $ZERO_ROOT/$ZERO_ROOT_MENU/$mod_run/* $ZERO_ROOT/vertx-pin/$mod_run/$ZERO_REST_OOB/plugin/$mod/oob/menu/
   aj jmod -p $ZERO_ROOT/vertx-pin/$mod_run/$ZERO_REST_OOB/plugin/$mod/oob
   echo "zero-$mod/菜单配置 拷贝完成!"
   echo "============== Finished!"
   echo ""
   module_index=`expr $module_index + 1`
done
