package cn.originx.scaffold.stdn;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.robin.Switcher;

/*
 * 单条记录专用处理
 */
public interface Completer {

    default Completer bind(final Switcher switcher) {
        return this;
    }

    /*
     * 创建配置项
     */
    Future<JsonArray> create(JsonArray records);

    Future<JsonObject> create(JsonObject record);

    /*
     * 更新配置项
     */
    Future<JsonArray> update(JsonArray records);

    Future<JsonObject> update(JsonObject record);

    /*
     * 删除配置项
     */
    Future<JsonArray> remove(JsonArray records);

    Future<JsonObject> remove(JsonObject record);

    /*
     * 读取配置项
     */
    Future<JsonObject> find(JsonObject record);

    Future<JsonArray> find(JsonArray records);
}
