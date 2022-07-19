#!/usr/bin/env bash
# --------------------- 环境变量专用区域 -----------------------
<<comment
   1. 设置核心三个环境变量
   2. ZC_MODE用于指定发布K8S的环境
      kmini:    本地Minicube开发环境（默认）
      k8s:      远程Minicube开发测试环境（单点）
      kmt:      预发布压力测试环境（单点/集群）
      ki:       远程生产标准环境（单点/集群）
      kiha:     远程HA高可用环境（单点/集群）
comment

# ZC_NS                 K8S名空间
export ZC_NS=aeon-hotel
# ZC_APP                应用APP核心配置文件路径
export ZC_APP="zapp.yml"
# ZC_MODE               五种环境选一种
export ZC_MODE="kmini"
# ZC_LANG               语言三选一
export ZC_LANG="cn"

# --------------------- TiDB
export ZC_TIDB_CLUSTER=aeon-hotel             # ZC_TIDB_CLUSTER       TiDB集群名称
export ZC_TIDB_NAME=aeon-hotel-db             # ZC_TIDB_NAME          TiDB部署到K8S的名称
                                              # ZC_TIDB_STORE         TiDB存储数据文件位置
export ZC_TIDB_STORE=/Users/lang/Develop/Source/ox-workshop/native-cloud/hotel-cloud/database/tidb/

<<comment
   初始化执行流程：
   1. 先执行 k-init.sh 处理空目录
   2. 再下载远程代码到本地
   3. 拷贝全套环境到本地（借用 ZC_MODE, ZC_LANG）
   4. 删除下载的远程代码
comment
# --------------------- 环境初始化
./k-init.sh
# --------------------- 远程脚本下载
cd development
# 下载代码
rm -rf vertx-zero-cloud
git clone https://github.com/silentbalanceyh/vertx-zero-cloud.git
rm -rf vertx-zero-cloud/.git
echo "1. 代码下载完成！！"
# App 配置拷贝当前目录
cd ..
cp -rf development/vertx-zero-cloud/apps/*.yml .
echo "2.1. 应用配置完成！！"
# 脚本目录全拷贝
cp -rf development/vertx-zero-cloud/development/*.sh development/
echo "2.2. 开发环境配置完成！！"
# 根据环境拷贝环境脚本（全套）
cp -rf development/vertx-zero-cloud/environment/$ZC_MODE/* deployment/
echo "2.3. 部署环境配置完成！！"
# 根据语言全拷贝
cp -rf development/vertx-zero-cloud/platform/$ZC_LANG/kidd/* kidd/
cp -rf development/vertx-zero-cloud/platform/$ZC_LANG/kinect/* kinect/
cp -rf development/vertx-zero-cloud/platform/$ZC_LANG/kzero/* kzero/
echo "2.4. 运行环境配置完成！！"
# 删除
rm -rf development/vertx-zero-cloud
echo "2.5. 清理冗余配置！！"

echo "应用环境初始化完成！！"
