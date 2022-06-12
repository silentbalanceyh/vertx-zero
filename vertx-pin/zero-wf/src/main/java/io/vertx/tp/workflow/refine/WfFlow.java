package io.vertx.tp.workflow.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.runtime.WProcess;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class WfFlow {
    static Future<WProcess> process(final WRequest request) {
        final WProcess process = WProcess.create();
        final KFlow workflow = request.workflow();
        final Io<Void, Void> io = Io.io();
        final ProcessInstance instance = io.inInstance(workflow.instanceId());
        return process.instance(instance/* WProcess -> Bind Process */)
            .compose(bind -> Ux.future(process));
    }

    static JsonObject processLinkage(final JsonObject linkageJ) {
        final JsonObject parsed = new JsonObject();
        linkageJ.fieldNames().forEach(field -> {
            final Object value = linkageJ.getValue(field);
            /*
             * Secondary format for
             * field1: path1
             * field1: path2
             */
            JsonObject json = null;
            if (value instanceof String) {
                json = Ut.ioJObject(value.toString());
            } else if (value instanceof JsonObject) {
                json = (JsonObject) value;
            }
            if (Ut.notNil(json)) {
                assert json != null;
                parsed.put(field, json.copy());
            }
        });
        return parsed;
    }
}
