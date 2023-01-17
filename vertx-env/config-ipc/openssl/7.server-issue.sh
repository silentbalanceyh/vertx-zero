#!/usr/bin/env bash
openssl x509 -req -days 365 -sha1 -extensions v3_req -CA zero-ca.pem -CAkey zero-ca.pem -CAserial zero.ca.srl -CAcreateserial -in server-key.csr -out server-cert.pem