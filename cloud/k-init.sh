#!/usr/bin/env bash
mkdir -p kzero
mkdir -p database/tidb/
mkdir -p database/mysql/
declare -A assets
assets=(
  ["admin"]="系统：平台管理"
  ["modulat"]="系统：模块化"
  ["tenant"]="系统：租户管理"
  ["istio"]="系统：环境连接"
)
declare -A atoms
atoms=(
  ["workflow"]="建模：工作流"
  ["emf"]="建模：EMF"
  ["authorize"]="建模：安全"
  ["integration"]="建模：集成"
  ["graphic"]="建模：拓扑图"
  ["tpl"]="建模：模板"
)
declare -A actions
actions=(
  ["job"]="行为：任务"
  ["api"]="行为：接口"
  ["lexeme"]="行为：智能语义"
  ["rule"]="行为：规则引擎"
)
declare -A aboves
aboves=(
  ["page"]="呈现：页面"
  ["form"]="呈现：表单"
  ["list"]="呈现：列表"
  ["event"]="呈现：事件"
)
  
# 系统
mkdir -p kzero/asset
for asset in ${!assets[*]}
do
  mkdir -p kzero/asset/$asset
  echo ${assets[$asset]} > kzero/asset/$asset/README.txt
  echo "(${assets[$asset]}) initialized !"
done
# 建模
mkdir -p kzero/atom
for atom in ${!atoms[*]}
do
  mkdir -p kzero/atom/$atom
  echo ${atoms[$atom]} > kzero/atom/$atom/README.txt
  echo "(${atoms[$atom]}) initialized !"
done
# 行为
mkdir -p kzero/action
for action in ${!actions[*]}
do
  mkdir -p kzero/action/$action
  echo ${actions[$action]} > kzero/action/$action/README.txt
  echo "(${actions[$action]}) initialized !"
done
# 呈现
mkdir -p kzero/above
for above in ${!aboves[*]}
do
  mkdir -p kzero/above/$above
  echo ${aboves[$above]} > kzero/above/$above/README.txt
  echo "(${aboves[$above]}) initialized !"
done

echo "Configuration Files initialized !"

mkdir -p kidd
echo "生产环境" > kidd/README.txt
mkdir -p kinect
echo "开发环境" > kidd/README.txt
# 发布专用
mkdir -p deployment
mkdir -p development
declare -A plugins
plugins=(
  ["istio"]="Kernel Mesh: Istio K8s"
  ["mysql"]="Database: MySQL"
  ["tidb"]="Database: TiDb"
  ["neo4j"]="Database: Neo4j"
  ["redis"]="Cache: Redis"
  ["es"]="Elastic Search"
  ["elk"]="ELK Logging System"
  ["hcp"]="HCP Vault Sensitive"
  ["kafka"]="Kafka"
  ["zipkin"]="Monitor: Zipkin"
  ["pts"]="Monitor: Prometheus"
  ["kiali"]="Monitor: Kiali"
  ["cloud"]="Cloud: Zero Cloud"
)
for plugin in ${!plugins[*]}
do
  # 每个目录
  mkdir -p deployment/$plugin
  echo ${plugins[$plugin]} > deployment/$plugin/README.txt
  echo "${plugin} 插件初始化完成 !"
done