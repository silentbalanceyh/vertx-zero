package io.aeon.experiment.shape.internal;

import io.aeon.experiment.shape.AbstractHAtom;
import io.modello.specification.atom.HModel;

/**
 * This atom is a standard implementation because of all the attributes is static and fixed, you can not configure
 * all the information when the container is running, this model will connect to `pojo/{0}.yml` file ( It's internal
 * mapping Channel ) and capture the `Mu` definition for current static model.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NormAtom extends AbstractHAtom {

    public NormAtom(final HModel model) {
        super(model);
    }

    @Override
    public NormAtom copy(final String identifier) {
        return new NormAtom(this.model());
    }
}
