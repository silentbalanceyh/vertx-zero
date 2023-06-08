package io.vertx.up.atom.extension;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KFlow implements Serializable {
    private final String definitionKey;
    private final String definitionId;
    private final String instanceId;
    private final String taskId;

    private KFlow(final JsonObject workflow) {
        this.definitionKey = workflow.getString(KName.Flow.DEFINITION_KEY);
        this.definitionId = workflow.getString(KName.Flow.DEFINITION_ID);
        this.instanceId = workflow.getString(KName.Flow.INSTANCE_ID);
        this.taskId = workflow.getString(KName.Flow.TASK_ID);
    }

    public static KFlow build(final JsonObject params) {
        final JsonObject workflow = params.getJsonObject(KName.Flow.WORKFLOW);
        return new KFlow(workflow);
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
