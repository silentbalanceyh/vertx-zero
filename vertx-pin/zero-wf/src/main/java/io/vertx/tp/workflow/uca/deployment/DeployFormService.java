package io.vertx.tp.workflow.uca.deployment;

import io.vertx.core.Future;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.repository.DeploymentBuilder;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class DeployFormService implements DeployStub {
    private final transient DeploymentBuilder builderRef;
    private final transient Set<String> formFiles = new HashSet<>();
    private final transient String workflow;

    DeployFormService(final String workflow, final DeploymentBuilder builder) {
        this.builderRef = builder;
        this.workflow = workflow;
    }

    public DeployStub forms(final Set<String> formFiles) {
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
                Wf.Log.warnDeploy(this.getClass(), "Ignored: `{0}` does not exist.", filePath);
            }
        });
        return Ux.futureT();
    }
}
