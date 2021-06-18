package io.vertx.tp.modular.acc;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.commune.config.Database;
import io.vertx.up.fn.Fn;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Rise {

    static Rise rapid() {
        return Fn.poolThread(Pool.POOL_RAPID, RiseRapid::new);
    }

    static String key(final JsonObject cond, final DataAtom atom) {
        return atom.identifier() + "-" + cond.hashCode();
    }

    Rise bind(Database database);

    /*
     * 读取增量结果
     * ADD - 新增
     * UPDATE - 更新
     * DELETE - 删除
     */
    Future<Apt> fetchBatch(JsonObject criteria, DataAtom atom);

    Future<Boolean> writeData(String key, JsonArray data, DataAtom atom);
}
