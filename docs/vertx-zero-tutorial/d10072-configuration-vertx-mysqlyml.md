# Configuration, vertx-mysql.yml

In zero system, if you do not use `Jooq` to access database, you can use native `SQLClient` here, all the official
client contains standard annotations, but this chapter we introduce the mysql configuration
only. [Reference](http://vertx.io/docs/vertx-mysql-postgresql-client/java/).

## 1. Configuration

### 1.1. vertx.yml

```yaml
zero:
  lime: mysql
  vertx:
    instance:
    - name: vx-zero
      options:
        maxEventLoopExecuteTime: 30000000000
```

### 1.2. vertx-inject.yml

```yaml
mysql: io.vertx.up.plugin.jdbc.MySqlInfix
```

### 1.3. vertx-mysql.yml

All the mysql configurations are put into `vertx-mysql.yml` the root node `mysql` as following:

```yaml
mysql:
  host: localhost
  port: 3306
  username: root
  password: root
  database: DB_ZERO
```

Above all keys are vert.x native configuration and it could be passed into vert.x mysql client directly, you can refer
the link to check all the valid configuration
information: [http://vertx.io/docs/vertx-mysql-postgresql-client/java/\#\_configuration](http://vertx.io/docs/vertx-mysql-postgresql-client/java/#_configuration)

## 2. Summary

Here are the mysql standalone configuration part and in forward tutorials we'll introduce the client usage in zero
system. The limitation here is that current version one zero system could support one mysql database only, you could not
connect to multi database with zero system at the same time.

