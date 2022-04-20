package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.daos.WFlowDao;
import cn.zeroup.macrocosm.cv.WfCv;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.ui.Form;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.runner.StoreOn;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
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
        return Wf.definitionByKey(definitionKey).compose(storeOn::workflowGet).compose(definition -> {
            // Fetch X_FLOW
            final JsonObject condition = Ux.whereAnd();
            condition.put(KName.CODE, definitionKey);
            condition.put(KName.SIGMA, sigma);
            return Ux.Jooq.on(WFlowDao.class).fetchJOneAsync(condition).compose(workflow -> {
                // Combine result
                final JsonObject response = new JsonObject();
                response.mergeIn(workflow);
                response.mergeIn(definition);
                // configuration should be JsonObject type
                return Ut.ifJObject(
                    KName.Flow.CONFIG_START,
                    KName.Flow.CONFIG_AUTHORIZED,
                    KName.Flow.CONFIG_END,
                    KName.Flow.CONFIG_RUN,
                    KName.Flow.CONFIG_GENERATE,
                    KName.Flow.UI_CONFIG,
                    KName.Flow.UI_ASSIST,
                    KName.Flow.UI_LINKAGE
                ).apply(response);
            });
        });
    }

    @Override
    public Future<JsonObject> fetchFormStart(final ProcessDefinition definition, final String sigma) {
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

    @Override
    public Future<JsonObject> fetchFormEnd(final ProcessDefinition definition, final HistoricProcessInstance instance, final String sigma) {
        final StoreOn storeOn = StoreOn.get();
        final JsonObject response = new JsonObject();
        return storeOn.workflowGet(definition, instance)
            .compose(Ux.attachJ(KName.Flow.WORKFLOW, response))
            .compose(processed -> {
                final JsonObject workflow = Ut.valueJObject(processed.getJsonObject(KName.Flow.WORKFLOW));
                final JsonObject formData = workflow.copy();
                formData.put(KName.CODE, WfCv.CODE_HISTORY);
                return this.fetchFormInternal(formData, sigma);
            })
            .compose(formData -> Ux.future(response.mergeIn(formData)));
    }

    private Future<JsonObject> fetchFormInternal(final JsonObject formData, final String sigma) {
        final JsonObject response = new JsonObject();
        final JsonObject parameters = Wf.formInput(formData, sigma);
        return Ke.channel(Form.class, JsonObject::new, stub -> stub.fetchUi(parameters))
            .compose(Ux.attachJ(KName.FORM, response));
    }
}
