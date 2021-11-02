package cn.vertxup.fm.service;

import cn.vertxup.fm.domain.tables.pojos.FBook;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.KSpec;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface BookStub {
    // Fetch Books
    Future<List<FBook>> fetchAsync(JsonObject criteria);

    // Fetch By Order
    Future<List<FBook>> fetchByOrder(String orderId);

    // Create Main
    Future<List<FBook>> createAsync(List<FBook> books, KSpec spec);
}
