package cn.vertxup.battery.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface BlockStub {

    Future<ConcurrentMap<String, JsonArray>> fetchByApp(String appId);

    Future<ConcurrentMap<String, JsonArray>> fetchByBag(Set<String> bagIds);

    Future<JsonArray> fetchByBag(String bagId);
}
