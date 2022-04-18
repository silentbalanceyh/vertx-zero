package cn.originx.uca.graphic;

import cn.originx.refine.Ox;
import io.vertx.tp.jet.atom.JtApp;
import io.vertx.up.experiment.meld.HDao;

public abstract class AbstractPlotter implements Plotter {

    protected transient JtApp app;

    @Override
    public Plotter bind(final JtApp app) {
        this.app = app;
        return this;
    }

    protected HDao dao(final String identifier) {
        return Ox.toDao(this.app.getAppId(), identifier);
    }
}
