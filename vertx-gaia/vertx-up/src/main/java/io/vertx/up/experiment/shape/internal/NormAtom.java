package io.vertx.up.experiment.shape.internal;

import io.vertx.up.experiment.mixture.HModel;
import io.vertx.up.experiment.shape.AbstractHAtom;
import io.vertx.up.experiment.specification.power.KApp;

/**
 * This atom is a standard implementation because of all the attributes is static and fixed, you can not configure
 * all the information when the container is running, this model will connect to `pojo/{0}.yml` file ( It's internal
 * mapping Channel ) and capture the `Mu` definition for current static model.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NormAtom extends AbstractHAtom {

    public NormAtom(final KApp app, final HModel model) {
        super(app, model);
    }

    @Override
    public NormAtom atom(final String identifier) {
        return new NormAtom(this.app, this.model());
    }
}
