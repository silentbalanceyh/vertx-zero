package io.vertx.up.experiment.shape;

import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.experiment.mixture.HModel;
import io.vertx.up.experiment.shape.atom.HAtomMetadata;
import io.vertx.up.experiment.shape.atom.HAtomReference;

import java.util.Set;

/**
 * This atom is a standard implementation because of all the attributes is static and fixed, you can not configure
 * all the information when the container is running, this model will connect to `pojo/{0}.yml` file ( It's internal
 * mapping Channel ) and capture the `Mu` definition for current static model.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NormAtom extends AbstractHAtom {

    public NormAtom(final HModel model, final String appName) {
        super(model, appName);
    }

    @Override
    public HAtom atom(final String identifier) {
        return null;
    }

    @Override
    public String sigma() {
        return null;
    }

    @Override
    public String language() {
        return null;
    }

    @Override
    public Boolean trackable() {
        return null;
    }

    @Override
    public Set<String> falseTrack() {
        return null;
    }

    @Override
    public Set<String> falseConfirm() {
        return null;
    }

    @Override
    public Set<String> falseIn() {
        return null;
    }

    @Override
    public Set<String> falseOut() {
        return null;
    }

    @Override
    public Set<String> trueTrack() {
        return null;
    }

    @Override
    public Set<String> trueConfirm() {
        return null;
    }

    @Override
    public Set<String> trueIn() {
        return null;
    }

    @Override
    public Set<String> trueOut() {
        return null;
    }

    @Override
    protected <T extends HModel> HAtomMetadata newMetadata(final T model) {
        return null;
    }

    @Override
    protected <T extends HModel> HAtomReference newReference(final T model) {
        return null;
    }
}
