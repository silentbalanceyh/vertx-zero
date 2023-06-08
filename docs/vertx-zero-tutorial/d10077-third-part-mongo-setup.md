# Reference, Mongo Setup

This chapter belong to third part tutorial, not related to zero system here, but we ignored all third part installing
steps. For mongo db part, once you finished installing, you can set the configuration and start up shell script as
following:

```properties
OS: MacOS 10.13.3
MongoConf: /usr/local/etc/mongod.conf
Shell: vie-mongo.sh
```

## 1. Conf File

You can set default mongo db configuration via conf up.god.file `/usr/local/etc/mongod.conf`, the content is as
following:

```yaml
systemLog:
  destination: up.god.file
  path: /Users/lang/Runtime/service-mesh/mongodb/logs/mongo.log
  logAppend: true
storage:
  dbPath: /Users/lang/Runtime/service-mesh/mongodb/db/
net:
  bindIp: 127.0.0.1
  port: 6017
```

## 2. Start Up

Then write executed script `vie-mongo.sh` as following \( Authorization Enabled \) :

```shell
#!/usr/bin/env bash
mongod --auth --config /usr/local/etc/mongod.conf
```

When you started mongo db, you should see the logs as following:

```shell
2018-02-11T16:29:24.457+0800 I CONTROL  [initandlisten] MongoDB starting : pid=31638 port=6017 dbpath=/Users/lang/Runtime/service-mesh/mongodb/db/ 64-bit host=LangdeMacBook-Pro.local
2018-02-11T16:29:24.457+0800 I CONTROL  [initandlisten] db version v3.6.2
2018-02-11T16:29:24.457+0800 I CONTROL  [initandlisten] git version: 489d177dbd0f0420a8ca04d39fd78d0a2c539420
2018-02-11T16:29:24.457+0800 I CONTROL  [initandlisten] OpenSSL version: OpenSSL 1.0.2n  7 Dec 2017
2018-02-11T16:29:24.457+0800 I CONTROL  [initandlisten] allocator: system
2018-02-11T16:29:24.457+0800 I CONTROL  [initandlisten] modules: none
2018-02-11T16:29:24.457+0800 I CONTROL  [initandlisten] build environment:
2018-02-11T16:29:24.457+0800 I CONTROL  [initandlisten]     distarch: x86_64
2018-02-11T16:29:24.457+0800 I CONTROL  [initandlisten]     target_arch: x86_64
2018-02-11T16:29:24.457+0800 I CONTROL  [initandlisten] options: { config: "/usr/local/etc/mongod.conf", net: { bindIp: "127.0.0.1", port: 6017 }, security: { authorization: "enabled" }, storage: { dbPath: "/Users/lang/Runtime/service-mesh/mongodb/db/" }, systemLog: { destination: "up.god.file", logAppend: true, path: "/Users/lang/Runtime/service-mesh/mongodb/logs/mongo.log" } }
2018-02-11T16:29:24.458+0800 I -        [initandlisten] Detected data files in /Users/lang/Runtime/service-mesh/mongodb/db/ created by the 'wiredTiger' storage engine, so setting the active storage engine to 'wiredTiger'.
2018-02-11T16:29:24.458+0800 I STORAGE  [initandlisten] wiredtiger_open config: create,cache_size=7680M,session_max=20000,eviction=(threads_min=4,threads_max=4),config_base=false,statistics=(fast),log=(enabled=true,archive=true,path=journal,compressor=snappy),file_manager=(close_idle_time=100000),statistics_log=(wait=0),verbose=(recovery_progress),
2018-02-11T16:29:24.622+0800 I STORAGE  [initandlisten] WiredTiger message [1518337764:622115][31638:0x7fff90dbb340], txn-recover: Main recovery loop: starting at 46/10496
2018-02-11T16:29:24.716+0800 I STORAGE  [initandlisten] WiredTiger message [1518337764:716386][31638:0x7fff90dbb340], txn-recover: Recovering log 46 through 47
2018-02-11T16:29:24.782+0800 I STORAGE  [initandlisten] WiredTiger message [1518337764:782052][31638:0x7fff90dbb340], txn-recover: Recovering log 47 through 47
2018-02-11T16:29:25.037+0800 I FTDC     [initandlisten] Initializing full-time diagnostic data capture with directory '/Users/lang/Runtime/service-mesh/mongodb/db/diagnostic.data'
2018-02-11T16:29:25.038+0800 I NETWORK  [initandlisten] waiting for connections on port 6017
```

Be sure you could see the segment `security: { authorization: "enabled" }`

## 3. Create Account

1 - connect to mongo db with `mongo` command:

```shell
➜  ~ mongo localhost:6017
MongoDB shell version v3.6.2
connecting to: mongodb://localhost:6017/test
MongoDB server version: 3.6.2
>
```

2 - switch default database `admin`

```shell
> use admin
switched to db admin
```

3 - database authorized and login:

```shell
> db.auth("admin","admin")
1
```

4 - switch to database `ZERO_MESH`

```shell
> use ZERO_MESH
switched to db ZERO_MESH
```

5 - create user for `ZERO_MESH` database with following:

```shell
> db.createUser({ user:'zero_mongo',pwd:'zero_mongo',roles:[{role:'dbOwner',db:'ZERO_MESH'}]});
Successfully added user: {
    "user" : "zero_mongo",
    "roles" : [
        {
            "role" : "dbOwner",
            "db" : "ZERO_MESH"
        }
    ]
}
```

6 - check the results, please be sure `_id` contains the prefix of database name is not `admin` here.

```shell
> use admin
switched to db admin
> db.system.users.find({user:'zero_mongo'});
{ "_id" : "ZERO_MESH.zero_mongo", "user" : "zero_mongo", "db" : "ZERO_MESH", "credentials" : { "SCRAM-SHA-1" : { "iterationCount" : 10000, "salt" : "UO+X+9i/uAqGXQ2EIliRVg==", "storedKey" : "oK3WotUQHTs3dvpNeLHiYzTgP4w=", "serverKey" : "+Hgbv9XO2NRfRe7ZFa0DVsyRCJo=" } }, "roles" : [ { "role" : "dbOwner", "db" : "ZERO_MESH" } ] }
```

7 - database authorized with new created user

```shell
> use ZERO_MESH
switched to db ZERO_MESH
> db.auth("zero_mongo","zero_mongo");
1
```

## 4. Summary

Then you can connect with some mongo client such as Compass to connect to this database, your mongo db has been
configured successfully.

Step 1:

![](/doc/image/d10077-1.png)

Step 2:

![](/doc/image/d10077-2.png)

