#!/usr/bin/env bash
keytool -genkeypair -alias zerocert -keyalg RSA -validity 365 -keystore zero-server.jks
