package cn.vertxup.battery.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface BagStub {

    Future<JsonArray> fetchBag(String appId);

    Future<JsonArray> fetchExtension(String appId);
}
