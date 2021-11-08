package io.vertx.tp.workflow.uca.deployment;

import io.vertx.up.eon.FileSuffix;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.ProcessEngine;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractCamunda implements InitStub {
    protected final transient ProcessEngine engine;
    protected final transient String workflowBpmn;
    protected final transient Set<String> forms;

    protected AbstractCamunda(final String folder, final ProcessEngine engine) {
        this.engine = engine;
        // Files List
        final List<String> files = Ut.ioFiles(folder);
        this.workflowBpmn = files.stream().filter(item -> item.endsWith(Strings.DOT + FileSuffix.BPMN))
            .findAny().orElse(null);
        Objects.requireNonNull(this.workflowBpmn);
        this.forms = files.stream().filter(item -> item.endsWith(Strings.DOT + FileSuffix.BPMN_FORM))
            .collect(Collectors.toSet());
    }
}
