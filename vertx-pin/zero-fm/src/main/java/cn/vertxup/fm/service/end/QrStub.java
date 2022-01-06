package cn.vertxup.fm.service.end;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface QrStub {

    // Fetch Book with bill and items
    Future<JsonObject> fetchSettlement(String key);

    // Fetch Debt
    Future<JsonObject> fetchDebt(String key);

    // Fetch Payment Related
    Future<JsonArray> fetchPayment(String settlementId);
}
