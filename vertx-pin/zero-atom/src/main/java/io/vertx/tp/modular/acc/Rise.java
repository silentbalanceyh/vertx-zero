package io.vertx.tp.modular.acc;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.up.atom.record.Apt;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Rise {
    /*
     * 读取增量结果
     * ADD - 新增
     * UPDATE - 更新
     * DELETE - 删除
     */
    Future<Apt> fetchAcc(JsonObject criteria, DataAtom atom);
    
}
