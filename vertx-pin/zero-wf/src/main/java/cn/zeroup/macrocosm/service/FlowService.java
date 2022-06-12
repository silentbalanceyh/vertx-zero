package cn.zeroup.macrocosm.service;

import cn.vertxup.workflow.domain.tables.daos.WFlowDao;
import cn.zeroup.macrocosm.cv.WfCv;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.ui.Form;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.camunda.Io;
import io.vertx.tp.workflow.uca.runner.StoreOn;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.form.FormData;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FlowService implements FlowStub {
    @Override
    public Future<JsonObject> fetchFlow(final String definitionKey, final String sigma) {
        // 1. Fetch workflow definition from Camunda
        final Io<StartEvent, ProcessDefinition> io = Io.ioEventStart();
        final JsonObject workflowJ = new JsonObject();
        final ProcessDefinition definition = io.pDefinition(definitionKey);
        workflowJ.mergeIn(Wf.bpmnOut(definition), true);
        return io.children(definition.getId())
            .compose(starts -> io.out(workflowJ, starts))
            .compose(definitionJ -> {
                // Fetch X_FLOW
                final JsonObject condition = Ux.whereAnd();
                condition.put(KName.CODE, definitionKey);
                condition.put(KName.SIGMA, sigma);
                return Ux.Jooq.on(WFlowDao.class).fetchJOneAsync(condition).compose(workflow -> {
                    // Combine result
                    final JsonObject response = new JsonObject();
                    response.mergeIn(workflow);
                    response.mergeIn(definitionJ);
                    // configuration should be JsonObject type
                    Ut.ifJObject(
                        response,
                        KName.Flow.CONFIG_START,
                        KName.Flow.CONFIG_AUTHORIZED,
                        KName.Flow.CONFIG_END,
                        KName.Flow.CONFIG_RUN,
                        KName.Flow.CONFIG_GENERATE,
                        KName.Flow.UI_CONFIG,
                        KName.Flow.UI_ASSIST,
                        KName.Flow.UI_LINKAGE
                    );
                    // Simply the linkage configuration for sharing global part
                    final JsonObject linkageJ = Ut.valueJObject(response, KName.Flow.UI_LINKAGE);
                    response.put(KName.Flow.UI_LINKAGE, Wf.processLinkage(linkageJ));
                    return Ux.future(response);
                });
            });
    }

    @Override
    public Future<JsonObject> fetchFormStart(final String definitionId,
                                             final String sigma) {
        // Io Building
        final Io<FormData, ProcessDefinition> ioForm = Io.ioForm();
        final Io<JsonObject, ProcessDefinition> ioFlow = Io.ioFlow();

        final JsonObject response = new JsonObject();
        return Ux.future(definitionId)


            // Form Fetch -> Out
            .compose(ioForm::start)
            .compose(formData -> ioForm.out(response, formData))
            .compose(formData -> this.fetchFormInternal(formData, sigma))


            // Workflow Fetch -> Out
            .compose(nil -> ioFlow.start(definitionId))
            .compose(workflow -> ioFlow.out(response, workflow));
    }

    @Override
    public Future<JsonObject> fetchForm(final ProcessDefinition definition, final ProcessInstance instance,
                                        final String sigma) {
        final StoreOn storeOn = StoreOn.get();
        return storeOn.formGet(definition, instance)
            .compose(formData -> this.fetchFormInternal(formData, sigma))
            .compose(response -> storeOn.workflowGet(definition, instance)
                .compose(Ux.attachJ(KName.Flow.WORKFLOW, response))
            );
    }

    @Override
    public Future<JsonObject> fetchFormEnd(final ProcessDefinition definition, final HistoricProcessInstance instance,
                                           final String sigma) {
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
        return Ux.channel(Form.class, JsonObject::new, stub -> stub.fetchUi(parameters))
            .compose(Ux.attachJ(KName.FORM, response));
    }
}
