# Configuration, vertx-secure.yml

From `0.4.7`, zero system support security limitation for **Authorization** and **Authentication**, if you want to
enable this feature, you should set another extension configuration for `secure` node.

## 1. Configuration

### 1.1. vertx.yml

Be sure the extension up.god.file name existing in `lime` node in the major configuration.

```yaml
zero:
  lime: secure
```

Because we'll use mongo as default authorization, you must finish mongo configuration based
on: [D10073 - Configuration, vertx-mongo.yml.](d10073-configuration-vertx-mongoyml.md)

### 1.2. vertx-secure.yml

For `MongoAuth` usage, you can set the configuration as following:

```yaml
secure:
  # Standard Type
  mongox:
    type: mongo
    config:
      collectionName: DB_USER
      saltStyle: NO_SALT
```

For Jwt authorization, you should set configuration as following:

```yaml
secure:
  # Standard Type
  jwt:
    type: jwt
    config:
      jwtOptions:
        algorithm: HS256
      keyStore:
        type: jceks
        path: keys/keystore.jceks
        password: zeroup
```

Here the `type` attribute now support `mongo`, `jwt` in zero system only, if you used `mongo` type the authorization
header should be:

```
Basic bGFuZy55dToxQkJEODg2NDYwODI3MDE1RTVENjA1RUQ0NDI1MjI1MQ==
```

But for `jwt` , you should set the header as following:

```
Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJfaWQiOiJjMWZiN2JiZC1kOTkxLTQwODItYTY3ZS0yODliYzM5NzQzNTEiLCJpYXQiOjE1MjAxMTk1Mzh9.iL1ymVq8b7vgqt6nna6vUqCPvaPT3QJpw0Fl4q4xbA4
```

## 2. Summary

Here are the jwt/mongo standalone configuration part and in forward tutorials we'll introduce how to use `secure` mode
in zero system.

