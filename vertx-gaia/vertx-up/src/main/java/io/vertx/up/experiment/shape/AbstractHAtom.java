package io.vertx.up.experiment.shape;

import io.vertx.up.experiment.mixture.HApp;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.experiment.mixture.HModel;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractHAtom implements HAtom {
    protected final String unique;
    protected final String appName;

    public AbstractHAtom(final HModel model, final String appName) {
        Objects.requireNonNull(model);
        this.appName = appName;
        this.unique = HApp.ns(appName, model.identifier());
    }
}
