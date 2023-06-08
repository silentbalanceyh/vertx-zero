package cn.vertxup.workflow.service;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.runtime.WRecord;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface AclStub {

    Future<JsonObject> authorize(WRecord record, String userId);
}
