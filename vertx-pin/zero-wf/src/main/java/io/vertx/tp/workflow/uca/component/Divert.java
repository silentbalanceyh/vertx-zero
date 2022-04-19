package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WRecord;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Divert {

    Divert bind(JsonObject config);

    /*
     *  Event Fire by Programming
     */
    Future<WRecord> goOnAsync(WRecord record, WProcess process);
}
