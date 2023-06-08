package io.horizon.hoc.checker;

import io.aeon.business._403LinkDeletionException;
import io.horizon.exception.WebException;
import io.horizon.exception.web._400BadRequestException;
import io.horizon.specification.uca.HTrue;
import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Set;

/**
 * @author lang : 2023-06-03
 */
public abstract class HocTrue<T> implements HTrue<T> {

    @SuppressWarnings("all")
    private static final Cc<String, HTrue<?>> CCT_CHECKER = Cc.openThread();

    public static WebException web403Link(final Class<?> clazz, final ClusterSerializable json) {
        if (json instanceof final JsonObject data) {
            final String identifier = Ut.valueString(data, KName.IDENTIFIER);
            return new _403LinkDeletionException(clazz, identifier);
        } else if (json instanceof final JsonArray data) {
            final Set<String> identifiers = Ut.valueSetString(data, KName.IDENTIFIER);
            return new _403LinkDeletionException(clazz, Ut.fromJoin(identifiers));
        } else {
            return new _400BadRequestException(clazz);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> HTrue<T> of(final Class<?> clazz) {
        return (HTrue<T>) CCT_CHECKER.pick(() -> Ut.instance(clazz), clazz.getName());
    }
}