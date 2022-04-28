package io.vertx.up.experiment.shape.internal;

import io.vertx.up.experiment.mixture.HApp;
import io.vertx.up.experiment.mixture.HModel;
import io.vertx.up.experiment.mixture.HPerformer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NormPerformer implements HPerformer<HModel> {
    private final String namespace;

    public NormPerformer(final String appName) {
        this.namespace = HApp.ns(appName);
    }

    @Override
    public HModel fetch(final String identifier) {
        return new NormModel(this.namespace, identifier);
    }
}
