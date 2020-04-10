package io.vertx.tp.modular.dao;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Record;

/**
 * 内置接口：搜索器
 */
interface AoSearcher {

    /* 搜索专用接口，生成对应的 Pagination */
    Future<JsonObject> searchAsync(final JsonObject filters);

    JsonObject search(final JsonObject filters);

    /* 直接搜索读取 */
    Future<Record[]> fetchAsync(final JsonObject criteria);

    Record[] fetch(final JsonObject criteria);
}
