package cn.vertxup.battery.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface BagArgStub {

    Future<JsonObject> fetchBagConfig(String bagId);

    Future<JsonObject> fetchBag(String bagId);

    Future<JsonObject> saveBag(String bagId, JsonObject data);
}
