package cn.vertxup.battery.service;

import cn.vertxup.battery.domain.tables.pojos.BBag;
import cn.vertxup.battery.domain.tables.pojos.BBlock;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface BagArgStub {

    Future<JsonObject> fetchBagConfig(String bagAbbr);

    Future<JsonObject> fetchBag(String bagAbbr);

    Future<JsonObject> saveBag(String bagId, JsonObject data);

    Future<JsonObject> saveBagBy(String nameAbbr, JsonObject data);

    Future<List<BBlock>> seekBlocks(BBag bag);
}
