#!/usr/bin/env bash
openssl req -new -key server-key.pem -out server-key.csr -subj "/C=CN/ST=ChongQing/L=ChongQing/O=Vertx/OU=ZeroUp"