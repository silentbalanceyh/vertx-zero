package io.vertx.tp.ambient.uca.differ;

import cn.vertxup.ambient.domain.tables.daos.XActivityChangeDao;
import cn.vertxup.ambient.domain.tables.daos.XActivityDao;
import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.pojos.XActivityChange;
import io.horizon.exception.web._501NotSupportException;
import io.horizon.specification.modeler.HAtom;
import io.modello.atom.normalize.KMarkAtom;
import io.modello.eon.em.Marker;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._409TrackableConflictException;
import io.vertx.up.atom.Refer;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractSchism implements Schism {
    protected transient HAtom atom;

    @Override
    public Schism bind(final HAtom atom) {
        Objects.requireNonNull(atom);
        // First Checking
        final Boolean trackable = Objects.isNull(atom.trackable()) ? Boolean.TRUE : atom.trackable();
        if (!trackable) {
            throw new _409TrackableConflictException(this.getClass(), atom.identifier());
        }
        this.atom = atom;
        // Second Checking
        final Set<String> trackFields = this.onTrack();
        if (trackFields.isEmpty()) {
            throw new _409TrackableConflictException(this.getClass(), atom.identifier());
        }
        return this;
    }

    // ---------------------- Re-Use the Attribute -----------------------
    /*
     * Tracking field
     * isTrack = true
     */
    protected Set<String> onTrack() {
        Objects.requireNonNull(this.atom);
        final KMarkAtom marker = this.atom.marker();
        return marker.enabled(Marker.track);
    }

    protected Future<JsonObject> createActivity(final XActivity activity, final List<XActivityChange> changes) {
        final Refer responseJ = new Refer();
        return Ux.Jooq.on(XActivityDao.class).insertJAsync(activity)
            .compose(responseJ::future)
            .compose(nil -> Ux.Jooq.on(XActivityChangeDao.class).insertAsync(changes))
            .compose(nil -> Ux.future(responseJ.get()));
    }
    // ---------------------- Provide the default operation to throw 501 ---------------------

    @Override
    public Future<JsonObject> diffAsync(final JsonObject recordO, final JsonObject recordN, final Supplier<Future<XActivity>> activityFn) {
        // Default should be 501
        return Fn.outWeb(_501NotSupportException.class, this.getClass());
    }

}
