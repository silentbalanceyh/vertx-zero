package io.horizon.spi.cloud.query;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;

/**
 * QBE专用流程，解析 QBE 参数
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HQBE {

    Future<Envelop> before(JsonObject qbeJ, Envelop envelop);
}
