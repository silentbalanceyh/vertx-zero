package cn.vertxup.integration.service;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface DirStub {

    Future<JsonObject> create(JsonObject directoryJ);

    Future<JsonObject> update(String key, JsonObject directoryJ);

    Future<JsonObject> updateBranch(IDirectory directory);

    Future<Boolean> remove(String key);

    Future<Boolean> remove(String key, String userId);
}
