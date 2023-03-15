#!/usr/bin/env bash
while read line
do
    echo "即将删除文件: $line"
    rm -f $line
done < <(find ~/.m2/repository -name "*.lastUpdated")
while read line
do
    echo "即将删除文件: $line"
    rm -f $line
done < <(find ~/.m2/repository -name "*.part")

while read line
do
    echo "即将删除文件: $line"
    rm -f $line
done < <(find ~/.m2/repository -name "*.resolverlock")