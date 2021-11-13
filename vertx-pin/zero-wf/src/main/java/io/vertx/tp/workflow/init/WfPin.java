package io.vertx.tp.workflow.init;

import cn.vertxup.workflow.domain.tables.pojos.WFlow;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.deployment.DeployOn;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WfPin {

    public static Future<Boolean> init(final Vertx vertx) {
        Ke.banner("「Ροή εργασίας」- ( Workflow )");
        Wf.Log.infoInit(WfPin.class, "WfConfiguration...");
        WfConfiguration.init();
        Wf.Log.infoInit(WfPin.class, "WfTodo...");
        WfTodo.init();
        Wf.Log.infoInit(WfPin.class, "Workflow Engine Start...");
        final List<String> resources = WfConfiguration.camundaResources();
        Wf.Log.infoInit(WfPin.class, "Here are {0} folder that will be waited for deployment...",
            String.valueOf(resources.size()));

        final List<Future<Boolean>> futures = new ArrayList<>();
        // Deployment for .bpmn files
        resources.forEach(resource -> DeployOn.get(resource).initialize());
        return Ux.thenCombineT(futures)
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
}
