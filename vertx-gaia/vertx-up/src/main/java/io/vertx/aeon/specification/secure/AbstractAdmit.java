package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.Hoi;
import io.vertx.aeon.specification.app.HES;
import io.vertx.up.experiment.mixture.HAtom;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AbstractAdmit implements HAdmit {
    protected transient HAtom atom;
    protected transient Hoi owner;

    @Override
    public HAdmit bind(final HAtom atom) {
        this.atom = atom;
        this.owner = HES.caller(atom.sigma());
        return this;
    }
}
