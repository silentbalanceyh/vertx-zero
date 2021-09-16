#!/usr/bin/env bash
openssl req -new -key server.key -out server.csr -subj "/C=CN/ST=ChongQing/L=ChongQing/O=ZeroUp/OU=zero-server/CN=zero-server"