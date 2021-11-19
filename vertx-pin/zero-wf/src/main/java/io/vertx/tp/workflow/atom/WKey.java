package io.vertx.tp.workflow.atom;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WKey implements Serializable {
    private final transient String definitionKey;
    private final transient String definitionId;
    private final transient String instanceId;
    private final transient String taskId;

    private WKey(final JsonObject workflow) {
        this.definitionKey = workflow.getString(KName.Flow.DEFINITION_KEY);
        this.definitionId = workflow.getString(KName.Flow.DEFINITION_ID);
        this.instanceId = workflow.getString(KName.Flow.INSTANCE_ID);
        this.taskId = workflow.getString(KName.Flow.TASK_ID);
    }

    public static WKey build(final JsonObject params) {
        final JsonObject workflow = params.getJsonObject(KName.Flow.WORKFLOW);
        return new WKey(workflow);
    }

    public String definitionKey() {
        return this.definitionKey;
    }

    public String definitionId() {
        return this.definitionId;
    }

    public String instanceId() {
        return this.instanceId;
    }

    public String taskId() {
        return this.taskId;
    }
}
