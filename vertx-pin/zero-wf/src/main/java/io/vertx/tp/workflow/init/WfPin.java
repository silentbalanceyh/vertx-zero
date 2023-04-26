package io.vertx.tp.workflow.init;

import cn.vertxup.workflow.domain.tables.pojos.WFlow;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.workflow.uca.deployment.DeployOn;
import io.vertx.up.fn.Fn;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.impl.history.handler.HistoryEventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static io.vertx.tp.workflow.refine.Wf.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WfPin {

    public static Future<Boolean> init(final Vertx vertx) {
        Ke.banner("「Ροή εργασίας」- ( Workflow )");
        LOG.Init.info(WfPin.class, "WfConfiguration...");
        WfConfiguration.init();
        LOG.Init.info(WfPin.class, "WfTodo...");
        WfTodo.init();
        LOG.Init.info(WfPin.class, "Workflow Engine Start...");
        final List<String> resources = WfConfiguration.camundaResources();
        LOG.Init.info(WfPin.class, "Here are {0} folder that will be waited for deployment...",
            String.valueOf(resources.size()));

        final List<Future<Boolean>> futures = new ArrayList<>();
        // Deployment for .bpmn files
        resources.forEach(resource -> DeployOn.get(resource).initialize());
        return Fn.combineT(futures)
            // Flow initialized
            .compose(nil -> WfConfiguration.init(vertx));
    }

    public static RepositoryService camundaRepository() {
        return WfConfiguration.camunda().getRepositoryService();
    }

    public static FormService camundaForm() {
        return WfConfiguration.camunda().getFormService();
    }

    public static RuntimeService camundaRuntime() {
        return WfConfiguration.camunda().getRuntimeService();
    }

    public static TaskService camundaTask() {
        return WfConfiguration.camunda().getTaskService();
    }

    public static HistoryService camundaHistory() {
        return WfConfiguration.camunda().getHistoryService();
    }

    public static HistoryEventHandler camundaLogger() {
        return WfConfiguration.camundaLogger();
    }

    /**
     * Return to configuration data that convert to {@link io.vertx.core.json.JsonObject} here by type.
     *
     * @param type {@link java.lang.String} The type value passed.
     *
     * @return {@link io.vertx.core.json.JsonObject}
     */
    public static JsonObject getTodo(final String type) {
        return WfTodo.getTodo(type);
    }

    public static WFlow getFlow(final String code) {
        return WfConfiguration.workflow(code);
    }

    public static Set<String> getBuiltIn() {
        return WfConfiguration.camundaBuiltIn();
    }
}
