#!/usr/bin/env bash
echo "0. 环境预处理！！"
source .env.development
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
chmod +x development/*
echo "2.2. 开发环境配置完成！！"
# 根据环境拷贝环境脚本（全套）
cp -rf development/vertx-zero-cloud/environment/$ZA_ENV/* deployment/
chmod +x deployment/*/*
echo "2.3. 部署环境配置完成！！"
# 根据语言全拷贝
cp -rf development/vertx-zero-cloud/platform/$ZA_LANG/kidd/* kidd/
chmod +x kidd/*
cp -rf development/vertx-zero-cloud/platform/$ZA_LANG/kinect/* kinect/
chmod +x kinect/*
cp -rf development/vertx-zero-cloud/platform/$ZA_LANG/kzero/* kzero/
chmod +x kzero/*
echo "2.4. 运行环境配置完成！！"
# 删除
rm -rf development/vertx-zero-cloud
echo "2.5. 清理冗余配置！！"
# 替换环境变量
function read_dir(){
  for file in ` ls $1 `
  do
    if [ -d $1"/"$file ]
    then
      read_dir $1"/"$file
    else
      fileName=$1"/"$file
      extension=${fileName##*.}
      # .yml
      # .sh
      if [ "yml" == $extension -o "yaml" == $extension ]
      then
         fileOld="${fileName}.lock"
         echo "处理文件：$fileName"
         envsubst < $fileName > $fileOld
         mv $fileOld $fileName
      fi
    fi
  done
}
read_dir .
echo "2.6. 环境变量处理完成！！"
echo "应用环境初始化完成！！"
