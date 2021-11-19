package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.daos.WFlowDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.DForm;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.runner.StoreOn;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FlowService implements FlowStub {
    @Override
    public Future<JsonObject> fetchFlow(final String definitionKey, final String sigma) {
        // 1. Fetch workflow definition from Camunda
        final StoreOn storeOn = StoreOn.get();
        return Wf.processByKey(definitionKey).compose(storeOn::workflowGet).compose(definition -> {
            // Fetch X_FLOW
            final JsonObject condition = Ux.whereAnd();
            condition.put(KName.CODE, definitionKey);
            condition.put(KName.SIGMA, sigma);
            return Ux.Jooq.on(WFlowDao.class).fetchJOneAsync(condition).compose(workflow -> {
                // Combine result
                final JsonObject response = new JsonObject();
                response.mergeIn(workflow);
                response.mergeIn(definition);
                return Ux.future(response);
            });
        });
    }

    @Override
    public Future<JsonObject> fetchForm(final ProcessDefinition definition, final String sigma) {
        final StoreOn storeOn = StoreOn.get();
        return storeOn.formGet(definition)
            .compose(formData -> this.fetchFormInternal(formData, sigma))
            .compose(response -> storeOn.workflowGet(definition)
                .compose(Ux.attachJ(KName.Flow.WORKFLOW, response))
            );
    }

    @Override
    public Future<JsonObject> fetchForm(final ProcessDefinition definition, final ProcessInstance instance, final String sigma) {
        final StoreOn storeOn = StoreOn.get();
        return storeOn.formGet(definition, instance)
            .compose(formData -> this.fetchFormInternal(formData, sigma))
            .compose(response -> storeOn.workflowGet(definition, instance)
                .compose(Ux.attachJ(KName.Flow.WORKFLOW, response))
            );
    }

    private Future<JsonObject> fetchFormInternal(final JsonObject formData, final String sigma) {
        final JsonObject response = new JsonObject();
        final JsonObject parameters = Wf.formInput(formData, sigma);
        return Ke.channel(DForm.class, JsonObject::new, stub -> stub.fetchUi(parameters))
            .compose(Ux.attachJ(KName.FORM, response));
    }
}
