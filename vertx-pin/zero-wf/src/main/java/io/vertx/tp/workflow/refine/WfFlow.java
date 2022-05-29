package io.vertx.tp.workflow.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WProcess;
import io.vertx.tp.workflow.atom.WProcessDefinition;
import io.vertx.tp.workflow.atom.WRequest;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class WfFlow {
    static Future<WProcess> process(final WRequest request) {
        final WProcess process = WProcess.create();
        final KFlow workflow = request.workflow();
        return WfCamunda.instanceById(workflow.instanceId())
            .compose(process::future/* WProcess -> Bind Process */)
            .compose(instance -> Ux.future(process));
    }

    static Future<WProcessDefinition> definition(final String instanceId) {
        // Fetch Instance First
        return WfCamunda.instanceById(instanceId).compose(instance -> {
            if (Objects.isNull(instance)) {
                // History
                return WfCamunda.instanceFinished(instanceId)
                    .compose(instanceFinished -> WfCamunda.definitionById(instanceFinished.getProcessDefinitionId())
                        .compose(definition -> WProcessDefinition.future(definition, instanceFinished))
                    );
            } else {
                // Running
                return WfCamunda.definitionById(instance.getProcessDefinitionId())
                    .compose(definition -> WProcessDefinition.future(definition, instance));
            }
        });
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
