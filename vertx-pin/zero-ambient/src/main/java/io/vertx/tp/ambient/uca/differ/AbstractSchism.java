package io.vertx.tp.ambient.uca.differ;

import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._409TrackableConflictException;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.experiment.mu.KMarker;
import io.vertx.up.unity.Ux;

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
        final KMarker marker = this.atom.marker();
        return marker.onTrack();
    }

    // ---------------------- Provide the default operation to throw 501 ---------------------

    @Override
    public Future<JsonObject> diffAsync(final JsonObject recordO, final JsonObject recordN, final Supplier<Future<XActivity>> activityFn) {
        // Default should be 501
        return Ux.thenError(_501NotSupportException.class, this.getClass());
    }
}
