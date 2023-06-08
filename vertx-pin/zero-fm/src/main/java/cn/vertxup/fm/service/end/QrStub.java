package cn.vertxup.fm.service.end;

import cn.vertxup.fm.domain.tables.pojos.FDebt;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface QrStub {

    // Fetch Book with bill and items
    Future<JsonObject> fetchSettlement(String key);

    // Fetch Debt
    Future<JsonObject> fetchDebt(String key);

    Future<ConcurrentMap<String, FDebt>> fetchDebtMap(Set<String> settlementId);

    // Fetch Payment Related
    Future<JsonArray> fetchPayment(String settlementId, boolean tree);
}
