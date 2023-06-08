#!/usr/bin/env bash
openssl req -new -x509 -days 36500 -key ca.key -out ca.crt -subj "/C=CN/ST=ChongQing/L=ChongQing/O=ZeroUp/OU=ZeroUp"
#openssl req -new -x509 -days 36500 -key zero.key -out zero.crt -subj "/C=CN/ST=ChongQing/L=ChongQing/O=Vertx/OU=ZeroUp"