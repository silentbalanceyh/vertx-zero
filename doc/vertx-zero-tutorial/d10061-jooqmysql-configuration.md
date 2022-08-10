# D10061 - Jooq/Mysql, Configuration

From this chapter we started to introduce Jooq in zero system, move to `up-thea` demo project for Mysql, because you
start this tutorial, you should prepare the environment:

* Be sure you have installed local database MySQL 5.7
* Modify the mysql database user `root` password to `root`

Then you can pull the source codes from zero github, when you enter to `up-thea` project, you should see the folder
structure under `tool` as following:

![](/doc/image/D10061-1.png)

This folder contains two parts:

1. **jooq**: This folder contains jooq generation tool include xml configuration, jar dependency and generation shell
2. **sql**: This folder contains database sql script and database building shell.

## 1. Environment Preparing

You can see the content of `sql/build-db.sh` as following:

```shell
#!/usr/bin/env bash
mysql -uroot -proot < generator-table.sql
mysql -uroot -proot < generator-data.sql
echo '[ZERO] Database has been initialized successfully.'
```

Here are two sql scripts and you can execute them with `root/root`. Here we'll ignore the `generator-data.sql` and focus
on `generator-table.sql` only, the script could help us to create demo table structure, this table will be used in all
the demos in current tutorials. The script content is as following:

```sql
CREATE DATABASE IF NOT EXISTS DB_ZERO
  DEFAULT CHARSET utf8mb4
  COLLATE utf8mb4_bin;
USE DB_ZERO;
SET NAMES 'UTF8';
-- ----------------------------
-- Table Purging
-- ----------------------------
DROP TABLE IF EXISTS `SYS_TABULAR`;
CREATE TABLE `SYS_TABULAR` (
  `PK_ID`         BIGINT(20)                      NOT NULL AUTO_INCREMENT
  COMMENT 'uniqueId,PK_ID',
  `T_COMMENT`     TEXT COLLATE utf8mb4_bin COMMENT 'comment,T_COMMENT',
  `S_NAME`        VARCHAR(64) COLLATE utf8mb4_bin NOT NULL
  COMMENT 'name,S_NAME',
  `S_CODE`        VARCHAR(36) COLLATE utf8mb4_bin          DEFAULT NULL
  COMMENT 'code,S_CODE',
  `S_SERIAL`      VARCHAR(64) COLLATE utf8mb4_bin          DEFAULT NULL
  COMMENT 'serial,S_SERIAL',
  `S_TYPE`        VARCHAR(32) COLLATE utf8mb4_bin NOT NULL
  COMMENT 'The tabular comments,type,S_TYPE',
  `J_CONFIG`      TEXT COLLATE utf8mb4_bin COMMENT 'config,J_CONFIG',
  `I_ORDER`       INT(11)                         NOT NULL
  COMMENT 'order,I_ORDER',
  `IS_ACTIVE`     TINYINT(1)                               DEFAULT NULL
  COMMENT 'active,IS_ACTIVE',
  `Z_SIGMA`       VARCHAR(32) COLLATE utf8mb4_bin          DEFAULT NULL
  COMMENT 'sigma,Z_SIGMA',
  `Z_LANGUAGE`    VARCHAR(8) COLLATE utf8mb4_bin           DEFAULT NULL
  COMMENT 'language,Z_LANGUAGE',
  `Z_CREATE_BY`   VARCHAR(36) COLLATE utf8mb4_bin          DEFAULT NULL
  COMMENT 'createBy,Z_CREATE_BY',
  `Z_CREATE_TIME` DATETIME                                 DEFAULT NULL
  COMMENT 'createTime,Z_CREATE_TIME',
  `Z_UPDATE_BY`   VARCHAR(36) COLLATE utf8mb4_bin          DEFAULT NULL
  COMMENT 'updateBy,Z_UPDATE_BY',
  `Z_UPDATE_TIME` DATETIME                                 DEFAULT NULL
  COMMENT 'updateTime,Z_UPDATE_TIME',
  PRIMARY KEY (`PK_ID`),
  UNIQUE KEY `UK_SYS_TABULAR_S_CODE_S_TYPE_Z_SIGMA_Z_LANGUAGE` (`S_CODE`, `S_TYPE`, `Z_SIGMA`, `Z_LANGUAGE`)
)
  ENGINE = InnoDB
  AUTO_INCREMENT = 169
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin;
```

## 2. Jooq Configuration

