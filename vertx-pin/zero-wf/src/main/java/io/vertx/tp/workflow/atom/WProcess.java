package io.vertx.tp.workflow.atom;

import io.vertx.core.Future;
import io.vertx.up.unity.Ux;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WProcess implements Serializable {
    private final ProcessDefinition definition;
    private final HistoricProcessInstance instanceFinished;
    private final ProcessInstance instance;

    private WProcess(final ProcessDefinition definition, final ProcessInstance instance) {
        this.definition = definition;
        this.instance = instance;
        this.instanceFinished = null;
    }

    private WProcess(final ProcessDefinition definition, final HistoricProcessInstance instance) {
        this.definition = definition;
        this.instance = null;
        this.instanceFinished = instance;
    }

    public static WProcess on(final ProcessDefinition definition, final ProcessInstance instance) {
        return new WProcess(definition, instance);
    }

    public static WProcess on(final ProcessDefinition definition, final HistoricProcessInstance instance) {
        return new WProcess(definition, instance);
    }

    public static Future<WProcess> future(final ProcessDefinition definition, final ProcessInstance instance) {
        return Ux.future(on(definition, instance));
    }

    public static Future<WProcess> future(final ProcessDefinition definition, final HistoricProcessInstance instance) {
        return Ux.future(on(definition, instance));
    }

    public ProcessDefinition definition() {
        return this.definition;
    }

    public ProcessInstance instance() {
        return this.instance;
    }

    public HistoricProcessInstance instanceFinished() {
        return this.instanceFinished;
    }

    public boolean isRunning() {
        return Objects.nonNull(this.instance);
    }
}
