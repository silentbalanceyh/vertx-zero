package io.vertx.mod.workflow.uca.deployment;

import io.vertx.core.Future;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.repository.DeploymentBuilder;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static io.vertx.mod.workflow.refine.Wf.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DeployFormService implements DeployOn {
    private final transient DeploymentBuilder builderRef;
    private final transient Set<String> formFiles = new HashSet<>();
    private final transient String workflow;

    DeployFormService(final String workflow, final DeploymentBuilder builder) {
        this.builderRef = builder;
        this.workflow = workflow;
    }

    public DeployOn forms(final Set<String> formFiles) {
        if (Objects.nonNull(formFiles)) {
            this.formFiles.addAll(formFiles);
        }
        return this;
    }

    @Override
    public Future<Boolean> initialize() {
        Objects.requireNonNull(this.builderRef);
        if (this.formFiles.isEmpty()) {
            return Ux.futureT();
        }
        this.formFiles.forEach(formFile -> {
            final String filePath = this.workflow + "/" + formFile;
            final InputStream istream = Ut.ioStream(filePath);
            if (Objects.nonNull(istream)) {
                this.builderRef.addInputStream(formFile, istream);
            } else {
                LOG.Deploy.warn(this.getClass(), "Ignored: `{0}` does not exist.", filePath);
            }
        });
        return Ux.futureT();
    }
}
