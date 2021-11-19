package io.vertx.tp.workflow.atom;

import io.vertx.core.Future;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WProcess implements Serializable {
    private final ProcessDefinition definition;
    private final ProcessInstance instance;

    private WProcess(final ProcessDefinition definition, final ProcessInstance instance) {
        this.definition = definition;
        this.instance = instance;
    }

    public static WProcess on(final ProcessDefinition definition, final ProcessInstance instance) {
        return new WProcess(definition, instance);
    }

    public static Future<WProcess> future(final ProcessDefinition definition, final ProcessInstance instance) {
        return Ux.future(on(definition, instance));
    }

    public ProcessDefinition definition() {
        return this.definition;
    }

    public ProcessInstance instance() {
        return this.instance;
    }
}
