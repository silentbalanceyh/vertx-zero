package io.mature.extension.uca.commerce;

import io.horizon.spi.robin.Switcher;
import io.modello.specification.action.HDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.modeling.builtin.DataAtom;
import io.vertx.up.util.Ut;

/*
 * 单条记录专用处理
 */
public interface Completer {

    static Completer create(final Class<?> completerCls, final HDao dao, final DataAtom atom) {
        return Ut.instance(completerCls, dao, atom);
    }

    static Completer create(final HDao dao, final DataAtom atom) {
        return new CompleterDefault(dao, atom);
    }

    default Completer bind(final Switcher switcher) {
        return this;
    }

    default Completer bind(final JsonObject options) {
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
