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

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FlowService implements FlowStub {
    @Override
    public Future<JsonObject> fetchFlow(final String code, final String sigma) {
        // 1. Fetch workflow definition from Camunda
        final StoreOn storeOn = StoreOn.get();
        return storeOn.processByKey(code).compose(definition -> {
            // Fetch X_FLOW
            final JsonObject condition = Ux.whereAnd();
            condition.put(KName.CODE, code);
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
    public Future<JsonObject> fetchFirst(final String definitionId, final String sigma) {
        final StoreOn storeOn = StoreOn.get();
        // 1. Fetch workflow
        final JsonObject response = new JsonObject();
        return storeOn.formById(definitionId, false).compose(formData -> {
            // code
            final JsonObject parameters = Wf.argsForm(formData, sigma);
            return Ke.channel(DForm.class, JsonObject::new, stub -> stub.fetchUi(parameters));
        }).compose(form -> {
            response.put(KName.FORM, form);
            // definition
            return storeOn.processById(definitionId);
        }).compose(workflow -> {
            response.put(KName.Flow.WORKFLOW, workflow);
            return Ux.future(response);
        });
    }
}
