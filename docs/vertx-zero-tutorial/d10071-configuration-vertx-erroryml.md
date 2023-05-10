# Configuration, vertx-error.yml

In zero system, it provide standard error system and extended error configuration, the up.god.file `vertx-error.yml`
could let you to set the errors that you want to defined.

## 1. Content Segment

```yaml
# Core configuration Error
E30001: Vert.x zero server config is missing in current data "{0}"
# Configuration validation Error
E10001: (V) - This rule require all elements of array is JsonObject, Now the index = {0} does not match, it''s {1}
E10002: (V) - The data object {0} missed required field "{1}"
E10003: (V) - The field {0} value is "{1}", but expected type is "{2}"
E10004: (V) - The cluster options is enabled, but the "{0}" vertx instance = "{1}"
E10005: (V) - The dynamic key "{0}" missing in uniform extension configuration data {1}
E10006: (V) - The data object {0} contains unsupported/forbidden field "{1}"
# .........
```

All above error codes will be mapped to zero system internally, we'll introduce the error codes in forward tutorials.

## 2. Abstract Exception

In zero system, the most used abstract abstract exception are following:

* `io.horizon.exception.WebException`
* `io.horizon.exception.UpException`

The `WebException` controlled the web request flow exceptions, and the `UpException` controlled the zero start up
exceptions, they are all runtime, zero system does not throw out these exceptions except some critical issue happened,
all the error response came from `WebException` and could provide normalized response to client.

## 3. Error Codes

Here are the error code area that we designed:

* `-10001 ~ -19999`: The configuration data validation such as required config key, config key data type, data format
  etc.
* `-20001 ~ -29999`: The third part errors such as Qiy, QQ, Wechat etc.
* `-30001 ~ -39999`: Critical system error, these exceptions may be `WebException` or `UpException`, they are all
  internally.
* `-40001 ~ -49999`: All the `UpException` sub exceptions that may impact zero system starting up.
* `-50001 ~ -59999`: \( Reserved \) All the `Rx` mode exceptions, it's used in future plan.
* `-60001 ~ -69999`: All the `WebException` that are provided by zero system internally, it's defined some standard web
  request exceptions in zero system.

If you want to define zero system exceptions that you wanted, you should set the error code start with `-100001`, if you
overwrite the error code of system internally, it may be bad things for you do to debugging.

## 4. Summary

This chapter described some specific error definition rules in zero system, in forward tutorials we'll introduce how to
define error in zero system then you could reply to client the normalized error response with correct Http Status Code.

