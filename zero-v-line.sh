#!/usr/bin/env bash
    echo "Java"
    find . -name "*.java"|xargs cat|grep -v -e ^$ -e ^\s*\/\/.*$|wc -l
    echo "JavaScript"
    find . -name "*.js"|xargs cat|grep -v -e ^$ -e ^\s*\/\/.*$|wc -l
    echo "Json"
    find . -name "*.json"|xargs cat|grep -v -e ^$ -e ^\s*\/\/.*$|wc -l
    echo "Xml"
    find . -name "*.xml"|xargs cat|grep -v -e ^$ -e ^\s*\/\/.*$|wc -l
    echo "[Code] See above reports"