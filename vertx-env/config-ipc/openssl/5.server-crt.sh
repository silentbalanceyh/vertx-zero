#!/usr/bin/env bash
openssl ca -in server.csr -out server.crt -cert ca.crt -keyfile ca.key