Once you have prepared the sql database environment, you can move to jooq configuration part, first you can check xml
configuration:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <!-- Configure the database connection here -->
    <jdbc>
        <driver>com.mysql.cj.jdbc.Driver</driver>
        <url>
            <![CDATA[ jdbc:mysql://127.0.0.1:3306/DB_ZERO?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&useSSL=false&allowPublicKeyRetrieval=true ]]>
        </url>
        <username>htl</username>
        <password>pl,okmijn123</password>
    </jdbc>
    <generator>
        <name>io.github.jklingsporn.vertx.jooq.generate.future.FutureVertxGenerator</name>
        <database>
            <!--force generating id'sfor everything in public schema, that has an 'id' field-->
            <syntheticPrimaryKeys>public\..*\.id</syntheticPrimaryKeys>
            <!--name for fake primary key-->
            <overridePrimaryKeys>override_primmary_key</overridePrimaryKeys>
            <name>org.jooq.util.mysql.MySQLDatabase</name>
            <includes>(^SYS.*)</includes>
            <inputSchema>DB_ZERO</inputSchema>
            <unsignedTypes>false</unsignedTypes>
            <forcedTypes>
                <!-- Convert tinyint to boolean -->
                <forcedType>
                    <name>BOOLEAN</name>
                    <types>(?i:TINYINT)</types>
                </forcedType>
                <!-- Convert varchar column with name 'someJsonObject' to a io.vertx.core.json.JsonObject-->
                <forcedType>
                    <userType>io.vertx.core.json.JsonObject</userType>
                    <converter>io.github.jklingsporn.vertx.jooq.shared.JsonObjectConverter
                    </converter>
                    <expression>ZeroJsonObject</expression>
                    <types>.*</types>
                </forcedType>
                <!-- Convert varchar column with name 'someJsonArray' to a io.vertx.core.json.JsonArray-->
                <forcedType>
                    <userType>io.vertx.core.json.JsonArray</userType>
                    <converter>io.github.jklingsporn.vertx.jooq.shared.JsonArrayConverter
                    </converter>
                    <expression>ZeroJsonArray</expression>
                    <types>.*</types>
                </forcedType>
            </forcedTypes>
        </database>
        <generate>
            <daos>true</daos>
            <pojos>true</pojos>
            <javaTimeTypes>true</javaTimeTypes>
            <interfaces>true</interfaces>
            <fluentSetters>true</fluentSetters>
        </generate>
        <target>
            <packageName>up.god.domain</packageName>
            <directory>../../src/main/java</directory>
        </target>
        <strategy>
            <name>io.github.jklingsporn.vertx.jooq.generate.future.FutureGeneratorStrategy
            </name>
        </strategy>
    </generator>
</configuration>
```

All above content is jooq configuration, here you should focus on some points of this up.god.file:

1 - The driver of mysql that will be used in your application, because we used the mysql driver version more than 8.x,
it means that you should use `com.mysql.cj.jdbc.Drvier`:

```xml
<driver>com.mysql.cj.jdbc.Driver</driver>
```

2 - The url of mysql should be put into `<url>` xml node as following, because you may use some parameters of mysql in
url, you should use `<![CDATA[ ]]>` to wrapper your content.

```xml
<url>
        <![CDATA[ ...... ]]>
</url>
```

3 - Then you can set your `username`  and `password` in your configuration up.god.file:

```xml
        <username>htl</username>
        <password>pl,okmijn123</password>
```

4 - You can set your table scanning policy as following:

```xml
            <includes>(^SYS.*)</includes>
            <inputSchema>DB_ZERO</inputSchema>
```

* **inputSchema**: the database name that you used in the demo, here are `DB_ZERO`.
* **includes**: The pattern for scanning the database tables, here the pattern means _The table name start with SYS, our
  table name is _`SYS_TABULR`_. _

5 - The last part is pojo configuration as following:

```xml
<packageName>up.god.domain</packageName>
<directory>../../src/main/java</directory>
```

The configuration for java class package name `up.god.domain` and the java up.god.file output
folder `../../src/main/java`, in zero system you can keep other xml configuration part because it's normalized by our
real projects. Once you have configured the jooq xml configuration, you can run shell as following:

```
#!/usr/bin/env bash
java -classpath jooq-3.10.3.jar:jooq-meta-3.10.3.jar:jooq-codegen-3.10.3.jar:\
    mysql-connector-java-8.0.8-dmr.jar:vertx-jooq-shared-2.4.1.jar:\
    vertx-jooq-generate-2.4.1.jar:vertx-jooq-future-2.4.1.jar \
    org.jooq.util.GenerationTool ./config/generator.xml
```

After the shell executed, you can see the generated java source up.god.file under `src/main/java/` .

## 3. Jooq Application Configuration

The last part of current tutorial is application configuration in zero system, the configuration files all listed here
are as following:

```
src/main/resources/vertx.yml
src/main/resources/vertx-inject.yml
src/main/resources/vertx-jooq.yml
src/main/resources/vertx-server.yml
```

1 - In the major configuration up.god.file `vertx.yml` you should configure following part, focus on **lime** node:

```yaml
zero:
  lime: jooq  # The core configuration of jooq, it connect vertx-jooq.yml up.god.file
  vertx:
    instance:
    - name: vertx-zeus
      options:
        # Fix block 2000 limit issue.
        maxEventLoopExecuteTime: 30000000000
```

2 - In the inject configuration up.god.file `vertx-inject.yml`, you should apply the plugin of jooq `Infix` as
following:

```yaml
jooq: io.vertx.tp.plugin.jooq.JooqInfix
```

3 - The last part is `vertx-jooq.yml`, in the major configuration you could see **lime** extension, and zero system will
connect `vertx-jooq.yml` to pick the configuration data. The content is as following:

```yaml
jooq:
  provider:
    driverClassName: "com.mysql.cj.jdbc.Driver"
    username: htl
    password: "pl,okmijn123"
    catalog: DB_ZERO
    jdbcUrl: "jdbc:mysql://127.0.0.1:3306/DB_ZERO?serverTimezone=UTC&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false&useSSL=false&allowPublicKeyRetrieval=true"
```

> Be sure the configuration of your jooq is the same as jooq xml configuration up.god.file.

## 4. Summary

If you finished above three part of configuration, you can use Jooq in zero system now. We'll focus on following part:

* How to use Jooq with Utility X tool in zero system ?
* How to do CRUD operations ?
* How to do some advanced database operations ?
* How to do advanced searching ?

_Please finish this tutorial preparing part for further tutorial of Jooq, the reason that we use Jooq is that it's more
smartly and could integrated with vert.x, you can ignore SQL statement and do the database operations with lightweight
ORM instead of some heavy or complex ORM such as Hibernate._

