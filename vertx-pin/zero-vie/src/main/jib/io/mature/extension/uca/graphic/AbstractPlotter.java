package io.mature.extension.uca.graphic;

import io.macrocosm.specification.app.HApp;
import io.macrocosm.specification.program.HArk;
import io.mature.extension.refine.Ox;
import io.modello.specification.action.HDao;
import io.vertx.up.eon.KName;

public abstract class AbstractPlotter implements Plotter {

    protected transient HArk ark;

    @Override
    public Plotter bind(final HArk ark) {
        this.ark = ark;
        return this;
    }

    protected HDao dao(final String identifier) {
        final HApp app = this.ark.app();
        return Ox.toDao(app.option(KName.APP_ID), identifier);
    }
}
