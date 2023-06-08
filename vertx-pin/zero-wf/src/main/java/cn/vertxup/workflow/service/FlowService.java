package cn.vertxup.workflow.service;

import cn.vertxup.workflow.domain.tables.daos.WFlowDao;
import io.horizon.spi.ui.Form;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.refine.Wf;
import io.vertx.mod.workflow.uca.camunda.Io;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.model.bpmn.instance.StartEvent;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FlowService implements FlowStub {
    @Override
    public Future<JsonObject> fetchFlow(final String definitionKey, final String sigma) {
        // 1. Fetch workflow definition from Camunda
        final Io<StartEvent> io = Io.ioEventStart();
        final JsonObject workflowJ = new JsonObject();
        final ProcessDefinition definition = io.inProcess(definitionKey);
        workflowJ.mergeIn(Wf.outBpmn(definition), true);
        return io.children(definition.getId())
            .compose(starts -> io.out(workflowJ, starts))
            // Fetch X_FLOW
            .compose(definitionJ -> this.flowInternal(definitionKey, sigma).compose(workflow -> {
                // Combine result
                final JsonObject response = new JsonObject();
                response.mergeIn(workflow);
                response.mergeIn(definitionJ);
                // configuration should be JsonObject type
                Ut.valueToJObject(
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
                response.put(KName.Flow.UI_LINKAGE, Wf.outLinkage(linkageJ));
                return Ux.future(response);
            }));
    }

    @Override
    public Future<JsonObject> fetchForm(final ProcessDefinition definition,
                                        final String sigma) {
        // Io Building
        final Io<JsonObject> ioForm = Io.ioForm();
        final Io<JsonObject> ioFlow = Io.ioFlow();

        final JsonObject response = new JsonObject();
        return Ux.future(definition)


            // Form Fetch -> Out
            .compose(ioForm::start)
            .compose(formInput -> this.formInternal(formInput, sigma))
            .compose(formOutput -> ioForm.out(response, formOutput))


            // Workflow Fetch -> Out
            .compose(nil -> ioFlow.start(definition))
            .compose(workflow -> ioFlow.out(response, workflow));
    }

    @Override
    public Future<JsonObject> fetchForm(final ProcessInstance instance, final Task task,
                                        final String sigma) {
        final JsonObject response = new JsonObject();
        final Io<JsonObject> ioForm = Io.ioForm();
        final Io<JsonObject> ioFlow = Io.ioFlow();
        return Ux.future(task)
            // Fetch form data
            .compose(ioForm::run)
            .compose(formInput -> this.formInternal(formInput, sigma))
            .compose(formOutput -> ioForm.out(response, formOutput))

            // Fetch workflow
            .compose(nil -> ioFlow.run(task))
            .compose(workflow -> ioFlow.out(response, workflow));
    }

    @Override
    public Future<JsonObject> fetchForm(final HistoricProcessInstance instance,
                                        final String sigma) {
        final JsonObject response = new JsonObject();

        final Io<JsonObject> ioForm = Io.ioForm();
        final Io<JsonObject> ioFlow = Io.ioFlow();
        return Ux.future(instance)


            // Fetch form data
            .compose(ioForm::end)
            .compose(formInput -> this.formInternal(formInput, sigma))
            .compose(formOutput -> ioForm.out(response, formOutput))


            // Fetch workflow
            .compose(nil -> ioFlow.end(instance))
            .compose(workflow -> ioFlow.out(response, workflow));
    }

    private Future<JsonObject> flowInternal(final String definitionKey, final String sigma) {
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.CODE, definitionKey);
        condition.put(KName.SIGMA, sigma);
        return Ux.Jooq.on(WFlowDao.class).fetchJOneAsync(condition);
    }

    private Future<JsonObject> formInternal(final JsonObject formInput, final String sigma) {
        final JsonObject parameters = formInput.copy();
        parameters.put(KName.SIGMA, sigma);
        return Ux.channel(Form.class, JsonObject::new, stub -> stub.fetchUi(parameters));
    }
}
